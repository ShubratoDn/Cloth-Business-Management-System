package com.cloth.business.servicesImple;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.cloth.business.entities.UserRole;
import com.cloth.business.exceptions.ResourceAlreadyExistsException;
import com.cloth.business.exceptions.ResourceNotFoundException;
import com.cloth.business.helpers.HelperUtils;
import com.cloth.business.payloads.PageResponse;
import com.cloth.business.repositories.UserRoleRepository;
import com.cloth.business.services.UserRoleServices;

@Service
public class UserRoleServiceImple implements UserRoleServices {

    @Autowired
    private UserRoleRepository userRoleRepository;


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
        role.setIsActive(userRole.getIsActive());
        
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
    
    @Override
    public PageResponse getAllRoles(int pageNumber, int size, String sortBy, String sortDirection) {
    	Sort sort = null;
		if (sortDirection.equalsIgnoreCase("asc")) {
			sort = Sort.by(sortBy).ascending();
		} else {
			sort = Sort.by(sortBy).descending();
		}
		
		Page<UserRole> pageInfo;
		
		try {
			Pageable pageable = PageRequest.of(pageNumber, size, sort);
			pageInfo = userRoleRepository.findAll(pageable);
		} catch (Exception e) {
			Pageable pageable = PageRequest.of(pageNumber, size, Sort.by("id").descending());
			pageInfo = userRoleRepository.findAll(pageable);
		}

		
		PageResponse pageToPageResponse = HelperUtils.pageToPageResponse(pageInfo);
		
    	return pageToPageResponse;
    }
    
    
    
    //search roles
    @Override
    public PageResponse searchRoles(String query, int page, int size, String sortBy, String sortDirection) {
    	Sort sort = null;
		if (sortDirection.equalsIgnoreCase("asc")) {
			sort = Sort.by(sortBy).ascending();
		} else {
			sort = Sort.by(sortBy).descending();
		}
		
		Page<UserRole> pageInfo;
		
		try {
			Pageable pageable = PageRequest.of(page, size, sort);
			pageInfo = userRoleRepository.searchRoles(query, query, query, pageable);
		} catch (Exception e) {
			Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
			pageInfo = userRoleRepository.searchRoles(query, query, query, pageable);
		}

		
		PageResponse pageToPageResponse = HelperUtils.pageToPageResponse(pageInfo);
		
    	return pageToPageResponse;
    }
}
