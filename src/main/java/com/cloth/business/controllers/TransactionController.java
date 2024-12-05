package com.cloth.business.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloth.business.configurations.annotations.CheckRoles;
import com.cloth.business.entities.enums.TransactionStatus;
import com.cloth.business.entities.enums.TransactionType;
import com.cloth.business.services.TransactionService;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;


    @CheckRoles({"ROLE_ADMIN", "ROLE_REPORT_PROFITABILITY"})
    @GetMapping("/search")
    public ResponseEntity<?> searchTransaction(
            @RequestParam(value = "storeId", required = false) Long storeId,
            @RequestParam(value = "supplierId", required = false) Long supplierId,
            @RequestParam(value = "poNumber", required = false) String poNumber,
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
            @RequestParam(value = "transactionStatus", required = false) TransactionStatus transactionStatus,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,
            @RequestParam(value = "transactionType", required = false) TransactionType transactionType, // New param
            @RequestParam(value = "page", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "size", defaultValue = "5", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "desc", required = false) String sortDirection) {

        return ResponseEntity.ok(transactionService.searchTransaction(storeId, supplierId, poNumber, transactionStatus, fromDate, toDate, transactionType , pageNumber, pageSize, sortBy, sortDirection));
    } 
        
}
