package com.cloth.business.services;

import java.util.Date;

import com.cloth.business.entities.Purchase;
import com.cloth.business.entities.enums.PurchaseStatus;
import com.cloth.business.payloads.PageResponse;

public interface PurchaseServices {

	Purchase createPurchase(Purchase purchase);
	
	  PageResponse searchPurchase(
		        Long storeId, 
		        Long supplierId, 
		        String poNumber, 
		        PurchaseStatus purchaseStatus,
		        Date fromDate, 
		        Date toDate, 
		        int page, 
		        int size, 
		        String sortBy, 
		        String sortDirection
		    );
	
}
