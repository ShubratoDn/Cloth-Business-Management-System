package com.cloth.business.configurations.constants;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Constants {
	public static final long USER_IMAGE_MAX_SIZE = 5 * 1024 * 1024; // 5MB
    public static final List<String> USER_IMAGE_ALLOWED_EXTENSIONS = Arrays.asList("png", "jpg", "jpeg");
    public static final String USER_IMAGE_UPLOAD_DIRECTORY = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "images" + File.separator + "userimages";
	
}
