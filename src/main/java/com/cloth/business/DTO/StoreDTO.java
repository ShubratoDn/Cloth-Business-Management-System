package com.cloth.business.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class StoreDTO {

	private Long id;
	
	@NotBlank(message = "Store address is required")
	private String address;
	@NotBlank(message = "Store code is required")
	private String storeCode;
	private String remark;

	
}
