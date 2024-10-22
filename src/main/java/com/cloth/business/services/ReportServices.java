package com.cloth.business.services;

import com.cloth.business.entities.Purchase;

public interface ReportServices {
	
	public byte[] generatePODetails(Purchase purchase);

}
