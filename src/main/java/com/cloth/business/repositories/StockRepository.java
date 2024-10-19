package com.cloth.business.repositories;

import com.cloth.business.entities.Stock;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {
    @Query("SELECT new Stock(s.product, SUM(s.quantity)) " +
            "FROM Stock s " +
            "WHERE (:storeId IS NULL OR s.store.id = :storeId) " +
            "AND (:productName IS NULL OR LOWER(s.product.name) LIKE LOWER(CONCAT('%', :productName, '%'))) " +
            "GROUP BY s.product")
    Page<Stock> findStockOverview(@Param("storeId") Long storeId, @Param("productName") String productName, Pageable pageable);
}
