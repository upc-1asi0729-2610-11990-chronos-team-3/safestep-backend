package com.safestep.platform.commerce.infrastructure.persistence.jpa.adapters;

import com.safestep.platform.commerce.domain.model.aggregates.Order;
import com.safestep.platform.commerce.domain.model.entities.OrderItem;
import com.safestep.platform.commerce.domain.model.valueobjects.CommerceValueObjects.OrderStatus;
import com.safestep.platform.commerce.domain.repositories.OrderRepository;
import com.safestep.platform.commerce.infrastructure.persistence.jpa.entities.OrderPersistenceEntity;
import com.safestep.platform.commerce.infrastructure.persistence.jpa.repositories.OrderPersistenceRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderPersistenceRepository r;

    public OrderRepositoryImpl(OrderPersistenceRepository r) {
        this.r = r;
    }

    private Order domain(OrderPersistenceEntity e) {
        return new Order(e.getId(), e.getExternalId(), e.getUsername(),
                e.getItems().stream().map(
                        i -> new OrderItem(i.getProductId(), i.getProductName(), i.getUnitPrice(), i.getQuantity()))
                        .toList(),
                OrderStatus.from(e.getStatus()), e.getOrderedAt());
    }

    private OrderPersistenceEntity entity(Order d) {
        var e = new OrderPersistenceEntity();
        e.setId(d.getId());
        e.setExternalId(d.getExternalId());
        e.setUsername(d.getUsername());
        e.setStatus(d.getStatus().name());
        e.setOrderedAt(d.getCreatedAt());
        e.setItems(d.getItems().stream().map(i -> new OrderPersistenceEntity.OrderItemEmbeddable(i.productId(),
                i.productName(), i.unitPrice(), i.quantity())).toList());
        return e;
    }

    public List<Order> findByUsername(String u) {
        return r.findByUsernameOrderByOrderedAtDesc(u).stream().map(this::domain).toList();
    }

    public Order save(Order d) {
        var s = r.save(entity(d));
        d.setId(s.getId());
        return d;
    }

    public boolean existsByExternalId(String id) {
        return r.existsByExternalId(id);
    }
}
