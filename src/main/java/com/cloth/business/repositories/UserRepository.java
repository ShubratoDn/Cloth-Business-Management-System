package com.cloth.business.repositories;

import com.cloth.business.entities.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
	

//    @Cacheable(value = "cache123", key = "#phone + ',' + #email")
    User findByPhoneOrEmail(String phone, String email);

    User findByPhone(String phone);

    User findByEmail(String email);
    
    
    @Query(value = "SELECT * FROM user WHERE " +
            "name LIKE %:name% " +
            "OR phone LIKE %:phone% " +
            "OR email LIKE %:email% " +
            "OR address LIKE %:address% " +
            "OR designation LIKE %:designation%", 
           countQuery = "SELECT count(*) FROM user WHERE " +
            "name LIKE %:name% " +
            "OR phone LIKE %:phone% " +
            "OR email LIKE %:email% " +
            "OR address LIKE %:address% " +
            "OR designation LIKE %:designation%", 
           nativeQuery = true)
    Page<User> searchByFields(@Param("name") String name,
                              @Param("phone") String phone,
                              @Param("email") String email,
                              @Param("address") String address,
                              @Param("designation") String designation,
                              Pageable pageable);
}
