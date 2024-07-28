package com.cloth.business.payloads;


import com.cloth.business.DTO.UserDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {

	private String token;
	private UserDTO user;
	
}
