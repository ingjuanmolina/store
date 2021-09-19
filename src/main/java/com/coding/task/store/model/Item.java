package com.coding.task.store.model;

public class Item {
    private BaseGood baseGood;
    private int quantity;

    public Item(BaseGood baseGood, int quantity) {
        this.baseGood = baseGood;
        this.quantity = quantity;
    }

    public BaseGood getBaseGood() {
        return baseGood;
    }

    public void setBaseGood(BaseGood baseGood) {
        this.baseGood = baseGood;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Item{" +
                "Type=" + baseGood.getDescription() +
                ", quantity=" + quantity +
                '}';
    }
}
