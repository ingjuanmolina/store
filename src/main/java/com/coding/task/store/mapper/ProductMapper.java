package com.coding.task.store.mapper;

import com.coding.task.store.model.Product;

public class ProductMapper {

    private ProductMapper() {}

    public static Product mapToProductModel(com.coding.task.store.entity.Product entity) {
        Product product = new Product();
        product.setId(entity.getId());
        product.setDescription(entity.getDescription());
        product.setPrice(entity.getPrice());

        return product;
    }
}
