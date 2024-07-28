package com.cloth.business.controllers;

import com.cloth.business.entities.UserRole;
import com.cloth.business.services.UserRoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/role")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @PostMapping("/")
    public ResponseEntity<?> addRole(@Valid @RequestBody UserRole userRole){
        UserRole role = userRoleService.addRole(userRole);
        return ResponseEntity.ok(role);
    }

    @GetMapping("/all")
    public ResponseEntity<?> allRoles(){
        return ResponseEntity.ok(userRoleService.getAllRoles());
    }

}
