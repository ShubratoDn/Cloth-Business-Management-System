package com.cloth.business.repositories;

import java.util.Date;

import com.cloth.business.entities.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cloth.business.entities.TradeTransaction;
import com.cloth.business.entities.enums.TransactionStatus;

public interface TransactionRepository extends JpaRepository<TradeTransaction, Long> {
	@Query("SELECT COUNT(p) FROM TradeTransaction p WHERE p.store.id = :storeId")
	long countPurchasesByStore(@Param("storeId") Long storeId);
	
	
	@Query("SELECT tt FROM TradeTransaction tt WHERE " +
	           "(:storeId IS NULL OR tt.store.id = :storeId) AND " +
	           "(:supplierId IS NULL OR tt.partner.id = :supplierId) AND " +
	           "(:transactionNumber IS NULL OR tt.transactionNumber LIKE %:transactionNumber%) AND " +
	           "(:purchaseStatus IS NULL OR tt.transactionStatus = :purchaseStatus) AND " +
	           "(:fromDate IS NULL OR tt.transactionDate >= :fromDate) AND " +
	           "(:toDate IS NULL OR tt.transactionDate <= :toDate) AND " +
				"(:transactionType IS NULL OR tt.transactionType = :transactionType) " )
	Page<TradeTransaction> searchPurchases(
	            @Param("storeId") Long storeId,
	            @Param("supplierId") Long supplierId,
	            @Param("transactionNumber") String transactionNumber,
	            @Param("purchaseStatus") TransactionStatus purchaseStatus,
	            @Param("fromDate") Date fromDate,
	            @Param("toDate") Date toDate,
	            @Param("transactionType") TransactionType transactionType,
	            Pageable pageable
	    );
	
	
	TradeTransaction findByIdAndTransactionNumber(Long id, String po);
	
	TradeTransaction findByIdAndTransactionNumberAndTransactionType(Long id, String po, TransactionType type);

	// Query to count the number of purchase orders for a specific store within a date range
	@Query("SELECT COUNT(p) FROM TradeTransaction p WHERE p.store.id = :storeId AND p.transactionDate BETWEEN :startDate AND :endDate")
	int countByStoreIdAndDateRange(@Param("storeId") Long storeId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

	@Query("SELECT COUNT(tt) FROM TradeTransaction tt WHERE tt.store.id = :storeId AND tt.transactionDate BETWEEN :startDate AND :endDate AND tt.transactionType = :transactionType")
	int countByStoreIdAndDateRangeAndType(@Param("storeId") Long storeId, @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("transactionType")TransactionType transactionType);
}
