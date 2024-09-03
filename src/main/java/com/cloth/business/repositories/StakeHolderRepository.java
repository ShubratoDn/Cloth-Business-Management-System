package com.cloth.business.repositories;


import com.cloth.business.entities.Store;
import com.cloth.business.entities.enums.StakeHolderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cloth.business.entities.StakeHolder;

import java.util.List;

@Repository
public interface StakeHolderRepository extends JpaRepository<StakeHolder, Long> {
	StakeHolder findByPhoneOrEmail(String phone, String email);

	List<StakeHolder> findByStakeHolderTypeAndStore(StakeHolderType stakeHolderType, Store store);
}
