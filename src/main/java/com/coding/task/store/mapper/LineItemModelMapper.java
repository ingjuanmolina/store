package com.coding.task.store.mapper;

import com.coding.task.store.model.LineItem;

import java.util.HashSet;
import java.util.Set;

public class LineItemModelMapper {

    private LineItemModelMapper(){}

    public static Set<LineItem> mapToLineItemModel(Set<com.coding.task.store.entity.LineItem> lineItemEntities) {
        Set<LineItem> lineItems = new HashSet<>();
        for (com.coding.task.store.entity.LineItem entity: lineItemEntities) {
            LineItem lineItem = new LineItem();
            lineItem.setLineItemId(entity.getLineItemId());
            lineItem.setProduct(ProductMapper.mapToProductModel(entity.getProduct()));
            lineItem.setQuantity(entity.getQuantity());

            lineItems.add(lineItem);
        }

        return lineItems;
    }
}
