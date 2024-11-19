package com.cloth.business.servicesImple;

import com.cloth.business.configurations.security.CustomUserDetails;
import com.cloth.business.entities.*;
import com.cloth.business.entities.enums.TransactionStatus;
import com.cloth.business.entities.enums.TransactionType;
import com.cloth.business.helpers.HelperUtils;
import com.cloth.business.payloads.PageResponse;
import com.cloth.business.repositories.ProductCategoryRepository;
import com.cloth.business.repositories.ProductRepository;
import com.cloth.business.repositories.TransactionRepository;
import com.cloth.business.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SaleServicesImple implements SaleService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private StoreServices storeServices;

    @Autowired
    private StakeHolderService stakeHolderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private FileServices fileServices;

    @Autowired
    private StockService stockService;

    @Override
    public TradeTransaction createSale(TradeTransaction sale) {
        sale.setTransactionType(TransactionType.SALE);
        //getting the logged in user
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User loggedInUser = customUserDetails.getLoggedInUser();

        Store store = storeServices.getStoreById(sale.getStore().getId());
        StakeHolder customer = stakeHolderService.getStakeHolderWithType(sale.getPartner().getId(), "customer");


        sale.setProcessedBy(loggedInUser);
        sale.setStore(store);
        sale.setPartner(customer);


        List<TradeTransactionDetails> updatedSaleDetails = new ArrayList<>();
        double productPriceTotal = 0.00;

        for (TradeTransactionDetails saleDetail : sale.getTransactionDetails()) {

            Product product = productService.getProductById(saleDetail.getProduct().getId());

            saleDetail.setProduct(product);
            
            double total = saleDetail.getQuantity() * saleDetail.getPrice();
            productPriceTotal = productPriceTotal + (total);


            saleDetail.setTradeTransaction(sale);
            updatedSaleDetails.add(saleDetail);
        }

        double grandTotal = productPriceTotal;
        grandTotal = sale.getDiscountAmount() == null ? grandTotal : grandTotal - sale.getDiscountAmount();
        grandTotal = sale.getChargeAmount() == null ? grandTotal : grandTotal + sale.getChargeAmount();

        sale.setTotalAmount(grandTotal);
        sale.setTransactionNumber(generateSOnumber(store));
        return transactionRepository.save(sale);
    }
    
    
	@Override
	public TradeTransaction getSaleInfoByIdAndSO(Long id, String so) {
		TradeTransaction sale = transactionRepository.findByIdAndTransactionNumberAndTransactionType(id, so, TransactionType.SALE);
		return sale;
	}


