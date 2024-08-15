package com.cloth.business.controllers;

import com.cloth.business.DTO.UserDTO;
import com.cloth.business.configurations.jwt.JwtTokenUtil;
import com.cloth.business.configurations.security.CustomUserDetailsServiceImpl;
import com.cloth.business.payloads.ErrorResponse;
import com.cloth.business.payloads.LoginRequest;
import com.cloth.business.payloads.LoginResponse;
import com.cloth.business.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api/v1/auth")
@RestController
@Slf4j
public class AuthController {

	@Autowired
	private UserService userServices;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsServiceImpl customUserDetailsServiceImpl;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @ModelAttribute UserDTO userDTO) {
		Map<String, String> response = new HashMap<>();

		System.out.println(userDTO.getName());
		System.out.println(userDTO.getUserImage().getContentType());
		System.out.println(userDTO.getUserImage().getOriginalFilename());
		System.out.println(userDTO.getUserImage().getSize());

		
		if (userServices.findByPhone(userDTO.getPhone()) != null) {
			response.put("phone", "Phone number already exists.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}

		if (userDTO.getEmail() != null || !userDTO.getEmail().isBlank()) {
			if (userServices.findByEmail(userDTO.getEmail()) != null) {
				response.put("email", "Email already exists.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}
		}

		userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		userDTO.setIsLocked(false);
		userDTO.setCreatedAt(new Date());
		userDTO.setUpdatedAt(null);

//        return ResponseEntity.ok(userDTO);
		// Save the user
//        UserDTO savedUser = userServices.addUser(userDTO);		
//		log.info("New user registered successfully: Id ->{}; Name -> {}", savedUser.getId(), savedUser.getName());
//		return ResponseEntity.ok(savedUser);
		return ResponseEntity.ok(userDTO);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		String username = loginRequest.getUsername();
		String password = loginRequest.getPassword();

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
					"User Account Disabled", "User account is disabled.");
			log.error("Login failed for user '{}': User account is disabled.", username);
			return ResponseEntity.badRequest().body(errorResponse);
		} catch (BadCredentialsException e) {
			ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value(),
					"Bad Credentials",
//                    "Incorrect username or password."
					"Incorrect password.");
			log.error("Login failed for user '{}': Incorrect username or password.", username);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		} catch (InternalAuthenticationServiceException e) {
			ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
					"User Not Found", "User not found.");
			log.error("Login failed for user '{}': User not found.", username);
			return ResponseEntity.badRequest().body(errorResponse);
		}

		UserDetails userDetails = customUserDetailsServiceImpl.loadUserByUsername(username);
		String token = jwtTokenUtil.generateToken(userDetails);

		UserDTO user = userServices.findByPhoneOrEmail(username, username);

		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setToken(token);
		loginResponse.setUser(user);

		log.info("User '{}' has successfully logged in.", username);
		return ResponseEntity.ok(loginResponse);
	}

}
