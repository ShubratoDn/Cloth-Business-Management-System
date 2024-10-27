package com.cloth.business.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.*;

import com.cloth.business.configurations.annotations.CheckRoles;
import com.cloth.business.entities.Purchase;
import com.cloth.business.entities.User;
import com.cloth.business.entities.enums.PurchaseStatus;
import com.cloth.business.exceptions.ResourceNotFoundException;
import com.cloth.business.helpers.HelperUtils;
import com.cloth.business.services.PurchaseServices;
import com.cloth.business.services.ReportServices;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


@RestController()
@RequestMapping("/api/v1/purchases")
public class PurchaseController {
	
	@Autowired
	private PurchaseServices purchaseServices;

	@Autowired
	private ReportServices reportServices; 
	
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
		
		if((dbPurchaseInfo.getPurchaseStatus().toString().equalsIgnoreCase("OPEN") || dbPurchaseInfo.getPurchaseStatus().toString().equalsIgnoreCase("REJECTED"))) {
			if(canEdit) {
				
				if(dbPurchaseInfo.getPurchaseStatus() == PurchaseStatus.REJECTED && purchaseInfo.getPurchaseStatus() == PurchaseStatus.SUBMITTED) {
					purchaseInfo.setPurchaseStatus(PurchaseStatus.REJECTED_MODIFIED);
				}
				
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
	

	@PutMapping("/update-purchase-status")
	@CheckRoles({"ROLE_ADMIN", "ROLE_PURCHASE_AUTHORIZATION"})
	public ResponseEntity<?> updatePurchaseStatus(@RequestBody Purchase purchase){
		Purchase dbPurchase = purchaseServices.getPurchaseInfoByIdAndPO(purchase.getId(), purchase.getPoNumber());
		if(HelperUtils.userAssignedThisStore(dbPurchase.getStore())){
			if(purchase.getPurchaseStatus().equals(PurchaseStatus.REJECTED)){
				dbPurchase.setRejectedNote(purchase.getRejectedNote());
			}
			return ResponseEntity.ok(purchaseServices.updatePurchaseStatus(dbPurchase, purchase.getPurchaseStatus()));
		}else{
			throw new RequestRejectedException("Unauthorized to update status of purchase order!");
		}

	}
	
	
	@GetMapping("/generate-pdf/{id}/{po}")
	public ResponseEntity<?> getPurchaseReport(@PathVariable Long id, @PathVariable String po, HttpServletRequest req) throws Exception {
	
		Purchase purchase = purchaseServices.getPurchaseInfoByIdAndPO(id, po);

		byte[] report;
		
		if(purchase != null) {
			report = reportServices.generatePODetails(purchase);
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+purchase.getPoNumber()+".pdf").contentType(MediaType.APPLICATION_PDF).body(report);
		}else {
			throw new ResourceNotFoundException("Item not found!");
		}
	}
}
