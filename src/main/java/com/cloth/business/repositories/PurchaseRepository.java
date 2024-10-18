package com.cloth.business.repositories;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cloth.business.entities.Purchase;
import com.cloth.business.entities.enums.PurchaseStatus;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
	@Query("SELECT COUNT(p) FROM Purchase p WHERE p.store.id = :storeId")
	long countPurchasesByStore(@Param("storeId") Long storeId);
	
	
	@Query("SELECT p FROM Purchase p WHERE " +
	           "(:storeId IS NULL OR p.store.id = :storeId) AND " +
	           "(:supplierId IS NULL OR p.supplier.id = :supplierId) AND " +
	           "(:poNumber IS NULL OR p.poNumber LIKE %:poNumber%) AND " +
	           "(:purchaseStatus IS NULL OR p.purchaseStatus = :purchaseStatus) AND " +
	           "(:fromDate IS NULL OR p.purchaseDate >= :fromDate) AND " +
	           "(:toDate IS NULL OR p.purchaseDate <= :toDate)")
	Page<Purchase> searchPurchases(
	            @Param("storeId") Long storeId,
	            @Param("supplierId") Long supplierId,
	            @Param("poNumber") String poNumber,
	            @Param("purchaseStatus") PurchaseStatus purchaseStatus,
	            @Param("fromDate") Date fromDate,
	            @Param("toDate") Date toDate,
	            Pageable pageable
	    );
	
	
	Purchase findByIdAndPoNumber(Long id, String po);

	// Query to count the number of purchase orders for a specific store within a date range
	@Query("SELECT COUNT(p) FROM Purchase p WHERE p.store.id = :storeId AND p.purchaseDate BETWEEN :startDate AND :endDate")
	int countByStoreIdAndDateRange(@Param("storeId") Long storeId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
