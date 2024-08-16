package com.cloth.business.services;

import java.util.List;

import com.cloth.business.DTO.UserDTO;

public interface UserServices {
    UserDTO findByPhoneOrEmail(String phone, String email);

    UserDTO findByPhone(String phone);

    UserDTO findByEmail(String email);

    UserDTO findById(Long id);
    
    List<UserDTO> getAllUsers();

    UserDTO addUser(UserDTO userDTO);
    
    UserDTO updateUser(UserDTO userDTO);
}
