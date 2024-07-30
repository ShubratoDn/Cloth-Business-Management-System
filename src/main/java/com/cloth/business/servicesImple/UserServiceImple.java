package com.cloth.business.servicesImple;

import com.cloth.business.DTO.UserDTO;
import com.cloth.business.entities.User;
import com.cloth.business.entities.UserRole;
import com.cloth.business.exceptions.ResourceNotFoundException;
import com.cloth.business.repositories.UserRepository;
import com.cloth.business.services.UserRoleService;
import com.cloth.business.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImple implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDTO findByPhoneOrEmail(String phone, String email) {    	
        User user = userRepository.findByPhoneOrEmail(phone, email);
        if(user != null){
            return modelMapper.map(user, UserDTO.class);
        }
        return null;
    }

    @Override
    public UserDTO findByPhone(String phone) {
        User user = userRepository.findByPhone(phone);
        if(user != null){
            return modelMapper.map(user, UserDTO.class);
        }
        return null;
    }

    @Override
    public UserDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if(user != null){
            return modelMapper.map(user, UserDTO.class);
        }
        return null;
    }

    @Override
    public UserDTO findById(Long id) {  
        User user = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User", id+""));
        if(user != null){
            return modelMapper.map(user, UserDTO.class);
        }
        return null;
    }

    @Override
    public UserDTO addUser(UserDTO userDTO) {
        List<UserRole> roles = new ArrayList<>();
        for(UserRole userRole : userDTO.getRoles()) {
            roles.add(userRoleService.getRoleById(userRole.getId()));
        }

        userDTO.setRoles(roles);

        User user = modelMapper.map(userDTO, User.class);
        User save = userRepository.save(user);
        if(save != null) {
            return modelMapper.map(save, UserDTO.class);
        }

        return null;
    }
    
    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        List<UserRole> roles = new ArrayList<>();
        for(UserRole userRole : userDTO.getRoles()) {
            roles.add(userRoleService.getRoleById(userRole.getId()));
        }

        userDTO.setRoles(roles);

        User user = modelMapper.map(userDTO, User.class);
        User save = userRepository.save(user);
        if(save != null) {
            return modelMapper.map(save, UserDTO.class);
        }

        return null;
    }
    
    @Override
    public List<UserDTO> getAllUsers() {
        List<User> all = userRepository.findAll();
        List<UserDTO> users = new ArrayList<>();
        for (User u : all) {
            users.add(modelMapper.map(u, UserDTO.class));
        }
        return users;
    }



}
