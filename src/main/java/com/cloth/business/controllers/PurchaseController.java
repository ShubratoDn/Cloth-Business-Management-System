package com.cloth.business.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloth.business.configurations.annotations.CheckRoles;
import com.cloth.business.entities.Purchase;
import com.cloth.business.entities.User;
import com.cloth.business.entities.enums.PurchaseStatus;
import com.cloth.business.helpers.HelperUtils;
import com.cloth.business.services.PurchaseServices;

import jakarta.validation.Valid;
import lombok.Builder;


@RestController()
@RequestMapping("/api/v1/purchases")
public class PurchaseController {
	
	@Autowired
	private PurchaseServices purchaseServices;

	@CheckRoles({"ROLE_ADMIN", "ROLE_PURCHASE_CREATE"})
	@PostMapping
	public ResponseEntity<?> addPurchase(@Valid @ModelAttribute Purchase purchaseInfo){	
		purchaseInfo.setTimestamp(new Date());
		purchaseInfo.setPurchaseStatus(PurchaseStatus.OPEN);
		Purchase purchase = purchaseServices.createPurchase(purchaseInfo);
		return ResponseEntity.ok(purchase);
	}
	
	

	@PutMapping("/{id}/{po}")
	public ResponseEntity<?> updatePurchase(@PathVariable(name = "id") Long id, @PathVariable (name = "po") String po, @Valid @ModelAttribute Purchase purchaseInfo){
		Purchase dbPurchaseInfo = purchaseServices.getPurchaseInfoByIdAndPO(id, po);
		User loggedinUser = HelperUtils.getLoggedinUser();
		
		boolean canEdit = false;
		
		if(HelperUtils.userHasRole("ROLE_ADMIN") || HelperUtils.userHasRole("ROLE_PURCHASE_UPDATE")) {
			canEdit = true;
		}		
		if((loggedinUser.getId() == dbPurchaseInfo.getAddedBy().getId())) {
			canEdit = true;
		}
		
		if((dbPurchaseInfo.getPurchaseStatus().toString().equalsIgnoreCase("OPEN") || dbPurchaseInfo.getPurchaseStatus().toString().equalsIgnoreCase("REJECTED_MODIFIED"))) {
			if(canEdit) {
				purchaseInfo.setLastUpdatedBy(loggedinUser);
				purchaseInfo.setLastUpdatedDate(new Date());
				return ResponseEntity.ok(purchaseServices.updatePurchase(purchaseInfo, dbPurchaseInfo));
			}
		}
		throw new RequestRejectedException("Can not edit purchase order!");
	}
	
	
	
	@GetMapping("/search")
	public ResponseEntity<?> searchPurchase(
	        @RequestParam(value = "storeId", required = false) Long storeId,
	        @RequestParam(value = "supplierId", required = false) Long supplierId,
	        @RequestParam(value = "poNumber", required = false) String poNumber,
	        @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
	        @RequestParam(value = "status", required = false) PurchaseStatus purchaseStatus, // New param
	        @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,
	        @RequestParam(value = "page", defaultValue = "0", required = false) int pageNumber,
	        @RequestParam(value = "size", defaultValue = "5", required = false) int pageSize,
	        @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
	        @RequestParam(value = "sortDirection", defaultValue = "desc", required = false) String sortDirection) {

//	    // Print values in the console
//	    System.out.println("storeId: " + storeId);
//	    System.out.println("supplierId: " + supplierId);
//	    System.out.println("poNumber: " + poNumber);
//        System.out.println("purchaseStatus: " + purchaseStatus); // Print purchaseStatus
//	    System.out.println("fromDate: " + fromDate);
//	    System.out.println("toDate: " + toDate);
//	    System.out.println("pageNumber: " + pageNumber);
//	    System.out.println("pageSize: " + pageSize);
//	    System.out.println("sortBy: " + sortBy);
//	    System.out.println("sortDirection: " + sortDirection);
//	    
	    return ResponseEntity.ok(purchaseServices.searchPurchase(storeId, supplierId, poNumber, purchaseStatus, fromDate, toDate, pageNumber, pageSize, sortBy, sortDirection));
	}

	
	
	
	@GetMapping("/{id}/{po}")
	public ResponseEntity<?> purchaseDetails(@PathVariable(name = "id") Long id, @PathVariable (name = "po") String po){
		Purchase purchaseInfoByIdAndPO = purchaseServices.getPurchaseInfoByIdAndPO(id, po);
		return ResponseEntity.ok(purchaseInfoByIdAndPO);
	}
}
