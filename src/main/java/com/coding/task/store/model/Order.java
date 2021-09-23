package com.coding.task.store.model;

import java.util.List;

public class Order {
    private List<Item> items;
    private double value;
    private double discount;
    private double totalValueAfterDiscount;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public double getTotalValue() {
        return value;
    }

    public void setTotalValue(double totalValue) {
        this.value = totalValue;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotalValueAfterDiscount() {
        return totalValueAfterDiscount;
    }

    public void setTotalValueAfterDiscount(double totalValueAfterDiscount) {
        this.totalValueAfterDiscount = totalValueAfterDiscount;
    }
}
