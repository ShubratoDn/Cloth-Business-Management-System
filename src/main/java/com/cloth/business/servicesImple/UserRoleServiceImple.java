package com.cloth.business.servicesImple;

import com.cloth.business.DTO.UserDTO;
import com.cloth.business.entities.User;
import com.cloth.business.entities.UserRole;
import com.cloth.business.exceptions.ResourceAlreadyExistsException;
import com.cloth.business.exceptions.ResourceNotFoundException;
import com.cloth.business.repositories.UserRepository;
import com.cloth.business.repositories.UserRoleRepository;
import com.cloth.business.services.UserRoleService;
import com.cloth.business.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserRoleServiceImple implements UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public UserRole addRole(UserRole userRole) {
        UserRole dbRole = userRoleRepository.findByRole(userRole.getRole());
        if(dbRole != null){
            throw new ResourceAlreadyExistsException("Role", userRole.getRole());
        }
        UserRole save = userRoleRepository.save(userRole);
        return save;
    }

    @Override
    public UserRole updateRole(UserRole userRole) {
        UserRole role = userRoleRepository.findById(userRole.getId()).orElseThrow(() -> new ResourceNotFoundException("Role ID", userRole.getId() + ""));

        role.setRole(userRole.getRole());
        role.setCategory(userRole.getCategory());
        role.setTitle(userRole.getTitle());

        UserRole save = userRoleRepository.save(role);
        return save;
    }

    @Override
    public UserRole getRoleById(Long id) {
        return userRoleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role ID", id + ""));
    }

    @Override
    public List<UserRole> getAllRoles() {
        List<UserRole> all = new ArrayList<>();
        all = userRoleRepository.findAll();
        return all;
    }
}
