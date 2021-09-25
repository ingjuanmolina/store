package com.coding.task.store.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import java.time.Instant;
import java.util.Date;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private double value;
    private double discount;
    private double totalValueAfterDiscount;
    private Date purchaseOrderDate;

    public PurchaseOrder() {
        this.purchaseOrderDate = Date.from(Instant.now());
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "purchaseOrder", fetch = FetchType.LAZY)
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

    public Date getPurchaseOrderDate() {
        return purchaseOrderDate;
    }

    public void setPurchaseOrderDate(Date purchaseOrderDate) {
        this.purchaseOrderDate = purchaseOrderDate;
    }
}
