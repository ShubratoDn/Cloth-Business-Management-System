package com.cloth.business.services;

import com.cloth.business.entities.TradeTransaction;

public interface ReportPDFServices {
	
	public byte[] generatePODetails(TradeTransaction purchase);

}
