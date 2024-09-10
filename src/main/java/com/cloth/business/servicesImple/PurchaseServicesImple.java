package com.cloth.business.servicesImple;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.cloth.business.entities.Purchase;
import com.cloth.business.entities.PurchaseDetails;
import com.cloth.business.entities.StakeHolder;
import com.cloth.business.entities.Store;
import com.cloth.business.entities.User;
import com.cloth.business.helpers.HelperUtils;
import com.cloth.business.payloads.PageResponse;
import com.cloth.business.repositories.ProductCategoryRepository;
import com.cloth.business.repositories.ProductRepository;
import com.cloth.business.repositories.PurchaseRepository;
import com.cloth.business.services.FileServices;
import com.cloth.business.services.ProductService;
import com.cloth.business.services.PurchaseServices;
import com.cloth.business.services.StakeHolderService;
import com.cloth.business.services.StoreServices;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PurchaseServicesImple implements PurchaseServices {

	@Autowired
	private PurchaseRepository purchaseRepository;
	
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
	
	@Override
	public Purchase createPurchase(Purchase purchase) {
		
		//getting the logged in user
		CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		User loggedInUser = customUserDetails.getLoggedInUser();
		
		Store store = storeServices.getStoreById(purchase.getStore().getId());
		StakeHolder supplier = stakeHolderService.getStakeHolderWithType(purchase.getSupplier().getId(), "supplier");
		
		
		
		purchase.setAddedBy(loggedInUser);
		purchase.setStore(store);
		purchase.setSupplier(supplier);
		
		
		List<PurchaseDetails> updatedPurchaseDetails = new ArrayList<>();
		Double grandTotal = 0.00;
		for(PurchaseDetails purchaseDetail : purchase.getPurchaseDetails()) {			
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
			grandTotal = grandTotal + (total);
			
			
			purchaseDetail.setPurchase(purchase);			
			updatedPurchaseDetails.add(purchaseDetail);
		}

		purchase.setTotalAmount(grandTotal);
		purchase.setPoNumber(generatePOnumber(store));
		return purchaseRepository.save(purchase);
	}

	
	//generate PO number
	public String generatePOnumber(Store store) {
		long countPurchasesByStore = purchaseRepository.countPurchasesByStore(store.getId());
		
		String poNumber = "PO"+store.getId()+"S"+(countPurchasesByStore+1);
		return poNumber;
	}
	
	
	
	
	
	
	@Override
	public PageResponse searchPurchase(Long storeId, Long supplierId, String poNumber, Date fromDate, Date toDate, int page, int size, String sortBy, String sortDirection) {

		Sort sort = null;
		if (sortDirection.equalsIgnoreCase("asc")) {
			sort = Sort.by(sortBy).ascending();
		} else {
			sort = Sort.by(sortBy).descending();
		}
		
		Page<Purchase> pageInfo;
		
		Pageable pageable = PageRequest.of(page, size, sort);
		pageInfo = purchaseRepository.searchPurchases(storeId, supplierId, poNumber, fromDate, toDate, pageable);
		return HelperUtils.pageToPageResponse(pageInfo);
	}
	
	
		
}
