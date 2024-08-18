package com.cloth.business.repositories;

import com.cloth.business.entities.UserRole;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
//	@Cacheable(value = "userRoles", key = "#role")
    UserRole findByRole(String role);

    @Query(value = "SELECT * FROM user_role WHERE title LIKE %:title% OR role LIKE %:role% OR category LIKE %:category%", 
            countQuery = "SELECT count(*) FROM user_role WHERE title LIKE %:title% OR role LIKE %:role% OR category LIKE %:category%", 
            nativeQuery = true)
     Page<UserRole> searchRoles(@Param("title") String title, 
                                                  @Param("role") String role, 
                                                  @Param("category") String category, 
                                                  Pageable page);
}
