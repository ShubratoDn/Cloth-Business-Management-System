package com.cloth.business.services;

import com.cloth.business.entities.TradeTransaction;
import com.cloth.business.entities.Stock;
import com.cloth.business.entities.StockOverview;
import com.cloth.business.payloads.PageResponse;

import java.util.List;

public interface StockService {
    List<Stock> updateStock(TradeTransaction purchase);

    PageResponse getStockOverview(Long storeId,Long productId, String productName, int page, int size, String sortBy, String sortDirection);
    
    List<StockOverview> getStockOverviewByStore(Long storeId);
}
