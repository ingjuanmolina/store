package com.coding.task.store.model;

import java.math.BigDecimal;
import java.util.Objects;

public class BaseGood {
    String description;
    BigDecimal price;

    public BaseGood(String description, BigDecimal price) {
        this.description = description;
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (Objects.isNull(description) || description.isEmpty()) {
            throw new IllegalArgumentException("Invalid argument. Description can't be null or empty.");
        }
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid argument. Price must be greater than zero.");
        }
        this.price = price;
    }
}
