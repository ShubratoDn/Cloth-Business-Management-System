package com.cloth.business.servicesImple;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloth.business.configurations.constants.Constants;
import com.cloth.business.exceptions.FileUploadingException;
import com.cloth.business.services.FileServices;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileServicesImple implements FileServices {

	
	/**
     * Uploads a user's image after validating its size and extension.
     *
     * @param imageFile the image file to be uploaded
     * @return the file path of the uploaded image
     * @throws FileUploadingException if the file size exceeds the maximum limit or the extension is invalid
     */
	@Override
	public String uploadUserImage(MultipartFile imageFile) {
		// Validate file size
		if (imageFile.getSize() > Constants.USER_IMAGE_MAX_SIZE) {
			throw new FileUploadingException("userImage", "File size exceeds the maximum limit of "+Constants.USER_IMAGE_MAX_SIZE+"MB");
		}

		// Validate file extension
		String fileName = imageFile.getOriginalFilename();
		if (fileName != null && !isExtensionAllowed(fileName, Constants.USER_IMAGE_ALLOWED_EXTENSIONS)) {
			throw new FileUploadingException("userImage",
					"Invalid file extension. Allowed extensions are jpg, jpeg, png");
		}

		
		String uploadDirectory = this.uploadFile(imageFile, generateRandomText(imageFile), "userImage", Constants.USER_IMAGE_UPLOAD_DIRECTORY);
		return uploadDirectory;
		
	}

	
	/**
     * Checks if the file extension is allowed.
     *
     * @param fileName   the name of the file
     * @param extensions the list of allowed extensions
     * @return true if the file extension is allowed, false otherwise
     */
	private boolean isExtensionAllowed(String fileName, List<String> extensions) {
		String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
		return extensions.contains(fileExtension);
	}

	
	/**
     * Generates a random hexadecimal string and appends the file extension.
     *
     * @param file the MultipartFile object containing the original file name
     * @return the generated random file name with the original extension
     */
	//Generate Random Text	
	public String generateRandomText(MultipartFile file) {
		// Random text generate
		SecureRandom random = new SecureRandom();
		byte[] randomBytes = new byte[20];
		random.nextBytes(randomBytes);

		StringBuilder sb = new StringBuilder();
		for (byte b : randomBytes) {
			sb.append(String.format("%02x", b));
		}

		String randomHexCode = sb.toString();
		String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		String fileName = randomHexCode + "_" + fileExtension;
		return fileName;
	}
	
	
	/**
     * Uploads a file to the specified directory.
     *
     * @param file            the file to be uploaded
     * @param fileName        the name of the file to be saved as
     * @param resourceName    the resource name used for logging and exception purposes
     * @param upload_directory the directory where the file will be saved
     * @return the name of the uploaded file
     * @throws FileUploadingException if the file upload fails
     */
	public String uploadFile(MultipartFile file, String fileName, String resourceName, String upload_directory ) {
		try {
			File f = new File(upload_directory);
			f.mkdirs();

			InputStream inputStream = file.getInputStream();
			byte[] data = new byte[inputStream.available()];
			inputStream.read(data);

			FileOutputStream fos = new FileOutputStream(upload_directory + File.separator + fileName);
			fos.write(data);

			fos.flush();
			fos.close();

			return fileName;

		} catch (Exception e) {
			log.error("File Upload fail Because : ");
			log.error(e.toString());
			throw new FileUploadingException(resourceName, "Failed to upload "+resourceName+". Please contact with administrator.");
		}
	}

}
