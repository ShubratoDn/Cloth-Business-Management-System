package com.cloth.business.controllers;

import com.cloth.business.configurations.annotations.CheckRoles;
import com.cloth.business.entities.UserRole;
import com.cloth.business.services.UserRoleServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/roles")
public class UserRoleController {

    @Autowired
    private UserRoleServices userRoleService;

    @PostMapping("/")
    @CheckRoles({"ROLE_ADMIN", "ROLE_ROLE_CREATE"})
    public ResponseEntity<?> addRole(@Valid @RequestBody UserRole userRole){
    	System.out.println(userRole);
        UserRole role = userRoleService.addRole(userRole);
        System.out.println(role);
        return ResponseEntity.ok(role);
    }

    @GetMapping("")
    @CheckRoles({"ROLE_ADMIN", "ROLE_ROLE_GET"})
    public ResponseEntity<?> allRoles(
    		@RequestParam(value = "page", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "size", defaultValue = "5", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
			@RequestParam(value = "sortDirection", defaultValue = "desc", required = false) String sortDirection
    		){
        return ResponseEntity.ok(userRoleService.getAllRoles(pageNumber, pageSize, sortBy, sortDirection));
    }
    
    @GetMapping("/all")
    @CheckRoles({"ROLE_ADMIN", "ROLE_ROLE_GET"})
    public ResponseEntity<?> allRoles(){
        return ResponseEntity.ok(userRoleService.getAllRoles());
    }
    
    
    @GetMapping("/search")
    @CheckRoles({"ROLE_ADMIN", "ROLE_ROLE_GET"})
    public ResponseEntity<?> searchRoles(
    		@RequestParam(value = "page", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "size", defaultValue = "5", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
			@RequestParam(value = "sortDirection", defaultValue = "desc", required = false) String sortDirection,
			@RequestParam(value = "query", defaultValue = "", required = false) String query
    		){
        return ResponseEntity.ok(userRoleService.searchRoles(query, pageNumber, pageSize, sortBy, sortDirection));
    }
    
    
    @PutMapping("/{id}/updateStatus")
    @CheckRoles({"ROLE_ADMIN", "ROLE_ROLE_UPDATE"})
    public ResponseEntity<?> updateStatus(@PathVariable Long id){
    	userRoleService.updateRoleStatus(id);
    	return ResponseEntity.ok(true);
    }

}
