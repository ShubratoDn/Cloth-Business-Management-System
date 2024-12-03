package com.cloth.business.servicesImple;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.cloth.business.entities.TradeTransaction;
import com.cloth.business.entities.enums.TransactionStatus;
import com.cloth.business.entities.enums.TransactionType;
import com.cloth.business.helpers.HelperUtils;
import com.cloth.business.payloads.PageResponse;
import com.cloth.business.repositories.TransactionRepository;
import com.cloth.business.services.TransactionService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransactionServicesImple implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

  

   
    @Override
    public PageResponse searchTransaction(Long storeId, Long supplierId, String transactioNumber, TransactionStatus purchaseStatus, Date fromDate, Date toDate, TransactionType transactionType, int page, int size, String sortBy, String sortDirection) {

        Sort sort = null;
        if (sortDirection.equalsIgnoreCase("asc")) {
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by(sortBy).descending();
        }

        Page<TradeTransaction> pageInfo;

        Pageable pageable = PageRequest.of(page, size, sort);
        pageInfo = transactionRepository.searchTransaction(storeId, supplierId,transactioNumber, purchaseStatus, fromDate, toDate, transactionType, pageable);
        return HelperUtils.pageToPageResponse(pageInfo);
    }
}
