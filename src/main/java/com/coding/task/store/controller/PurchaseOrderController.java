package com.coding.task.store.controller;

import com.coding.task.store.entity.PurchaseOrder;
import com.coding.task.store.model.Entry;
import com.coding.task.store.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/purchase-orders")
public class PurchaseOrderController {

    private final PurchaseOrderService service;

    @Autowired
    public PurchaseOrderController(PurchaseOrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Object> createOrder(@RequestBody List<Entry> entries) {
        try {
            PurchaseOrder purchaseOrder = service.getPurchaseOrder(entries);
            return new ResponseEntity<>(purchaseOrder, HttpStatus.OK);
        } catch (IllegalArgumentException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
        }
    }

}
