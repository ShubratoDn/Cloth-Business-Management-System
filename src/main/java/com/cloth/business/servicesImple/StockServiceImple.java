package com.cloth.business.servicesImple;

import com.cloth.business.entities.Product;
import com.cloth.business.entities.Purchase;
import com.cloth.business.entities.PurchaseDetails;
import com.cloth.business.entities.Stock;
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
    public List<Stock> updateStock(Purchase purchase) {
        List<Stock> stockList = new ArrayList<>();
        for(PurchaseDetails purchaseDetail : purchase.getPurchaseDetails()){
            Stock stock = new Stock();
            stock.setLocation(purchase.getStore().getAddress());
            stock.setProduct(purchaseDetail.getProduct());
            stock.setQuantity(purchaseDetail.getQuantity());
            stock.setStore(purchase.getStore());
            stock.setPurchaseDetasils(purchaseDetail);
            stock.setTimestamp(new Date());
            stock.setPurchase(purchase);

            stockList.add(stock);
        }
        return  stockRepository.saveAll(stockList);
    }

    @Override
    public PageResponse getStockOverview(Long storeId, String productName, int page, int size, String sortBy, String sortDirection){

        Sort sort = null;
        if (sortDirection.equalsIgnoreCase("asc")) {
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by(sortBy).descending();
        }

        Page<Stock> pageInfo;
        Pageable pageable = PageRequest.of(page, size, sort);
        pageInfo = stockRepository.findStockOverview(storeId, productName, pageable);
        return HelperUtils.pageToPageResponse(pageInfo);
    }
}