//    update sale
	@Override
	public TradeTransaction updateSale(TradeTransaction sale, TradeTransaction dbSale) {

		if (sale.getStore().getId() != null && sale.getStore().getId() > 0) {
			Store store = storeServices.getStoreById(sale.getStore().getId());
			dbSale.setStore(store);
		}

		if (sale.getPartner().getId() != null && sale.getPartner().getId() > 0) {
			StakeHolder customer = stakeHolderService.getStakeHolderWithType(sale.getPartner().getId(),
					"customer");
			dbSale.setPartner(customer);
		}

		List<TradeTransactionDetails> updatedSaleDetails = new ArrayList<>();
		Double productPriceTotal = 0.00;
		for (TradeTransactionDetails saleDetail : sale.getTransactionDetails()) {

            Product product = productService.getProductById(saleDetail.getProduct().getId());
            saleDetail.setProduct(product);

			Double total = saleDetail.getQuantity() * saleDetail.getPrice();
			productPriceTotal = productPriceTotal + (total);

			saleDetail.setTradeTransaction(sale);
			updatedSaleDetails.add(saleDetail);
		}

		Double grandTotal = productPriceTotal;
		grandTotal = sale.getDiscountAmount() == null ? grandTotal : grandTotal - sale.getDiscountAmount();
		grandTotal = sale.getChargeAmount() == null ? grandTotal : grandTotal + sale.getChargeAmount();


		sale.setTotalAmount(grandTotal);


		dbSale.setRemark(sale.getRemark());
		dbSale.setDiscountAmount(sale.getDiscountAmount() != null ? sale.getDiscountAmount() : 0);
		dbSale.setChargeAmount(sale.getChargeAmount() != null ? sale.getChargeAmount() : 0);
		dbSale.setChargeRemark(sale.getChargeRemark());
		dbSale.setDiscountRemark(sale.getDiscountRemark());

		dbSale.setTransactionDate(sale.getTransactionDate());
		dbSale.setTransactionStatus(sale.getTransactionStatus());
		dbSale.setTotalAmount(sale.getTotalAmount());
		dbSale.setLastUpdatedBy(sale.getLastUpdatedBy());
		dbSale.setLastUpdatedDate(sale.getLastUpdatedDate());
		dbSale.setTransactionDetails(sale.getTransactionDetails());

		return transactionRepository.save(dbSale);
	}


	public String generateSOnumber(Store store) {
		Long storeId = store.getId();
		// Get current date (Year and Month)
		LocalDate now = LocalDate.now();
		String year = now.format(DateTimeFormatter.ofPattern("yy"));
		String month = now.format(DateTimeFormatter.ofPattern("MM"));

		// Calculate the first and last day of the current month
		LocalDate firstDayOfMonth = now.withDayOfMonth(1);
		LocalDate lastDayOfMonth = now.withDayOfMonth(now.lengthOfMonth());

		// Convert LocalDate to Date (for JPA query)
		Date startDate = Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date endDate = Date.from(lastDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());

		// Get count of POs for the current month for the given store
		int currentMonthCount = transactionRepository.countByStoreIdAndDateRangeAndType(storeId, startDate, endDate, TransactionType.SALE);

		// Generate the serial number for the PO
		String serialNumber = String.format("%04d", currentMonthCount + 1);

		// Build and return the PO number string
		return String.format("SO-ST%02d-%s%s%s", storeId, year, month, serialNumber);
	}
//
//
//
//
@Override
public PageResponse searchSale(Long storeId, Long supplierId, String poNumber, TransactionStatus purchaseStatus, Date fromDate, Date toDate, TransactionType transactionType, int page, int size, String sortBy, String sortDirection) {

    Sort sort = null;
    if (sortDirection.equalsIgnoreCase("asc")) {
        sort = Sort.by(sortBy).ascending();
    } else {
        sort = Sort.by(sortBy).descending();
    }

    Page<TradeTransaction> pageInfo;

    Pageable pageable = PageRequest.of(page, size, sort);
    pageInfo = transactionRepository.searchPurchases(storeId, supplierId, poNumber, purchaseStatus, fromDate, toDate, transactionType, pageable);
    return HelperUtils.pageToPageResponse(pageInfo);
}
//
//
//	@Override
//	public TradeTransaction getPurchaseInfoByIdAndPO(Long id, String po) {
//		TradeTransaction sale = transactionRepository.findByIdAndTransactionNumber(id, po);
//		return sale;
//	}
//
//
//	@Override
//	public TradeTransaction updatePurchaseStatus(TradeTransaction sale, TransactionStatus status) {
//		if(status.equals(TransactionStatus.APPROVED) ){
//			sale.setTransactionStatus(TransactionStatus.APPROVED);
//			sale.setApprovedBy(HelperUtils.getLoggedinUser());
//			sale.setApprovedDate(new Date());
//
//			sale.setRejectedBy(null);
//			sale.setRejectedDate(null);
//
//			//UPDATING STOCK
//			stockService.updateStock(sale);
//		}else if(status.equals(TransactionStatus.REJECTED)){
//			sale.setRejectedBy(HelperUtils.getLoggedinUser());
//			sale.setRejectedDate(new Date());
// 			sale.setTransactionStatus(TransactionStatus.REJECTED);
//
//			sale.setApprovedBy(null);
//			sale.setApprovedDate(null);
//
//		} else if (status.equals(TransactionStatus.CLOSED)) {
//			if(sale.getTransactionStatus().equals(TransactionStatus.APPROVED) || sale.getTransactionStatus().equals(TransactionStatus.REJECTED)) {
//				sale.setTransactionStatus(TransactionStatus.CLOSED);
//			}
//		}
//		return transactionRepository.save(sale);
//	}
}
