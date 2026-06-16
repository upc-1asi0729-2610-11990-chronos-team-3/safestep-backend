package com.safestep.platform.commerce.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record OrderResource(String id, String userId, BigDecimal total, String status, List<OrderItemResource> items,
        LocalDate createdAt) {
}
