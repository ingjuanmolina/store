package com.coding.task.store.controller;

import com.coding.task.store.entity.PurchaseOrder;
import com.coding.task.store.model.Entry;
import com.coding.task.store.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/purchase-orders")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    @Autowired
    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @GetMapping
    public ResponseEntity<Object> findAll() {
        return new ResponseEntity<>(purchaseOrderService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> createPurchaseOrder(@RequestBody List<Entry> entries) {
        try {
            PurchaseOrder purchaseOrder = purchaseOrderService.createPurchaseOrder(entries);
            return new ResponseEntity<>(purchaseOrder, HttpStatus.OK);
        } catch (IllegalArgumentException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
        }
    }

}
