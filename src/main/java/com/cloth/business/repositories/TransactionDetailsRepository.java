package com.cloth.business.repositories;

import com.cloth.business.entities.TradeTransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionDetailsRepository extends JpaRepository<TradeTransactionDetails, Long> {

}
