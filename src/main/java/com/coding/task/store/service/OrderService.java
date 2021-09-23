package com.coding.task.store.service;

import com.coding.task.store.entity.Product;
import com.coding.task.store.model.Entry;
import com.coding.task.store.model.Item;
import com.coding.task.store.model.Order;
import com.coding.task.store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class OrderService {

    @Autowired
    private ProductRepository productRepository;

    private Map<String, Item> chart;

    public Order getOrder(List<Entry> entries) {
        this.chart = new HashMap<>();
        entries.forEach(this::validateAndSaveItem);
        Order order = processOrder();

        if (order.getItems().isEmpty()) {
            throw new IllegalStateException("Order doesn't contain any items.");
        }

        return order;
    }

    private void validateAndSaveItem(Entry entry) {
        if (Objects.isNull(entry)) {
            return;
        }

        Item item = this.chart.get(entry.getItemName().toUpperCase());
        if (Objects.isNull(item)) {
            addNewItemToChart(entry);
        } else {
            updateItemInChart(entry);
        }
    }

    private void addNewItemToChart(Entry entry) {
        Product product = productRepository.findByDescriptionIgnoreCase(entry.getItemName());
        Item item = new Item(product, entry.getQuantity());
        this.chart.put(entry.getItemName().toUpperCase(), item);
    }

    private void updateItemInChart (Entry entry) {
        String key = entry.getItemName().toUpperCase();
        Item itemInChart = this.chart.get(key);
        itemInChart.setQuantity(itemInChart.getQuantity() + entry.getQuantity());
        this.chart.put(key, itemInChart);
    }

    private Order processOrder() {
        final List<Item> totalItems = new ArrayList<>();
        double total = 0;
        double valueAfterDiscount = 0;
        for (Item item : chart.values()) {
            if (item.getQuantity() > 0) {
                totalItems.add(item);
                total += item.getProduct().getPrice() * item.getQuantity();
                valueAfterDiscount += getTotalWithOffer(item);
            }
        }

        Order order = new Order();
        order.setItems(totalItems);
        order.setTotalValue(total);
        order.setDiscount(total - valueAfterDiscount);
        order.setTotalValueAfterDiscount(valueAfterDiscount);

        return order;
    }

    private double getTotalWithOffer(Item item) {
        int baseQuantityToSetPrice = 0;
        if (item.getProduct().getDescription().equalsIgnoreCase("orange")) {
            baseQuantityToSetPrice = item.getQuantity() - item.getQuantity() / 2;
        }

        if (item.getProduct().getDescription().equalsIgnoreCase("apple")) {
            baseQuantityToSetPrice = (item.getQuantity() / 3) * 2 + item.getQuantity() % 3;
        }

        return item.getProduct().getPrice() * baseQuantityToSetPrice;
    }

}
