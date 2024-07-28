package com.cloth.business.controllers;

import com.cloth.business.DTO.UserDTO;
import com.cloth.business.payloads.ErrorResponse;
import com.cloth.business.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/v1/users/")
public class UserController {


    @Autowired
    private UserService userService;


    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserDTO userDTO, @PathVariable Long userId){

        UserDTO dbUser = userService.findById(userId);

        if(!userDTO.getPhone().equals(dbUser.getPhone())){
            if(userService.findByPhone(userDTO.getPhone()) != null){
                ErrorResponse errorResponse = new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "Bad Request",
                        "Phone number already exists. Try another phone number"
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
        }


        if(userDTO.getEmail() != null || !userDTO.getEmail().isBlank()){
            if(!userDTO.getEmail().equals(dbUser.getEmail())){
                if(userService.findByEmail(userDTO.getEmail()) != null){
                    ErrorResponse errorResponse = new ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.BAD_REQUEST.value(),
                            "Bad Request",
                            "Email already exists."
                    );
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
                }
            }
        }


//        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userDTO.setIsLocked(false);


//        return ResponseEntity.ok(userDTO);
        // Save the user
        UserDTO savedUser = userServices.addUser(userDTO);

        log.info("New user registered successfully: Id ->{}; Name -> {}", savedUser.getId(), savedUser.getName());

        return ResponseEntity.ok(savedUser);
    }

}
