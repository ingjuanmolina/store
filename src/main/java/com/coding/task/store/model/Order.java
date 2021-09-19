package com.coding.task.store.model;

import com.coding.task.store.factory.ItemFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Order {
    private final Map<String, Item> chart;
    private final List<Item> totalItems;
    private BigDecimal totalValue;

    public Order() {
        this.chart = new HashMap<>();
        this.totalItems = new ArrayList<>();
    }

    public void addEntries(List<Entry> entries) {
        entries.forEach(this::validateAndSaveItem);
        processOrder();

    }

    private void processOrder() {
        BigDecimal total = BigDecimal.ZERO;
        for (Item item : chart.values()) {
                if (item.getQuantity() > 0) {
                this.totalItems.add(item);
                total = total.add(item.getBaseGood().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            }
        }
        setTotalValue(total);
    }

    private void setTotalValue(BigDecimal value) {
        this.totalValue = value;
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
        Item item = ItemFactory.getItem(entry.getItemName(), entry.getQuantity());
        this.chart.put(entry.getItemName().toUpperCase(), item);
    }

    private void updateItemInChart (Entry entry) {
        String key = entry.getItemName().toUpperCase();
        Item itemInChart = this.chart.get(key);
        itemInChart.setQuantity(itemInChart.getQuantity() + entry.getQuantity());
        this.chart.put(key, itemInChart);
    }

    public List<Item> getTotalItems() {
        return totalItems;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }
}
