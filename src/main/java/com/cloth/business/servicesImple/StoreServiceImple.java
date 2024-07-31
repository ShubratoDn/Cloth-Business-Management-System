package com.cloth.business.servicesImple;


import com.cloth.business.entities.Store;
import com.cloth.business.exceptions.ResourceNotFoundException;
import com.cloth.business.repositories.StoreRepository;
import com.cloth.business.services.StoreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StoreServiceImple implements StoreService {

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
}
