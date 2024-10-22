package com.cloth.business.configurations.constants;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Constants {
	public static final String RESOURCE_DIRECTORY ="src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator ;
	
	
	public static final long USER_IMAGE_MAX_SIZE = 5 * 1024 * 1024; // 5MB
    public static final List<String> USER_IMAGE_ALLOWED_EXTENSIONS = Arrays.asList("png", "jpg", "jpeg");
    public static final String USER_IMAGE_UPLOAD_DIRECTORY = "images/userimages";
	
    
    
	public static final long STORE_IMAGE_MAX_SIZE = 10 * 1024 * 1024; // 5MB
    public static final List<String> STORE_IMAGE_ALLOWED_EXTENSIONS = Arrays.asList("png", "jpg", "jpeg");
    public static final String STORE_IMAGE_UPLOAD_DIRECTORY = "images/storeimages";
    
	
    public static final long PRODUCT_IMAGE_MAX_SIZE = 10 * 1024 * 1024; // 5MB
    public static final List<String> PRODUCT_IMAGE_ALLOWED_EXTENSIONS = Arrays.asList("png", "jpg", "jpeg");
    public static final String PRODUCT_IMAGE_UPLOAD_DIRECTORY = "images/productimages";
    
    
    
    public static final String STAKEHOLDER_IMAGE_UPLOAD_DIRECTORY = "images/stakeholderimages";
    
    public static final String REPORT_TYPE_PDF="PDF";
    public static final String REPORT_TYPE_HTML="HTML";
    public static final String REPORT_TYPE_EXCEL="xl";
    
}
