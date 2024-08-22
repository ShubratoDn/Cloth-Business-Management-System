package com.cloth.business.servicesImple;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.cloth.business.entities.Store;
import com.cloth.business.exceptions.ResourceNotFoundException;
import com.cloth.business.helpers.HelperUtils;
import com.cloth.business.payloads.PageResponse;
import com.cloth.business.repositories.StoreRepository;
import com.cloth.business.services.StoreServices;

@Service
public class StoreServiceImple implements StoreServices {

    @Autowired
    private StoreRepository storeRepository;
    

    @Override
    public Store addStore(Store store) {
        return storeRepository.save(store);
    }

    @Override
    public Store updateStore(Store store) {
        Store existingStore = storeRepository.findById(store.getId()).orElseThrow(()->new ResourceNotFoundException("Store", "ID"));
        existingStore = storeRepository.save(store);
        return existingStore;
    }

    @Override
    public Store getStoreById(Long id) {
        Optional<Store> store = storeRepository.findById(id);
        return store.orElseThrow(() -> new ResourceNotFoundException("Store", "ID"));
    }

    @Override
    public List<Store> getAllStoreInfo() {
        return storeRepository.findAll();
    }
    
    
    @Override
    public PageResponse searchStore(String query, int page, int size, String sortBy, String sortDirection) {
    	
    	Sort sort = null;
		if (sortDirection.equalsIgnoreCase("asc")) {
			sort = Sort.by(sortBy).ascending();
		} else {
			sort = Sort.by(sortBy).descending();
		}
		
		Page<Store> pageInfo;
		
		try {
			Pageable pageable = PageRequest.of(page, size, sort);
			pageInfo = storeRepository.searchStores(query, pageable);
		} catch (Exception e) {
			Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
			pageInfo = storeRepository.searchStores(query, pageable);
		}

		
		PageResponse pageToPageResponse = HelperUtils.pageToPageResponse(pageInfo);
		
    	return pageToPageResponse;
    }
    
}
