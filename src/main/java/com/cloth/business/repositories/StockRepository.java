package com.cloth.business.repositories;

import com.cloth.business.entities.Stock;
import com.cloth.business.payloads.StockOverview;

import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface StockRepository extends JpaRepository<Stock, Long> {
    
//	@Query("SELECT new com.cloth.business.payloads.StockOverview(s.product, SUM(s.quantity), s.store) " +
//            "FROM Stock s " +
//            "WHERE (:storeId IS NULL OR s.store.id = :storeId) " +
//            "AND (:productName IS NULL OR LOWER(s.product.name) LIKE LOWER(CONCAT('%', :productName, '%'))) " +
//            "GROUP BY s.product, s.store")
//    Page<StockOverview> findStockOverview(@Param("storeId") Long storeId,
//                                          @Param("productName") String productName,
//                                          Pageable pageable);
//

	@Query("SELECT new com.cloth.business.payloads.StockOverview(s.product, " +
			"SUM(CASE WHEN s.transactionType = 'PURCHASE' THEN s.quantity " +
			"WHEN s.transactionType = 'SALE' THEN -s.quantity ELSE 0 END), " +
			"s.store) " +
			"FROM Stock s " +
			"WHERE (:storeId IS NULL OR s.store.id = :storeId) " +
			"AND (:productName IS NULL OR LOWER(s.product.name) LIKE LOWER(CONCAT('%', :productName, '%'))) " +
			"GROUP BY s.product, s.store")
	Page<StockOverview> findStockOverview(@Param("storeId") Long storeId,
										  @Param("productName") String productName,
										  Pageable pageable);


//	@Query("SELECT new com.cloth.business.payloads.StockOverview(s.product, SUM(s.quantity), s.store) " +
//	       "FROM Stock s " +
//	       "WHERE (:storeId IS NULL OR s.store.id = :storeId) " +
//	       "AND (:productId IS NULL OR s.product.id = :productId) " +
//	       "AND (:productName IS NULL OR LOWER(s.product.name) LIKE LOWER(CONCAT('%', :productName, '%'))) " +
//	       "GROUP BY s.product, s.store")
//	Page<StockOverview> findStockOverview(@Param("storeId") Long storeId,
//	                                      @Param("productId") Long productId,  // New parameter
//	                                      @Param("productName") String productName,
//	                                      Pageable pageable);



	@Query("SELECT new com.cloth.business.payloads.StockOverview(s.product, " +
			"SUM(CASE WHEN s.transactionType = 'PURCHASE' THEN s.quantity " +
			"WHEN s.transactionType = 'SALE' THEN -s.quantity ELSE 0 END), " +
			"s.store) " +
			"FROM Stock s " +
			"WHERE (:storeId IS NULL OR s.store.id = :storeId) " +
			"AND (:productId IS NULL OR s.product.id = :productId) " +
			"AND (:productName IS NULL OR LOWER(s.product.name) LIKE LOWER(CONCAT('%', :productName, '%'))) " +
			"GROUP BY s.product, s.store")
	Page<StockOverview> findStockOverview(@Param("storeId") Long storeId,
										  @Param("productId") Long productId,
										  @Param("productName") String productName,
										  Pageable pageable);




//	@Query("SELECT new com.cloth.business.payloads.StockOverview(s.product, SUM(s.quantity), s.store) " +
//		       "FROM Stock s " +
//		       "WHERE (:storeId IS NULL OR s.store.id = :storeId) "+
//		       "GROUP BY s.product, s.store")
//	List<StockOverview> findStockOverviewByStore(@Param("storeId") Long storeId);


	@Query("SELECT new com.cloth.business.payloads.StockOverview(s.product, " +
			"SUM(CASE WHEN s.transactionType = 'PURCHASE' THEN s.quantity " +
			"WHEN s.transactionType = 'SALE' THEN -s.quantity ELSE 0 END), " +
			"s.store) " +
			"FROM Stock s " +
			"WHERE (:storeId IS NULL OR s.store.id = :storeId) " +
			"GROUP BY s.product, s.store")
	List<StockOverview> findStockOverviewByStore(@Param("storeId") Long storeId);




	@Query("SELECT new com.cloth.business.payloads.StockOverview(s.product, " +
			"SUM(CASE WHEN s.transactionType = 'PURCHASE' THEN s.quantity " +
			"WHEN s.transactionType = 'SALE' THEN -s.quantity ELSE 0 END), " +
			"s.store) " +
			"FROM Stock s " +
			"WHERE (:storeId IS NULL OR s.store.id = :storeId) " +
			"AND (:productId IS NULL OR s.product.id = :productId) " +
			"GROUP BY s.product, s.store")
	List<StockOverview> findStockByStoreAndProduct(@Param("storeId") Long storeId,
										  @Param("productId") Long productId
										  );
}
