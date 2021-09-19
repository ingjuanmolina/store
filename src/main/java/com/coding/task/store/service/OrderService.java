package com.coding.task.store.service;

import com.coding.task.store.model.Entry;
import com.coding.task.store.model.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    public Order getOrder(List<Entry> entries) {
        Order order = new Order();
        order.addEntries(entries);
        if (order.getTotalItems().isEmpty()) {
            throw new IllegalStateException("Order doesn't contain items.");
        }

        return order;
    }
}
