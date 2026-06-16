package com.safestep.platform.commerce.domain.model.aggregates;

import com.safestep.platform.commerce.domain.model.entities.OrderItem;
import com.safestep.platform.commerce.domain.model.valueobjects.CommerceValueObjects.OrderStatus;
import org.junit.jupiter.api.Test;
import java.math.*;
import java.time.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    @Test
    void calculatesTotalFromCopiedItemPrices() {
        var order = new Order(null, "ord-1", "ana", List.of(new OrderItem("p1", "Bandage", new BigDecimal("12.50"), 2),
                new OrderItem("p2", "Mask", new BigDecimal("5.00"), 1)), OrderStatus.PENDING, LocalDate.now());
        assertEquals(new BigDecimal("30.00"), order.total());
    }
}
