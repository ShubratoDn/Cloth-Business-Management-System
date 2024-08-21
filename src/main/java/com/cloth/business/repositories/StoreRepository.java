package com.cloth.business.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cloth.business.entities.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {
	List<Store> findByStoreCodeOrStoreName(String storeCode, String storeName);

	@Query("SELECT s FROM Store s WHERE " + "LOWER(s.storeName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
			+ "LOWER(s.storeCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
			+ "LOWER(s.address) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
			+ "LOWER(s.remark) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	Page<Store> searchStores(@Param("keyword") String keyword, Pageable pageable);
}
