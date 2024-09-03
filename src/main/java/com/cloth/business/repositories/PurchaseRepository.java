package com.cloth.business.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cloth.business.entities.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

}
