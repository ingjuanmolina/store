package com.coding.task.store.model;

import java.math.BigDecimal;
import java.util.List;

public class Order {
    private List<Item> items;
    private BigDecimal value;
    private BigDecimal discount;
    private BigDecimal totalValueAfterDiscount;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public BigDecimal getTotalValue() {
        return value;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.value = totalValue;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getTotalValueAfterDiscount() {
        return totalValueAfterDiscount;
    }

    public void setTotalValueAfterDiscount(BigDecimal totalValueAfterDiscount) {
        this.totalValueAfterDiscount = totalValueAfterDiscount;
    }
}
