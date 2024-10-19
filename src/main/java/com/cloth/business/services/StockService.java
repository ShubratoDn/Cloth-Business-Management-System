package com.cloth.business.services;

import com.cloth.business.entities.Purchase;
import com.cloth.business.entities.Stock;

import java.util.List;

public interface StockService {
    List<Stock> updateStock(Purchase purchase);
}
