package com.cloth.business.repositories;

import com.cloth.business.entities.UserRole;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
	@Cacheable(value = "userRoles", key = "#role")
    UserRole findByRole(String role);

}
