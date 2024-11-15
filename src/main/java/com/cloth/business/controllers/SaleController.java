package com.cloth.business.controllers;

import java.util.Date;

import com.cloth.business.services.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cloth.business.configurations.annotations.CheckRoles;
import com.cloth.business.entities.TradeTransaction;
import com.cloth.business.entities.enums.TransactionStatus;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RestController;

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
}
