package com.cloth.business.repositories;

import com.cloth.business.entities.Stock;
import com.cloth.business.entities.StockOverview;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface StockRepository extends JpaRepository<Stock, Long> {
    
	@Query("SELECT new com.cloth.business.entities.StockOverview(s.product, SUM(s.quantity), s.store) " +
            "FROM Stock s " +
            "WHERE (:storeId IS NULL OR s.store.id = :storeId) " +
            "AND (:productName IS NULL OR LOWER(s.product.name) LIKE LOWER(CONCAT('%', :productName, '%'))) " +
            "GROUP BY s.product, s.store")
    Page<StockOverview> findStockOverview(@Param("storeId") Long storeId, 
                                          @Param("productName") String productName, 
                                          Pageable pageable);
	
	@Query("SELECT new com.cloth.business.entities.StockOverview(s.product, SUM(s.quantity), s.store) " +
	       "FROM Stock s " +
	       "WHERE (:storeId IS NULL OR s.store.id = :storeId) " +
	       "AND (:productId IS NULL OR s.product.id = :productId) " + 
	       "AND (:productName IS NULL OR LOWER(s.product.name) LIKE LOWER(CONCAT('%', :productName, '%'))) " +
	       "GROUP BY s.product, s.store")
	Page<StockOverview> findStockOverview(@Param("storeId") Long storeId, 
	                                      @Param("productId") Long productId,  // New parameter
	                                      @Param("productName") String productName, 
	                                      Pageable pageable);

}
