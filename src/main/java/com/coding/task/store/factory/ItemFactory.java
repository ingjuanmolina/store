package com.coding.task.store.factory;

import com.coding.task.store.model.Apple;
import com.coding.task.store.model.Item;
import com.coding.task.store.model.Orange;

public class ItemFactory {
    private static final String APPLE = "Apple";
    private static final String ORANGE = "Orange";

    private ItemFactory() {}

    public static Item getItem(String type, int quantity) {
        if (type.equalsIgnoreCase(APPLE)) {
            return new Item(new Apple(), quantity);
        }

        if (type.equalsIgnoreCase(ORANGE)) {
            return new Item(new Orange(), quantity);
        }

        throw new IllegalArgumentException("Invalid argument. " + type + "is not a valid type.");
    }
}
