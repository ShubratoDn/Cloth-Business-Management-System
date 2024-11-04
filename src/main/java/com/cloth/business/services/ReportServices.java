package com.cloth.business.services;

import com.cloth.business.entities.TradeTransaction;

public interface ReportServices {
	
	public byte[] generatePODetails(TradeTransaction purchase);

}
