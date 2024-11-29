package com.cloth.business.payloads;

import com.cloth.business.entities.Product;
import com.cloth.business.entities.Store;
import lombok.Data;

@Data
public class StockOverview {
    private Product product;
    private long totalQuantity;
    private Store store;  // Add store field
    private String productImage;  // Add product image field

    public StockOverview(Product product, long totalQuantity, Store store) {
        this.product = product;
        this.totalQuantity = totalQuantity;
        this.store = store;
        this.productImage = product.getImage();  // Assuming Product has a getImage method
    }
}