package com.coding.task.store.mapper;

import com.coding.task.store.model.PurchaseOrder;

public class PurchaseOrderModelMapper {

    private PurchaseOrderModelMapper() {}

    public static PurchaseOrder mapToPurchaseOrderModel(com.coding.task.store.entity.PurchaseOrder entity) {
        PurchaseOrder purchaseOrderModel = new PurchaseOrder();

        purchaseOrderModel.setId(entity.getId());
        purchaseOrderModel.setLineItems(LineItemModelMapper.mapToLineItemModel(entity.getLineItems()));
        purchaseOrderModel.setValue(entity.getValue());
        purchaseOrderModel.setDiscount(entity.getDiscount());
        purchaseOrderModel.setTotalValueAfterDiscount(entity.getTotalValueAfterDiscount());

        return purchaseOrderModel;
    }
}
