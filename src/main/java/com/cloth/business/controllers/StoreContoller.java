package com.cloth.business.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloth.business.DTO.StoreDTO;
import com.cloth.business.entities.Store;
import com.cloth.business.services.StoreServices;

@RestController
@RequestMapping("/api/v1/stores")
public class StoreContoller {

	@Autowired
	private StoreServices storeService;

	@Autowired
	private ModelMapper modelMapper;
	
	@PostMapping("/")
	public Store addStore(@RequestBody StoreDTO storeDto) {
		Store store = modelMapper.map(storeDto, Store.class);
		return storeService.addStore(store);
	}

	@PutMapping("/{id}")
	public Store updateStore(@PathVariable Long id, @RequestBody StoreDTO storeDto) {
		Store store = modelMapper.map(storeDto, Store.class);
		return storeService.updateStore(store);
	}

	@GetMapping("/{id}")
	public Store getStoreById(@PathVariable Long id) {
		return storeService.getStoreById(id);
	}

	@GetMapping
	public List<Store> getAllStoreInfo() {
		return storeService.getAllStoreInfo();
	}

}
