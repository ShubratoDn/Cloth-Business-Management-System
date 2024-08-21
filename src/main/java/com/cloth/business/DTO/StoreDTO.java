package com.cloth.business.DTO;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class StoreDTO {

	private Long id;
	
	@NotBlank(message = "Store Name is required")
	private String storeName;
	
	@NotBlank(message = "Store address is required")
	private String address;
	
	@NotBlank(message = "Store code is required")
	private String storeCode;
	
	private String remark;
	
	private Boolean isActive;
	
	private String image;
	
	private MultipartFile storeImage;
	
	
	@JsonIgnore
    public MultipartFile getStoreImage() {
        return this.storeImage;
    }

    @JsonProperty
    public void setStoreImage(MultipartFile storeImage) {
        this.storeImage = storeImage;
    }
	
}
