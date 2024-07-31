package com.cloth.business.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cloth.business.entities.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {

}
