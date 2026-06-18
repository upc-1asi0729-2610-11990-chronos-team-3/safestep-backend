package com.safestep.platform.commerce.domain.repositories;

import com.safestep.platform.commerce.domain.model.aggregates.Order;
import java.util.*;

public interface OrderRepository {
    List<Order> findByUsername(String username);

    Order save(Order order);

    boolean existsByExternalId(String id);
}
