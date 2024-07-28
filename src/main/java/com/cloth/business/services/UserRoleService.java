package com.cloth.business.services;

import com.cloth.business.entities.UserRole;

import java.util.List;

public interface UserRoleService {
    UserRole addRole(UserRole userRole);
    UserRole updateRole(UserRole userRole);
    UserRole getRoleById(Long id);
    List<UserRole> getAllRoles();

}
