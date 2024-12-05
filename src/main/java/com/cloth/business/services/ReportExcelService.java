package com.cloth.business.services;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

import com.cloth.business.entities.enums.TransactionStatus;
import com.cloth.business.entities.enums.TransactionType;

public interface ReportExcelService {
	public ByteArrayInputStream generateExcel(List<String[]> data);
    
	public ByteArrayInputStream downloadProfitabilityReport(
            Long storeId,
            Long supplierId,
            String transactioNumber,
            TransactionStatus purchaseStatus,
            Date fromDate,
            Date toDate,
            TransactionType transactionType
    );
}
