package com.cloth.business.services;

import com.cloth.business.entities.TradeTransaction;
import com.cloth.business.entities.enums.TransactionStatus;
import com.cloth.business.entities.enums.TransactionType;
import com.cloth.business.payloads.PageResponse;

import java.util.Date;
import java.util.List;

public interface TransactionService {

    PageResponse searchTransaction(
            Long storeId,
            Long supplierId,
            String transactioNumber,
            TransactionStatus purchaseStatus,
            Date fromDate,
            Date toDate,
            TransactionType transactionType,
            int page,
            int size,
            String sortBy,
            String sortDirection
    );
    
    
    List<TradeTransaction> searchTransaction(
            Long storeId,
            Long supplierId,
            String transactioNumber,
            TransactionStatus purchaseStatus,
            Date fromDate,
            Date toDate,
            TransactionType transactionType
    );
}
