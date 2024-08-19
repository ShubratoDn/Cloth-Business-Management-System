package com.cloth.business.services;

import com.cloth.business.entities.UserRole;
import com.cloth.business.payloads.PageResponse;

import java.util.List;

public interface UserRoleServices {
    UserRole addRole(UserRole userRole);
    UserRole updateRole(UserRole userRole);
    UserRole getRoleById(Long id);
    List<UserRole> getAllRoles();
    
    PageResponse getAllRoles(int page, int size, String sortBy, String sortDirection);

    PageResponse searchRoles(String query, int page, int size, String sortBy, String sortDirection);
    
    Boolean updateRoleStatus(Long id);
}
