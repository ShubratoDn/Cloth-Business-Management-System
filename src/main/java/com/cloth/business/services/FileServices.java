package com.cloth.business.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileServices {

	public String uploadUserImage(MultipartFile imageFile);
	
}
