package com.cloth.business.controllers;


import com.cloth.business.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
//@RequestMapping("/api/v1/stocks")
@RequestMapping("/api/v1/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/overview")
    public ResponseEntity<?> stockOverView(
            @RequestParam(name = "storeId", required = false, defaultValue = "") Long storeId,
            @RequestParam(name = "productId", required = false, defaultValue = "") Long productId,
            @RequestParam(name = "productName", required = false, defaultValue = "") String productName,

            @RequestParam(value = "page", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "size", defaultValue = "5", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "desc", required = false) String sortDirection
    ){
        return ResponseEntity.ok(stockService.getStockOverview(storeId, productId, productName, pageNumber, pageSize, sortBy, sortDirection));
    }
    
    
    @GetMapping("/store/{storeId}")
    public ResponseEntity<?> stockOverViewByStore(@PathVariable Long storeId){
        return ResponseEntity.ok(stockService.getStockOverviewByStore(storeId));
    }
}
