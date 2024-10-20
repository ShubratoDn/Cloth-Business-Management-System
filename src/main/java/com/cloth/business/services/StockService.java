package com.cloth.business.services;

import com.cloth.business.entities.Purchase;
import com.cloth.business.entities.Stock;
import com.cloth.business.payloads.PageResponse;

import java.util.List;

public interface StockService {
    List<Stock> updateStock(Purchase purchase);

    PageResponse getStockOverview(Long storeId,Long productId, String productName, int page, int size, String sortBy, String sortDirection);
}
