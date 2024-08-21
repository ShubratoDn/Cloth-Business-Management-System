package com.cloth.business.services;

import java.util.List;

import com.cloth.business.entities.Store;
import com.cloth.business.payloads.PageResponse;


public interface StoreServices {
	public Store addStore(Store store);
	public Store updateStore(Store store);
	
	public Store getStoreById(Long id);
	public List<Store> getAllStoreInfo();
	
	PageResponse searchStore(String query, int page, int size, String sortBy, String sortDirection);
    
}
