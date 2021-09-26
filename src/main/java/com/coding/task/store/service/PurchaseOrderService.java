package com.coding.task.store.service;

import com.coding.task.store.entity.LineItem;
import com.coding.task.store.entity.Product;
import com.coding.task.store.entity.PurchaseOrder;
import com.coding.task.store.mapper.PurchaseOrderModelMapper;
import com.coding.task.store.model.Entry;
import com.coding.task.store.repository.LineItemRepository;
import com.coding.task.store.repository.ProductRepository;
import com.coding.task.store.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class PurchaseOrderService {
    private final LineItemRepository lineItemRepository;
    private final ProductRepository productRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    private Map<String, LineItem> chart;

    @Autowired
    public PurchaseOrderService(LineItemRepository lineItemRepository, ProductRepository productRepository, PurchaseOrderRepository purchaseOrderRepository) {
        this.lineItemRepository = lineItemRepository;
        this.productRepository = productRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    public List<com.coding.task.store.model.PurchaseOrder> findAll() {
        List<com.coding.task.store.model.PurchaseOrder> purchaseOrders = new LinkedList<>();
        for (PurchaseOrder entity : purchaseOrderRepository.findAll()) {
            purchaseOrders.add(PurchaseOrderModelMapper.mapToPurchaseOrderModel(entity));
        }

        return purchaseOrders;
    }

    public com.coding.task.store.model.PurchaseOrder createPurchaseOrder(List<Entry> entries) {
        this.chart = new HashMap<>();
        entries.forEach(this::validateAndAddItem);
        PurchaseOrder processOrder = processOrder();

        if (processOrder.getLineItems().isEmpty()) {
            throw new IllegalStateException("Order doesn't contain any items.");
        }

        return PurchaseOrderModelMapper.mapToPurchaseOrderModel(processOrder);
    }

    private void validateAndAddItem(Entry entry) {
        if (Objects.isNull(entry)) {
            return;
        }

        LineItem lineItem = this.chart.get(entry.getItemName().toUpperCase());
        if (Objects.isNull(lineItem)) {
            addNewItemToChart(entry);
        } else {
            updateItemInChart(lineItem, entry);
        }
    }

    private void addNewItemToChart(Entry entry) {
        Product product = productRepository.findByDescriptionIgnoreCase(entry.getItemName());

        if (Objects.isNull(product)) {
            throw new IllegalArgumentException("Invalid argument. " + entry.getItemName() + " is not a valid item.");
        }

        LineItem lineItem = new LineItem();
        lineItem.setProduct(product);
        lineItem.setQuantity(entry.getQuantity());
        this.chart.put(entry.getItemName().toUpperCase(), lineItem);
    }

    private void updateItemInChart(LineItem itemInChart, Entry entry) {
        String key = entry.getItemName().toUpperCase();
        itemInChart.setQuantity(itemInChart.getQuantity() + entry.getQuantity());
        this.chart.put(key, itemInChart);
    }

    private PurchaseOrder processOrder() {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.save(new PurchaseOrder());

        final Set<LineItem> lineItems = new HashSet<>();
        double total = 0;
        double valueAfterDiscount = 0;

        for (LineItem lineItem : chart.values()) {
            if (lineItem.getQuantity() > 0) {
                lineItem.setPurchase(purchaseOrder);
                lineItems.add(lineItemRepository.save(lineItem));

                total += lineItem.getProduct().getPrice() * lineItem.getQuantity();
                valueAfterDiscount += getTotalWithOffer(lineItem);
            }
        }

        if (lineItems.isEmpty()) {
            throw new IllegalStateException("Order doesn't contain any items.");
        }

        purchaseOrder.setValue(total);
        purchaseOrder.setDiscount(total - valueAfterDiscount);
        purchaseOrder.setTotalValueAfterDiscount(valueAfterDiscount);
        purchaseOrder.setLineItems(lineItems);
        purchaseOrderRepository.save(purchaseOrder);

        return purchaseOrder;
    }

    private double getTotalWithOffer(LineItem item) {
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
