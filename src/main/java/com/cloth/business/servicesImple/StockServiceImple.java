package com.cloth.business.servicesImple;

import com.cloth.business.entities.TradeTransaction;
import com.cloth.business.entities.TradeTransactionDetails;
import com.cloth.business.entities.Stock;
import com.cloth.business.payloads.StockOverview;
import com.cloth.business.helpers.HelperUtils;
import com.cloth.business.payloads.PageResponse;
import com.cloth.business.repositories.StockRepository;
import com.cloth.business.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class StockServiceImple implements StockService {

    @Autowired
    private StockRepository stockRepository;

    @Override
    public List<Stock> updateStock(TradeTransaction transaction) {
        List<Stock> stockList = new ArrayList<>();
        for(TradeTransactionDetails transactionDetail : transaction.getTransactionDetails()){
            Stock stock = new Stock();
            stock.setLocation(transaction.getStore().getAddress());
            stock.setProduct(transactionDetail.getProduct());
            stock.setQuantity(transactionDetail.getQuantity());
            stock.setStore(transaction.getStore());
            stock.setTransactionDetasils(transactionDetail);
            stock.setTimestamp(new Date());
            stock.setTransaction(transaction);
            stock.setTransactionType(transaction.getTransactionType());
            stockList.add(stock);
        }
        return  stockRepository.saveAll(stockList);
    }

    @Override
    public PageResponse getStockOverview(Long storeId, Long productId, String productName, int page, int size, String sortBy, String sortDirection){

        Sort sort = null;
        if (sortDirection.equalsIgnoreCase("asc")) {
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by(sortBy).descending();
        }

        Page<StockOverview> pageInfo;
        Pageable pageable = PageRequest.of(page, size, sort);
        pageInfo = stockRepository.findStockOverview(storeId, productId, productName, pageable);
        return HelperUtils.pageToPageResponse(pageInfo);
    }
    
    @Override
    public List<StockOverview> getStockOverviewByStore(Long storeId) {
    	List<StockOverview> stockOverviewByStore = stockRepository.findStockOverviewByStore(storeId);
    	return stockOverviewByStore;
    }

    @Override
    public List<StockOverview> findStockByStoreAndProduct(Long storeId, Long productId){
        return stockRepository.findStockByStoreAndProduct(storeId, productId);
    }
}
