package com.cloth.business.services;

import com.cloth.business.DTO.UserDTO;
import com.cloth.business.entities.User;

public interface UserService {
    UserDTO findByPhoneOrEmail(String phone, String email);

    UserDTO findByPhone(String phone);

    UserDTO findByEmail(String email);

    UserDTO findById(Long id);

    UserDTO addUser(UserDTO userDTO);
}
