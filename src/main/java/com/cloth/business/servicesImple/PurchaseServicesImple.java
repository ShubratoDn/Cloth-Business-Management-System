package com.cloth.business.servicesImple;

import org.springframework.beans.factory.annotation.Autowired;

import com.cloth.business.entities.Purchase;
import com.cloth.business.repositories.PurchaseRepository;
import com.cloth.business.services.PurchaseServices;

public class PurchaseServicesImple implements PurchaseServices {

	@Autowired
	private PurchaseRepository purchaseRepository;
	
	@Override
	public Purchase createPurchase(Purchase purchase) {
		// TODO Auto-generated method stub
		return null;
	}

}
