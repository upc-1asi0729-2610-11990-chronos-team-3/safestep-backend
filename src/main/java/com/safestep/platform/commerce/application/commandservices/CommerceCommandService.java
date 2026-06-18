package com.safestep.platform.commerce.application.commandservices;

import com.safestep.platform.commerce.domain.model.aggregates.Order;
import com.safestep.platform.commerce.domain.model.commands.AddCartItemCommand;
import com.safestep.platform.commerce.domain.model.commands.CreateOrderCommand;
import com.safestep.platform.commerce.domain.model.commands.UpdateCartItemCommand;
import com.safestep.platform.commerce.domain.model.entities.CartItem;
import com.safestep.platform.shared.application.result.*;

public interface CommerceCommandService {
    Result<CartItem, ApplicationError> handle(AddCartItemCommand command);

    Result<CartItem, ApplicationError> handle(UpdateCartItemCommand command);

    void deleteCartItem(String username, String itemId);

    Result<Order, ApplicationError> handle(CreateOrderCommand command);
}
