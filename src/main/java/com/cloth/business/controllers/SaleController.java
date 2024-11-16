package com.cloth.business.controllers;

import java.util.Date;

import com.cloth.business.entities.enums.TransactionType;
import com.cloth.business.services.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cloth.business.configurations.annotations.CheckRoles;
import com.cloth.business.entities.TradeTransaction;
import com.cloth.business.entities.enums.TransactionStatus;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

	@CheckRoles({"ROLE_ADMIN", "ROLE_SALE_CREATE"})
    @PostMapping
    public ResponseEntity<?> addPurchase(@Valid @ModelAttribute TradeTransaction saleInfo){
        saleInfo.setTimestamp(new Date());
        saleInfo.setTransactionStatus(TransactionStatus.OPEN);
        TradeTransaction sale = saleService.createSale(saleInfo);
        return ResponseEntity.ok(sale);
    }

    @CheckRoles({"ROLE_ADMIN", "ROLE_SALE_GET"})
    @GetMapping("/search")
    public ResponseEntity<?> searchPurchase(
            @RequestParam(value = "storeId", required = false) Long storeId,
            @RequestParam(value = "supplierId", required = false) Long supplierId,
            @RequestParam(value = "poNumber", required = false) String poNumber,
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
            @RequestParam(value = "status", required = false) TransactionStatus purchaseStatus, // New param
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,
            @RequestParam(value = "page", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "size", defaultValue = "5", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "desc", required = false) String sortDirection) {

        return ResponseEntity.ok(saleService.searchSale(storeId, supplierId, poNumber, purchaseStatus, fromDate, toDate, TransactionType.SALE, pageNumber, pageSize, sortBy, sortDirection));
    }
}
