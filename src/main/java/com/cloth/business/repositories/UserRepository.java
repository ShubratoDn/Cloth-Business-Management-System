package com.cloth.business.repositories;

import com.cloth.business.entities.User;


import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	

//    @Cacheable(value = "cache123", key = "#phone + ',' + #email")
    User findByPhoneOrEmail(String phone, String email);

    User findByPhone(String phone);

    User findByEmail(String email);
}
