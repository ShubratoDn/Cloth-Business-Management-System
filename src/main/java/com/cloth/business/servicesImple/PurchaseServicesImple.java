package com.cloth.business.servicesImple;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cloth.business.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cloth.business.configurations.security.CustomUserDetails;
import com.cloth.business.entities.Product;
import com.cloth.business.entities.ProductCategory;
import com.cloth.business.entities.TradeTransaction;
import com.cloth.business.entities.TradeTransactionDetails;
import com.cloth.business.entities.StakeHolder;
import com.cloth.business.entities.Store;
import com.cloth.business.entities.User;
import com.cloth.business.entities.enums.TransactionStatus;
import com.cloth.business.helpers.HelperUtils;
import com.cloth.business.payloads.PageResponse;
import com.cloth.business.repositories.ProductCategoryRepository;
import com.cloth.business.repositories.ProductRepository;
import com.cloth.business.repositories.TransactionRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PurchaseServicesImple implements PurchaseServices {

	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private StoreServices storeServices;
	
	@Autowired
	private StakeHolderService stakeHolderService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductCategoryRepository productCategoryRepository;
	
	@Autowired
	private FileServices fileServices;

	@Autowired
	private StockService stockService;
	
	@Override
	public TradeTransaction createPurchase(TradeTransaction purchase) {
		
		//getting the logged in user
		CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		User loggedInUser = customUserDetails.getLoggedInUser();
		
		Store store = storeServices.getStoreById(purchase.getStore().getId());
		StakeHolder supplier = stakeHolderService.getStakeHolderWithType(purchase.getPartner().getId(), "supplier");
		
		
		
		purchase.setProcessedBy(loggedInUser);
		purchase.setStore(store);
		purchase.setPartner(supplier);
		
		
		List<TradeTransactionDetails> updatedPurchaseDetails = new ArrayList<>();
		Double productPriceTotal = 0.00;
		for(TradeTransactionDetails purchaseDetail : purchase.getTransactionDetails()) {			
			//if the category not found...
			ProductCategory productCategory = productCategoryRepository.findByName(purchaseDetail.getProduct().getCategory().getName());
			if(productCategory == null) {
				Product product = purchaseDetail.getProduct();
				product.setProductCategory(purchaseDetail.getProduct().getCategory().getName());
				
				if(purchaseDetail.getProductImage() != null) {
					product.setProductImage(purchaseDetail.getProductImage());
				}
				
				//creating new product
				Product savedProduct = productService.createProduct(product);
				
				purchaseDetail.setProduct(savedProduct);
				log.info("New product created {}", product.getName());
			}else {
				Product product = purchaseDetail.getProduct();
				product.setCategory(productCategory);
				
				List<Product> byCategoryAndNameAndSize = productRepository.findByCategoryAndNameAndSize(purchaseDetail.getProduct().getCategory(), purchaseDetail.getProduct().getName(), purchaseDetail.getProduct().getSize());
				if(byCategoryAndNameAndSize.size() != 0 && byCategoryAndNameAndSize.get(0) != null) {
					purchaseDetail.setProduct(byCategoryAndNameAndSize.get(0));
					
					
					//upload the new product image if found
					if(purchaseDetail.getProductImage() != null) {
						String uploadProductImage = fileServices.uploadProductImage(purchaseDetail.getProductImage());
						purchaseDetail.setImage(uploadProductImage);
					}else {
						purchaseDetail.setImage(purchaseDetail.getProduct().getImage());
					}
					
					
					log.info("Product already exist {}", product.getName());
				}else {
					product.setProductCategory(product.getCategory().getName());
					if(purchaseDetail.getProductImage() != null) {
						product.setProductImage(purchaseDetail.getProductImage());
					}
					
					//create new product
					Product savedProduct = productService.createProduct(product);								
					purchaseDetail.setProduct(savedProduct);
					log.info("New product created {} , Size: {}", product.getName(), product.getSize());
				}
			}
			
			
			
			Double total =  purchaseDetail.getQuantity() * purchaseDetail.getPrice();
			productPriceTotal = productPriceTotal + (total);
			
			
			purchaseDetail.setTradeTransaction(purchase);			
			updatedPurchaseDetails.add(purchaseDetail);
		}

		Double grandTotal = productPriceTotal;
		grandTotal = purchase.getDiscountAmount() == null ? grandTotal : grandTotal - purchase.getDiscountAmount();
		grandTotal = purchase.getChargeAmount() == null ? grandTotal : grandTotal + purchase.getChargeAmount();

		purchase.setTotalAmount(grandTotal);
		purchase.setTransactionNumber(generatePOnumber(store));
		return transactionRepository.save(purchase);
	}

	
	//update purchase
	@Override
	public TradeTransaction updatePurchase(TradeTransaction purchase, TradeTransaction dbPurchase) {

		if (purchase.getStore().getId() != null && purchase.getStore().getId() > 0) {
			Store store = storeServices.getStoreById(purchase.getStore().getId());
			dbPurchase.setStore(store);
		}

		if (purchase.getPartner().getId() != null && purchase.getPartner().getId() > 0) {
			StakeHolder supplier = stakeHolderService.getStakeHolderWithType(purchase.getPartner().getId(),
					"supplier");
			dbPurchase.setPartner(supplier);
		}

		List<TradeTransactionDetails> updatedPurchaseDetails = new ArrayList<>();
		Double productPriceTotal = 0.00;
		for (TradeTransactionDetails purchaseDetail : purchase.getTransactionDetails()) {
			// if the category not found...
			ProductCategory productCategory = productCategoryRepository
					.findByName(purchaseDetail.getProduct().getCategory().getName());
			if (productCategory == null) {
				Product product = purchaseDetail.getProduct();
				product.setProductCategory(purchaseDetail.getProduct().getCategory().getName());

				if (purchaseDetail.getProductImage() != null) {
					product.setProductImage(purchaseDetail.getProductImage());
				}

				// creating new product
				Product savedProduct = productService.createProduct(product);

				purchaseDetail.setProduct(savedProduct);
				log.info("New product created {}", product.getName());
			} else {
				Product product = purchaseDetail.getProduct();
				product.setCategory(productCategory);

				List<Product> byCategoryAndNameAndSize = productRepository.findByCategoryAndNameAndSize(
						purchaseDetail.getProduct().getCategory(), purchaseDetail.getProduct().getName(),
						purchaseDetail.getProduct().getSize());
				if (byCategoryAndNameAndSize.size() != 0 && byCategoryAndNameAndSize.get(0) != null) {
					purchaseDetail.setProduct(byCategoryAndNameAndSize.get(0));
					// upload the new product image if found
					if (purchaseDetail.getProductImage() != null) {
						String uploadProductImage = fileServices.uploadProductImage(purchaseDetail.getProductImage());
						purchaseDetail.setImage(uploadProductImage);
					} else {
						if(purchaseDetail.getImage() != null){
							purchaseDetail.setImage(purchaseDetail.getImage());
						}else{
							purchaseDetail.setImage(purchaseDetail.getProduct().getImage());
						}
					}

					log.info("Product already exist {}", product.getName());
				} else {
					product.setProductCategory(product.getCategory().getName());
					if (purchaseDetail.getProductImage() != null) {
						product.setProductImage(purchaseDetail.getProductImage());
					}

					// create new product
					Product savedProduct = productService.createProduct(product);
					purchaseDetail.setProduct(savedProduct);
					log.info("New product created {} , Size: {}", product.getName(), product.getSize());
				}
			}

			Double total = purchaseDetail.getQuantity() * purchaseDetail.getPrice();
			productPriceTotal = productPriceTotal + (total);

			purchaseDetail.setTradeTransaction(purchase);
			updatedPurchaseDetails.add(purchaseDetail);
		}

		Double grandTotal = productPriceTotal;
		grandTotal = purchase.getDiscountAmount() == null ? grandTotal : grandTotal - purchase.getDiscountAmount();
		grandTotal = purchase.getChargeAmount() == null ? grandTotal : grandTotal + purchase.getChargeAmount();


		purchase.setTotalAmount(grandTotal);


		dbPurchase.setRemark(purchase.getRemark());
		dbPurchase.setDiscountAmount(purchase.getDiscountAmount() != null ? purchase.getDiscountAmount() : 0);
		dbPurchase.setChargeAmount(purchase.getChargeAmount() != null ? purchase.getChargeAmount() : 0);
		dbPurchase.setChargeRemark(purchase.getChargeRemark());
		dbPurchase.setDiscountRemark(purchase.getDiscountRemark());

		dbPurchase.setTransactionDate(purchase.getTransactionDate());
		dbPurchase.setTransactionStatus(purchase.getTransactionStatus());
		dbPurchase.setTotalAmount(purchase.getTotalAmount());
		dbPurchase.setLastUpdatedBy(purchase.getLastUpdatedBy());
		dbPurchase.setLastUpdatedDate(purchase.getLastUpdatedDate());
		dbPurchase.setTransactionDetails(purchase.getTransactionDetails());

		return transactionRepository.save(dbPurchase);
	}
	
	
	
	
	//generate PO number
//	public String generatePOnumber(Store store) {
//		long countPurchasesByStore = transactionRepository.countPurchasesByStore(store.getId());
//
//		String poNumber = "PO"+store.getId()+"S"+(countPurchasesByStore+1);
//		return poNumber;
//	}

	public String generatePOnumber(Store store) {
		Long storeId = store.getId();
		// Get current date (Year and Month)
		LocalDate now = LocalDate.now();
		String year = now.format(DateTimeFormatter.ofPattern("yy"));
		String month = now.format(DateTimeFormatter.ofPattern("MM"));

		// Calculate the first and last day of the current month
		LocalDate firstDayOfMonth = now.withDayOfMonth(1);
		LocalDate lastDayOfMonth = now.withDayOfMonth(now.lengthOfMonth());

		// Convert LocalDate to Date (for JPA query)
		Date startDate = Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date endDate = Date.from(lastDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());

		// Get count of POs for the current month for the given store
		int currentMonthCount = transactionRepository.countByStoreIdAndDateRange(storeId, startDate, endDate);

		// Generate the serial number for the PO
		String serialNumber = String.format("%04d", currentMonthCount + 1);

		// Build and return the PO number string
		return String.format("PO-ST%02d-%s%s%s", storeId, year, month, serialNumber);
	}
	
	
	
	
	@Override
	public PageResponse searchPurchase(Long storeId, Long supplierId, String poNumber, TransactionStatus purchaseStatus, Date fromDate, Date toDate, int page, int size, String sortBy, String sortDirection) {

		Sort sort = null;
		if (sortDirection.equalsIgnoreCase("asc")) {
			sort = Sort.by(sortBy).ascending();
		} else {
			sort = Sort.by(sortBy).descending();
		}
		
		Page<TradeTransaction> pageInfo;
		
		Pageable pageable = PageRequest.of(page, size, sort);
		pageInfo = transactionRepository.searchPurchases(storeId, supplierId, poNumber, purchaseStatus, fromDate, toDate, pageable);
		return HelperUtils.pageToPageResponse(pageInfo);
	}
	
	
	@Override
	public TradeTransaction getPurchaseInfoByIdAndPO(Long id, String po) {
		TradeTransaction purchase = transactionRepository.findByIdAndTransactionNumber(id, po);
		return purchase;
	}


	@Override
	public TradeTransaction updatePurchaseStatus(TradeTransaction purchase, TransactionStatus status) {
		if(status.equals(TransactionStatus.APPROVED) ){
			purchase.setTransactionStatus(TransactionStatus.APPROVED);
			purchase.setApprovedBy(HelperUtils.getLoggedinUser());
			purchase.setApprovedDate(new Date());

			purchase.setRejectedBy(null);
			purchase.setRejectedDate(null);

			//UPDATING STOCK
			stockService.updateStock(purchase);
		}else if(status.equals(TransactionStatus.REJECTED)){
			purchase.setRejectedBy(HelperUtils.getLoggedinUser());
			purchase.setRejectedDate(new Date());
 			purchase.setTransactionStatus(TransactionStatus.REJECTED);

			purchase.setApprovedBy(null);
			purchase.setApprovedDate(null);

		} else if (status.equals(TransactionStatus.CLOSED)) {
			if(purchase.getTransactionStatus().equals(TransactionStatus.APPROVED) || purchase.getTransactionStatus().equals(TransactionStatus.REJECTED)) {
				purchase.setTransactionStatus(TransactionStatus.CLOSED);
			}
		}
		return transactionRepository.save(purchase);
	}
}
