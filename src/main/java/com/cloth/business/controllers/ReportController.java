package com.cloth.business.controllers;

import java.io.ByteArrayInputStream;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloth.business.configurations.annotations.CheckRoles;
import com.cloth.business.entities.enums.TransactionStatus;
import com.cloth.business.entities.enums.TransactionType;
import com.cloth.business.services.ReportExcelService;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {
    
    @Autowired
    private ReportExcelService reportExcelService;
    
    
	@CheckRoles({"ROLE_ADMIN", "ROLE_REPORT_PROFITABILITY"})
    @PostMapping("/profitability")
    public ResponseEntity<?> downloadReport(
            @RequestParam(value = "storeId", required = false) Long storeId,
            @RequestParam(value = "supplierId", required = false) Long supplierId,
            @RequestParam(value = "poNumber", required = false) String poNumber,
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
            @RequestParam(value = "transactionStatus", required = false) TransactionStatus transactionStatus,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,
            @RequestParam(value = "transactionType", required = false) TransactionType transactionType) {

		
    	ByteArrayInputStream reportStream = reportExcelService.downloadProfitabilityReport(storeId, supplierId, poNumber, transactionStatus, fromDate, toDate, transactionType);
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=profitability_report.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(reportStream.readAllBytes());
    }
}
