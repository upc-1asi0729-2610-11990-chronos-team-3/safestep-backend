package com.safestep.platform.commerce.domain.model.aggregates;

import com.safestep.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.safestep.platform.commerce.domain.model.entities.OrderItem;
import com.safestep.platform.commerce.domain.model.valueobjects.CommerceValueObjects.OrderStatus;
import java.math.*;
import java.time.*;
import java.util.*;

public class Order extends AbstractDomainAggregateRoot<Order> {
    private Long id;
    private final String externalId, username;
    private final List<OrderItem> items;
    private final OrderStatus status;
    private final LocalDate createdAt;

    public Order(Long id, String externalId, String username, List<OrderItem> items, OrderStatus status, LocalDate at) {
        if (items == null || items.isEmpty())
            throw new IllegalArgumentException("Order must contain items");
        this.id = id;
        this.externalId = externalId;
        this.username = username;
        this.items = List.copyOf(items);
        this.status = status;
        createdAt = at;
    }

    public BigDecimal total() {
        return items.stream().map(OrderItem::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long v) {
        id = v;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getUsername() {
        return username;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }
}
