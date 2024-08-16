package com.cloth.business.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FileUploadingException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String keyName;
    private String errorMessage;

    public FileUploadingException(String keyName, String errorMessage) {
        super(String.format("%s: %s", keyName, errorMessage));
        this.keyName = keyName;
        this.errorMessage = errorMessage;
    }

    public String getKeyName() {
        return keyName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
