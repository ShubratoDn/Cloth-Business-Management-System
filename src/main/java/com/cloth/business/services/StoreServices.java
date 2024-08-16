package com.cloth.business.services;

import java.util.List;

import com.cloth.business.entities.Store;


public interface StoreServices {
	public Store addStore(Store store);
	public Store updateStore(Store store);
	
	public Store getStoreById(Long id);
	public List<Store> getAllStoreInfo();
}
