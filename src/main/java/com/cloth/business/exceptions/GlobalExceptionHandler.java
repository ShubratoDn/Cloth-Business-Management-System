package com.cloth.business.exceptions;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cloth.business.payloads.ErrorResponse;

import org.springframework.beans.InvalidPropertyException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;


import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		ex.printStackTrace();		
        ErrorResponse errorResponse = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
	
	 /**
     * Handles the exception when a method argument type mismatch occurs.
     * Returns a response with a "Bad Request" error message indicating the invalid parameter type.
     *
     * @param ex The exception indicating the method argument type mismatch.
     * @return A ResponseEntity with an ErrorResponse and HTTP status code.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.error("Bad Request - Invalid parameter type: {}", ex.getName());
      
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Invalid parameter type for " + ex.getName()
            );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
    
    

	/**
     * Handles the HttpMessageNotReadableException and returns an error response.
     *
     * @param ex The HttpMessageNotReadableException to handle.
     * @return A ResponseEntity containing the error response.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
//        log.error("Error occurred while processing the request.", ex);

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(), // Replace with actual timestamp logic
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Request body is not readable or is in an invalid format."
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
    
    
    /**
     * Handles the HttpMediaTypeNotSupportedException and returns an error response.
     *
     * @param ex The HttpMediaTypeNotSupportedException to handle.
     * @return A ResponseEntity containing the error response.
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        log.error("Error occurred while processing the request.", ex);

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(), // Replace with actual timestamp logic
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                "Unsupported Media Type",
                "Unsupported media type in the request."
        );

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(errorResponse);
    }

    
    
    
    /**
     * Handles the HttpRequestMethodNotSupportedException and returns an error response.
     *
     * @param ex The HttpRequestMethodNotSupportedException to handle.
     * @return A ResponseEntity containing the error response.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.error("Error occurred while processing the request.", ex);

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(), // Replace with actual timestamp logic
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                "Method Not Allowed",
                ""+ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(), 
                HttpStatus.BAD_REQUEST.value(),
                "File is too large.",
                ""+ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
    
    
  //validation error handler
  	@ExceptionHandler(MethodArgumentNotValidException.class)
  	public ResponseEntity<Map<String, String>> methodArgumentNotValidExceptionHandler (MethodArgumentNotValidException ex){
  		
  		Map<String, String> response = new HashMap<>(); 
//  		response.put("error", "Invalid Input");
  		
  		List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
  		
  		for(ObjectError error : allErrors) {
  			String fieldName = ((FieldError) error).getField();
  			String message = error.getDefaultMessage();			
  			response.put(fieldName, message);
  		}		
  		return new ResponseEntity<Map<String,String>>(response, HttpStatus.BAD_REQUEST);		
  	}
  	

  	
  	/**
  	 * Handles the UsernameNotFoundException and returns an error response.
  	 *
  	 * @param ex The UsernameNotFoundException to handle.
  	 * @return A ResponseEntity containing the error response.
  	 */
  	@ExceptionHandler(UsernameNotFoundException.class)
  	public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
  	    log.error("Error occurred while processing the request.", ex);

  	    ErrorResponse errorResponse = new ErrorResponse(
  	            LocalDateTime.now(), // Replace with actual timestamp logic
  	            HttpStatus.NOT_FOUND.value(),
  	            "User Not Found",
  	            "The requested user was not found."
  	    );

  	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  	}
  	
  	
  	
  	/**
  	 * Exception handler for handling SignatureException.
  	 *
  	 * @param ex The SignatureException to handle.
  	 * @return A ResponseEntity containing the error response for unauthorized requests.
  	 */
  	@ExceptionHandler(SignatureException.class)
  	public ResponseEntity<ErrorResponse> handleSignatureException(SignatureException ex) {
  	    ErrorResponse errorResponse = new ErrorResponse(
  	            LocalDateTime.now(),
  	            HttpStatus.UNAUTHORIZED.value(),
  	            "Unauthorized",
  	            "Signature verification failed: " + ex.getMessage()
  	    );

  	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
  	}


  	/**
  	 * Exception handler for handling AccessDeniedException.
  	 * This method is invoked when access to a resource is denied due to insufficient permissions.
  	 *
  	 * @param ex The AccessDeniedException that was thrown.
  	 * @return A ResponseEntity containing an ErrorResponse with details of the access denial.
  	 */
  	@ExceptionHandler(AccessDeniedException.class)
  	public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
  	    ErrorResponse errorResponse = new ErrorResponse(
  	        LocalDateTime.now(),
  	        HttpStatus.FORBIDDEN.value(),
  	        "Access Denied",
  	        "You do not have permission to access this resource."
  	    );
  	    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
  	}
	
	  	


	
	/**
	 * Handles the exception when a request is rejected.
	 * Returns a response with a "Forbidden" error message indicating the rejected request.
	 *
	 * @param ex The exception indicating the rejected request.
	 * @return A ResponseEntity with an ErrorResponse and HTTP status code.
	 */
	@ExceptionHandler(RequestRejectedException.class)
	public ResponseEntity<ErrorResponse> handleRequestRejectedException(RequestRejectedException ex) {
	    log.error("Forbidden - Request rejected: {}", ex.getMessage());

	    ErrorResponse errorResponse = new ErrorResponse(
	        LocalDateTime.now(),
	        HttpStatus.FORBIDDEN.value(),
	        "Forbidden",
	        "Request rejected: " + ex.getMessage()
	    );

	    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
	}

	
	
	
	
	
	@ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
	
		
	
	/**
     * Handles the exception when a resource is not found.
     * Returns a response with a "Not Found" error message indicating the resource was not found.
     *
     * @param ex The exception indicating the resource was not found.
     * @return A ResponseEntity with an ErrorResponse and HTTP status code.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("Not Found - Resource not found: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            "Not Found",
            "Resource not found: " + ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


	@ExceptionHandler(ResourceAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
		log.error("Resource available - Resource found: {}", ex.getMessage());

		ErrorResponse errorResponse = new ErrorResponse(
				LocalDateTime.now(),
				HttpStatus.BAD_REQUEST.value(),
				"Resource Already Exists",
				"" + ex.getMessage()
		);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}
	
    
    /**
     * Handles the exception when a path variable is missing.
     * Returns a response with a "Bad Request" error message indicating the missing path variable.
     *
     * @param ex The exception indicating the missing path variable.
     * @return A ResponseEntity with an ErrorResponse and HTTP status code.
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ErrorResponse> handleMissingPathVariable(MissingPathVariableException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Missing Path Variable",
            "The path variable '" + ex.getVariableName() + "' is missing."
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
	
	
    
    /**
     * Handles the exception when a NullPointerException occurs.
     * Returns a response with a "Internal Server Error" error message indicating the null pointer exception.
     *
     * @param ex The exception indicating the NullPointerException.
     * @return A ResponseEntity with an ErrorResponse and HTTP status code.
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Bad Request",
            "A NullPointerException occurred: " + ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
    
    


    @ExceptionHandler(InvalidDateFormatException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDateFormatException(InvalidDateFormatException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Date Format",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
	


    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(
            NoHandlerFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Resource Not Found",
                "The requested resource was not found."
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    
    
    /**
     * Handles SQLIntegrityConstraintViolationException.
     *
     * @param ex The SQLIntegrityConstraintViolationException.
     * @return A ResponseEntity with an ErrorResponse and HTTP status code.
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex) {
        log.error("SQL Integrity Constraint Violation Exception: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.CONFLICT.value(),
            "SQL Integrity Constraint Violation",
            "A database constraint violation occurred: Before deleting parent, please delete child data"
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    
    
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("SQL Integrity Constraint Violation Exception: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.CONFLICT.value(),
            "SQL Integrity Constraint Violation",
            ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
    
    
    @ExceptionHandler(FileUploadingException.class)
    public ResponseEntity<Map<String, String>> handleFileUploadingException(FileUploadingException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(ex.getKeyName(), ex.getErrorMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    
    /**
     * Handles exceptions related to invalid properties in the request body.
     * This method is triggered when an {@link InvalidPropertyException} occurs,
     * indicating that a property in the request body is either missing or invalid.
     *
     * @param ex the exception indicating that a property is invalid
     * @return a {@link ResponseEntity} containing an {@link ErrorResponse} with details about the error
     */
    @ExceptionHandler(InvalidPropertyException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPropertyException(InvalidPropertyException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Property",
                "A property in the request body is invalid: " + ex.getPropertyName()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}
