package com.cloth.business.services;

import java.util.Date;

import com.cloth.business.entities.Purchase;
import com.cloth.business.entities.enums.PurchaseStatus;
import com.cloth.business.payloads.PageResponse;

public interface PurchaseServices {

	Purchase createPurchase(Purchase purchase);
	
	Purchase updatePurchase(Purchase purchase, Purchase dbPurchase);
	
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
	  
	  Purchase getPurchaseInfoByIdAndPO(Long id, String po);
	
}
