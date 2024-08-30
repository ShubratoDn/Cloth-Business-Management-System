package com.cloth.business.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cloth.business.entities.StakeHolder;

@Repository
public interface StakeHolderRepository extends JpaRepository<StakeHolder, Long> {
}
