package com.cloth.business.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloth.business.DTO.StoreDTO;
import com.cloth.business.entities.Store;
import com.cloth.business.exceptions.ResourceAlreadyExistsException;
import com.cloth.business.repositories.StoreRepository;
import com.cloth.business.services.FileServices;
import com.cloth.business.services.StoreServices;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/stores")
public class StoreContoller {

	@Autowired
	private StoreServices storeService;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private FileServices fileServices;
	
	@Autowired
	private StoreRepository storeRepository;
	
	@PostMapping("/")
	public Store addStore(@Valid @ModelAttribute StoreDTO storeDto) {
		List<Store> byStoreCodeOrStoreName = storeRepository.findByStoreCodeOrStoreName(storeDto.getStoreCode(), storeDto.getStoreName());
		if( byStoreCodeOrStoreName!= null && byStoreCodeOrStoreName.size()>0) {
			throw new ResourceAlreadyExistsException("Store code or Store name already exist");
		}
		
		if(storeDto.getStoreImage() != null) {			
			String uploadUserImage = fileServices.uploadStoreImage(storeDto.getStoreImage());	
			storeDto.setImage(uploadUserImage);
		}
		
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
	
	
	@GetMapping("/search")
	public ResponseEntity<?> getAllStoreInfo(
			@RequestParam(value = "page", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "size", defaultValue = "5", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
			@RequestParam(value = "sortDirection", defaultValue = "desc", required = false) String sortDirection,
			@RequestParam(value = "query", defaultValue = "", required = false) String query 
			) {
		return ResponseEntity.ok(storeService.searchStore(query, pageNumber, pageSize, sortBy, sortDirection));
	}

}
