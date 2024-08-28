package com.cloth.business.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileServices {

	public String uploadUserImage(MultipartFile imageFile);
	
	public String uploadStoreImage(MultipartFile imageFile);
	
	public String uploadProductImage(MultipartFile imageFile);
	
}
