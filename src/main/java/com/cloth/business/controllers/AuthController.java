package com.cloth.business.controllers;

import com.cloth.business.DTO.UserDTO;
import com.cloth.business.configurations.jwt.JwtTokenUtil;
import com.cloth.business.configurations.security.CustomUserDetailsServiceImpl;
import com.cloth.business.payloads.ErrorResponse;
import com.cloth.business.payloads.LoginRequest;
import com.cloth.business.payloads.LoginResponse;
import com.cloth.business.services.UserServices;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	private UserServices userServices;

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
		userDTO.setCreatedAt(new Date());
		userDTO.setUpdatedAt(null);

		// Save the user
        UserDTO savedUser = userServices.addUser(userDTO);		
		log.info("New user registered successfully: Id ->{}; Name -> {}", savedUser.getId(), savedUser.getName());
		return ResponseEntity.ok(savedUser);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		String username = loginRequest.getUsername();
		String password = loginRequest.getPassword();

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		}catch (BadCredentialsException e) {
	        // Handle bad credentials
	        ErrorResponse errorResponse = new ErrorResponse(
	                LocalDateTime.now(), 
	                HttpStatus.UNAUTHORIZED.value(),
	                "Bad Credentials", 
	                "Incorrect password."
	        );
	        log.error("Login failed for user '{}': Incorrect username or password.", username);
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
	    } catch (LockedException e) {
	        // Handle locked account
	        ErrorResponse errorResponse = new ErrorResponse(
	                LocalDateTime.now(), 
	                HttpStatus.LOCKED.value(),
	                "User Account Locked", 
	                "Your account is locked. Please contact support."
	        );
	        log.error("Login failed for user '{}': Account is locked.", username);
	        return ResponseEntity.status(HttpStatus.LOCKED).body(errorResponse);
	    } catch (DisabledException e) {
	        // Handle disabled account
	        ErrorResponse errorResponse = new ErrorResponse(
	                LocalDateTime.now(), 
	                HttpStatus.FORBIDDEN.value(),
	                "User Account Disabled", 
	                "Your account is disabled. Please contact support."
	        );
	        log.error("Login failed for user '{}': User account is disabled.", username);
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
	    } catch (AccountExpiredException e) {
	        // Handle expired account
	        ErrorResponse errorResponse = new ErrorResponse(
	                LocalDateTime.now(), 
	                HttpStatus.UNAUTHORIZED.value(),
	                "User Account Expired", 
	                "Your account has expired. Please contact support."
	        );
	        log.error("Login failed for user '{}': Account expired.", username);
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
	    }  catch (InternalAuthenticationServiceException e) {
	        // Handle user not found
	        ErrorResponse errorResponse = new ErrorResponse(
	                LocalDateTime.now(), 
	                HttpStatus.BAD_REQUEST.value(),
	                "User Not Found", 
	                "User not found."
	        );
	        log.error("Login failed for user '{}': User not found.", username);
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	    }

		UserDetails userDetails = customUserDetailsServiceImpl.loadUserByUsername(username);
		String token = jwtTokenUtil.generateToken(userDetails);

		UserDTO user = userServices.findByPhoneOrEmail(username, username);
		
		
		user.setLogoutRequired(false);		
		userServices.updateUser(user);
		
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setToken(token);
		loginResponse.setUser(user);

		log.info("User '{}' has successfully logged in.", username);
		return ResponseEntity.ok(loginResponse);
	}

}
