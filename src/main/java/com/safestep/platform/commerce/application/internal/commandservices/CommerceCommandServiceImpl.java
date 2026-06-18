package com.safestep.platform.commerce.application.internal.commandservices;

import com.safestep.platform.commerce.application.commandservices.CommerceCommandService;
import com.safestep.platform.commerce.domain.model.aggregates.Order;
import com.safestep.platform.commerce.domain.model.commands.AddCartItemCommand;
import com.safestep.platform.commerce.domain.model.commands.CreateOrderCommand;
import com.safestep.platform.commerce.domain.model.commands.UpdateCartItemCommand;
import com.safestep.platform.commerce.domain.model.entities.*;
import com.safestep.platform.commerce.domain.model.valueobjects.CommerceValueObjects.OrderStatus;
import com.safestep.platform.commerce.domain.repositories.*;
import com.safestep.platform.shared.application.result.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.*;

@Service
public class CommerceCommandServiceImpl implements CommerceCommandService {
    private final ProductRepository products;
    private final ShoppingCartRepository carts;
    private final OrderRepository orders;

    public CommerceCommandServiceImpl(ProductRepository p, ShoppingCartRepository c, OrderRepository o) {
        products = p;
        carts = c;
        orders = o;
    }

    @Override
    public Result<CartItem, ApplicationError> handle(AddCartItemCommand c) {
        var p = products.findByExternalId(c.productId());
        if (p.isEmpty())
            return Result.failure(ApplicationError.notFound("product", c.productId()));
        if (c.quantity() < 1 || c.quantity() > p.get().getStock())
            return Result.failure(ApplicationError.businessRuleViolation("cart quantity",
                    "Quantity must be between one and available stock"));
        return Result.success(carts.save(new CartItem(null, "cart-" + UUID.randomUUID(), c.username(), c.productId(),
                c.quantity(), Instant.now())));
    }

    @Override
    public Result<CartItem, ApplicationError> handle(UpdateCartItemCommand c) {
        var found = carts.findByExternalIdAndUsername(c.itemId(), c.username());
        if (found.isEmpty())
            return Result.failure(ApplicationError.notFound("cart item", c.itemId()));
        var p = products.findByExternalId(found.get().getProductId());
        if (p.isEmpty() || c.quantity() < 1 || c.quantity() > p.get().getStock())
            return Result.failure(
                    ApplicationError.businessRuleViolation("cart quantity", "Quantity exceeds available stock"));
        found.get().changeQuantity(c.quantity());
        return Result.success(carts.save(found.get()));
    }

    @Override
    public void deleteCartItem(String u, String id) {
        carts.findByExternalIdAndUsername(id, u).ifPresent(carts::delete);
    }

    @Transactional
    @Override
    public Result<Order, ApplicationError> handle(CreateOrderCommand c) {
        var cart = carts.findByUsername(c.username());
        if (cart.isEmpty())
            return Result.failure(ApplicationError.businessRuleViolation("order", "Cart is empty"));
        var items = new ArrayList<OrderItem>();
        for (var item : cart) {
            var p = products.findByExternalId(item.getProductId());
            if (p.isEmpty())
                return Result.failure(ApplicationError.notFound("product", item.getProductId()));
            try {
                p.get().reduceStock(item.getQuantity());
            } catch (IllegalArgumentException e) {
                return Result.failure(ApplicationError.businessRuleViolation("stock", e.getMessage()));
            }
            products.save(p.get());
            items.add(
                    new OrderItem(p.get().getExternalId(), p.get().getName(), p.get().getPrice(), item.getQuantity()));
        }
        var order = new Order(null, "ord-" + UUID.randomUUID(), c.username(), items, OrderStatus.from(c.status()),
                LocalDate.now());
        orders.save(order);
        carts.deleteByUsername(c.username());
        return Result.success(order);
    }
}
