package com.cloth.business.controllers;

import com.cloth.business.DTO.UserDTO;
import com.cloth.business.configurations.annotations.CheckRoles;
import com.cloth.business.configurations.security.CustomUserDetails;
import com.cloth.business.entities.User;
import com.cloth.business.entities.UserRole;
import com.cloth.business.payloads.ErrorResponse;
import com.cloth.business.payloads.PageResponse;
import com.cloth.business.services.UserServices;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("api/v1/users/")
@Slf4j
public class UserController {

	@Autowired
	private UserServices userService;

	@GetMapping("/me")
	public ResponseEntity<?> myInfo() {
		CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		User loggedInUser = customUserDetails.getLoggedInUser();
		return ResponseEntity.ok(loggedInUser);
	}

	@GetMapping("/all")
	@CheckRoles({"ROLE_ADMIN", "ROLE_USER_GET"})
	public ResponseEntity<?> getAllUser() {
		return ResponseEntity.ok(userService.getAllUsers());
	}

	@GetMapping("/search")
	@CheckRoles({"ROLE_ADMIN", "ROLE_USER_GET"})
	public ResponseEntity<?> search(
			@RequestParam(value = "page", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "size", defaultValue = "5", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
			@RequestParam(value = "sortDirection", defaultValue = "desc", required = false) String sortDirection,
			@RequestParam(value = "query", defaultValue = "", required = false) String query
			) {
		
		return ResponseEntity.ok(userService.searchUser(query, pageNumber, pageSize, sortBy, sortDirection));
	}

	@GetMapping("/{userId}")
	@CheckRoles({"ROLE_ADMIN", "ROLE_USER_GET"})
	public ResponseEntity<?> getUserByiD(@PathVariable Long userId) {
		return ResponseEntity.ok(userService.findById(userId));
	}

	@PutMapping("/{userId}")
	@CheckRoles({"ROLE_ADMIN", "ROLE_USER_UPDATE"})
	public ResponseEntity<?> updateUser(@Valid @RequestBody UserDTO userDTO, @PathVariable Long userId) {

		UserDTO dbUser = userService.findById(userId);

		if (!userDTO.getPhone().equals(dbUser.getPhone())) {
			if (userService.findByPhone(userDTO.getPhone()) != null) {
				ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
						"Bad Request", "Phone number already exists. Try another phone number");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
			}
		}

		if (userDTO.getEmail() != null || !userDTO.getEmail().isBlank()) {
			if (!userDTO.getEmail().equals(dbUser.getEmail())) {
				if (userService.findByEmail(userDTO.getEmail()) != null) {
					ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
							"Bad Request", "Email already exists.");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
				}
			}
		}

		dbUser.setUpdatedAt(new Date());
		dbUser.setName(userDTO.getName());
		dbUser.setDesignation(userDTO.getDesignation());
		dbUser.setPhone(userDTO.getPhone());
		dbUser.setEmail(userDTO.getEmail());
		dbUser.setAddress(userDTO.getAddress());
		dbUser.setRemark(userDTO.getRemark());
		dbUser.setRoles(userDTO.getRoles());
		dbUser.setAssignedStore(userDTO.getAssignedStore());

		// Save the user
		UserDTO savedUser = userService.updateUser(dbUser);

		log.info("New user registered successfully: Id ->{}; Name -> {}", savedUser.getId(), savedUser.getName());

		return ResponseEntity.ok(savedUser);
	}
	
	
	
	
	
	@PutMapping("/{userId}/roles")
	@CheckRoles({"ROLE_ADMIN", "ROLE_ROLE_ASSIGN"})
	public ResponseEntity<?> updateUserRoles(@RequestBody UserDTO userDTO, @PathVariable Long userId) {
		userDTO.setId(userId);
		UserDTO updateUserRoles = userService.updateUserRoles(userDTO);
		return ResponseEntity.ok(updateUserRoles);
	}

	
	
	@PutMapping("/{userId}/stores/{storeId}/assign")
	@CheckRoles({"ROLE_ADMIN", "ROLE_STORE_ASSIGN"})
	public ResponseEntity<?> updateUserStore(@PathVariable Long userId, @PathVariable Long storeId ) {
		userService.updateUserAssignedStore(userId, storeId);
		return ResponseEntity.ok("OK ok");
	}


}
