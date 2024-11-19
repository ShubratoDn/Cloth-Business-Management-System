package com.cloth.business.services;

import com.cloth.business.entities.TradeTransaction;
import com.cloth.business.entities.enums.TransactionStatus;
import com.cloth.business.entities.enums.TransactionType;
import com.cloth.business.payloads.PageResponse;

import java.util.Date;

public interface SaleService {

	TradeTransaction getSaleInfoByIdAndSO(Long id, String so);
	
    TradeTransaction createSale(TradeTransaction sale);

    TradeTransaction updateSale(TradeTransaction sale, TradeTransaction dbSale);
    
    PageResponse searchSale(
            Long storeId,
            Long supplierId,
            String poNumber,
            TransactionStatus purchaseStatus,
            Date fromDate,
            Date toDate,
            TransactionType transactionType,
            int page,
            int size,
            String sortBy,
            String sortDirection
    );

//    TradeTransaction updatePurchaseStatus(TradeTransaction purchase, TransactionStatus status);

}
