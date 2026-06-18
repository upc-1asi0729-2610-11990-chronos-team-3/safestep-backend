package com.safestep.platform.commerce.domain.model.valueobjects;

import java.math.*;

public final class CommerceValueObjects {
    private CommerceValueObjects() {
    }

    public record Money(BigDecimal value) {
        public Money {
            if (value == null || value.signum() < 0)
                throw new IllegalArgumentException("Money cannot be negative");
            value = value.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public record Stock(int value) {
        public Stock {
            if (value < 0)
                throw new IllegalArgumentException("Stock cannot be negative");
        }
    }

    public enum OrderStatus {
        PENDING, PAID, SHIPPED, DELIVERED, CANCELLED;

        public static OrderStatus from(String v) {
            if (v == null)
                return PENDING;
            var n = v.toUpperCase();
            if (n.contains("COMPR") || n.contains("PAID"))
                return PAID;
            if (n.contains("ENVI") || n.contains("SHIP"))
                return SHIPPED;
            if (n.contains("ENTREG") || n.contains("DELIVER"))
                return DELIVERED;
            if (n.contains("CANCEL"))
                return CANCELLED;
            return PENDING;
        }
    }

    public enum ProductType {
        PRODUCT, KIT, SERVICE;

        public static ProductType from(String v) {
            if (v == null)
                return PRODUCT;
            try {
                return valueOf(v.toUpperCase());
            } catch (Exception e) {
                return PRODUCT;
            }
        }
    }
}
