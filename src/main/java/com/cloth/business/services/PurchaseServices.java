package com.cloth.business.services;

import java.util.Date;

import com.cloth.business.entities.TradeTransaction;
import com.cloth.business.entities.enums.TransactionStatus;
import com.cloth.business.entities.enums.TransactionType;
import com.cloth.business.payloads.PageResponse;

public interface PurchaseServices {

	TradeTransaction createPurchase(TradeTransaction purchase);
	
	TradeTransaction updatePurchase(TradeTransaction purchase, TradeTransaction dbPurchase);
	
	  PageResponse searchPurchase(
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
	  
	  TradeTransaction getPurchaseInfoByIdAndPO(Long id, String po);

	  TradeTransaction updatePurchaseStatus(TradeTransaction purchase, TransactionStatus status);

}
