package com.coding.task.store.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Purchase {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private double value;
    private double discount;
    private double totalValueAfterDiscount;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "purchase", fetch = FetchType.LAZY)
    private Set<LineItem> lineItems;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
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

    public Set<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(Set<LineItem> lineItems) {
        this.lineItems = lineItems;
    }
}
