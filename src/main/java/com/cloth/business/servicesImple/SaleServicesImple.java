package com.cloth.business.servicesImple;

import com.cloth.business.configurations.security.CustomUserDetails;
import com.cloth.business.entities.*;
import com.cloth.business.entities.enums.TransactionStatus;
import com.cloth.business.entities.enums.TransactionType;
import com.cloth.business.exceptions.StockException;
import com.cloth.business.helpers.HelperUtils;
import com.cloth.business.payloads.PageResponse;
import com.cloth.business.payloads.StockOverview;
import com.cloth.business.repositories.ProductCategoryRepository;
import com.cloth.business.repositories.ProductRepository;
import com.cloth.business.repositories.TransactionDetailsRepository;
import com.cloth.business.repositories.TransactionRepository;
import com.cloth.business.services.*;
import jakarta.transaction.Transactional;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SaleServicesImple implements SaleService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;

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

//    @Override
//    public TradeTransaction updateSale(TradeTransaction sale, TradeTransaction dbSale) {
//
//        // Update store if a new store ID is provided
//        if (sale.getStore().getId() != null && sale.getStore().getId() > 0) {
//            Store store = storeServices.getStoreById(sale.getStore().getId());
//            dbSale.setStore(store);
//        }
//
//        // Update partner if a new partner ID is provided
//        if (sale.getPartner().getId() != null && sale.getPartner().getId() > 0) {
//            StakeHolder customer = stakeHolderService.getStakeHolderWithType(sale.getPartner().getId(), "customer");
//            dbSale.setPartner(customer);
//        }
//
//        // List to store updated sale details
//        List<TradeTransactionDetails> updatedSaleDetails = new ArrayList<>();
//        Double productPriceTotal = 0.00;
//
//        // Set up updated details and calculate total price
//        for (TradeTransactionDetails saleDetail : sale.getTransactionDetails()) {
//            Product product = productService.getProductById(saleDetail.getProduct().getId());
//            saleDetail.setProduct(product);
//
//            Double total = saleDetail.getQuantity() * saleDetail.getPrice();
//            productPriceTotal += total;
//
//            saleDetail.setTradeTransaction(sale);
//            updatedSaleDetails.add(saleDetail);
//        }
//
//        // Calculate grand total considering discount and charge
//        Double grandTotal = productPriceTotal;
//        grandTotal = sale.getDiscountAmount() == null ? grandTotal : grandTotal - sale.getDiscountAmount();
//        grandTotal = sale.getChargeAmount() == null ? grandTotal : grandTotal + sale.getChargeAmount();
//
//        sale.setTotalAmount(grandTotal);
//
//        // Update the basic sale attributes
//        dbSale.setRemark(sale.getRemark());
//        dbSale.setDiscountAmount(sale.getDiscountAmount() != null ? sale.getDiscountAmount() : 0);
//        dbSale.setChargeAmount(sale.getChargeAmount() != null ? sale.getChargeAmount() : 0);
//        dbSale.setChargeRemark(sale.getChargeRemark());
//        dbSale.setDiscountRemark(sale.getDiscountRemark());
//
//        dbSale.setTransactionDate(sale.getTransactionDate());
//        dbSale.setTransactionStatus(sale.getTransactionStatus());
//        dbSale.setTotalAmount(sale.getTotalAmount());
//        dbSale.setLastUpdatedBy(sale.getLastUpdatedBy());
//        dbSale.setLastUpdatedDate(sale.getLastUpdatedDate());
//
//        // Handle removal of transaction details that are not part of the updated sale
//        List<TradeTransactionDetails> dbSaleDetails = dbSale.getTransactionDetails();
//
//        // Collect the ids of the current sale details
//        Set<Long> saleDetailIds = sale.getTransactionDetails().stream()
//                .map(detail -> detail.getId())
//                .collect(Collectors.toSet());
//
//        // Find details in dbSale that need to be removed (i.e., not in the sale)
//        List<Long> idsToRemove = dbSaleDetails.stream()
//                .filter(detail -> !saleDetailIds.contains(detail.getId()))
//                .map(TradeTransactionDetails::getId)
//                .collect(Collectors.toList());
//
//        System.out.println("IDs to remove from dbSale: " + idsToRemove);
//
//        // Add the updated details to dbSale
//        dbSale.setTransactionDetails(updatedSaleDetails);
//
//        // Save the updated sale transaction
//        TradeTransaction savedTransaction = transactionRepository.save(dbSale);
//        System.out.println("Saved updated TradeTransaction with ID: " + savedTransaction.getId());
//
//        // Now remove the details that are no longer part of the sale
//        for (Long idToRemove : idsToRemove) {
//            System.out.println("Removing TradeTransactionDetail with ID: " + idToRemove);
//            transactionDetailsRepository.deleteById(idToRemove);  // Assuming you have this method in the repository
//        }
//
//        // Return the updated transaction after removal of details
//        return savedTransaction;
//    }







//    If a row of sale.getTransactionDetails() does not exist in the dbSale, then delete the dbSale.getTransactionDetails() row after updating
//    @Override
//    public TradeTransaction updateSale(TradeTransaction sale, TradeTransaction dbSale) {
//
//        if (sale.getStore().getId() != null && sale.getStore().getId() > 0) {
//            Store store = storeServices.getStoreById(sale.getStore().getId());
//            dbSale.setStore(store);
//        }
//
//        if (sale.getPartner().getId() != null && sale.getPartner().getId() > 0) {
//            StakeHolder customer = stakeHolderService.getStakeHolderWithType(sale.getPartner().getId(), "customer");
//            dbSale.setPartner(customer);
//        }
//
//        List<TradeTransactionDetails> updatedSaleDetails = new ArrayList<>();
//        Double productPriceTotal = 0.00;
//
//        // Step 1: Process and update the new transaction details (the ones that should remain or be added)
//        for (TradeTransactionDetails saleDetail : sale.getTransactionDetails()) {
//            Product product = productService.getProductById(saleDetail.getProduct().getId());
//            saleDetail.setProduct(product);
//
//            Double total = saleDetail.getQuantity() * saleDetail.getPrice();
//            productPriceTotal += total;
//
//            saleDetail.setTradeTransaction(sale);
//            updatedSaleDetails.add(saleDetail);
//        }
//
//        Double grandTotal = productPriceTotal;
//        grandTotal = sale.getDiscountAmount() == null ? grandTotal : grandTotal - sale.getDiscountAmount();
//        grandTotal = sale.getChargeAmount() == null ? grandTotal : grandTotal + sale.getChargeAmount();
//
//        sale.setTotalAmount(grandTotal);
//
//        dbSale.setRemark(sale.getRemark());
//        dbSale.setDiscountAmount(sale.getDiscountAmount() != null ? sale.getDiscountAmount() : 0);
//        dbSale.setChargeAmount(sale.getChargeAmount() != null ? sale.getChargeAmount() : 0);
//        dbSale.setChargeRemark(sale.getChargeRemark());
//        dbSale.setDiscountRemark(sale.getDiscountRemark());
//        dbSale.setTransactionDate(sale.getTransactionDate());
//        dbSale.setTransactionStatus(sale.getTransactionStatus());
//        dbSale.setTotalAmount(sale.getTotalAmount());
//        dbSale.setLastUpdatedBy(sale.getLastUpdatedBy());
//        dbSale.setLastUpdatedDate(sale.getLastUpdatedDate());
//
////        // Step 2: Find and delete the transaction details from dbSale that are not present in sale
////        List<TradeTransactionDetails> dbSaleDetails = dbSale.getTransactionDetails();
////        for (TradeTransactionDetails dbSaleDetail : dbSaleDetails) {
////            boolean existsInSale = sale.getTransactionDetails().stream()
////                    .anyMatch(saleDetail -> saleDetail.getId().equals(dbSaleDetail.getId())); // Check by ID or another unique field
////
////            if (!existsInSale) {
////                System.out.println(dbSaleDetail.getId() + " not exists. Preparing to remove");
////                // If the detail does not exist in the new sale, delete it from dbSale
////                transactionDetailsRepository.deleteById(dbSaleDetail.getId()); // Assuming you have a repository for TradeTransactionDetails
////            }
////        }
//
////        // Step 2: Find and collect the IDs of the transaction details from dbSale that are not present in sale
////        List<Long> idsToDelete = new ArrayList<>();
////        List<TradeTransactionDetails> dbSaleDetails = dbSale.getTransactionDetails();
////        for (TradeTransactionDetails dbSaleDetail : dbSaleDetails) {
////            boolean existsInSale = sale.getTransactionDetails().stream()
////                    .peek(saleDetail -> System.out.println("Checking sale detail ID: " + saleDetail.getId()))
////                    .anyMatch(saleDetail -> saleDetail.getId().equals(dbSaleDetail.getId()));
////
////
////            if (!existsInSale) {
////                System.out.println(dbSaleDetail.getId() + " not exists. Preparing to remove");
////                // Collect IDs of the details to delete
////                idsToDelete.add(dbSaleDetail.getId());
////            }
////        }
////
////        // Step 3: Set the updated transaction details list on the dbSale
////        dbSale.setTransactionDetails(sale.getTransactionDetails());
////
////        // Step 4: Save and return the updated sale
////        dbSale = transactionRepository.save(dbSale);
////
////        // Step 4: Delete the transaction details after the save operation
////        if (!idsToDelete.isEmpty()) {
////            System.out.println("Deleting transaction details with IDs: " + idsToDelete);
////            for (Long id : idsToDelete) {
////                transactionDetailsRepository.deleteById(id); // Assuming you have a repository for TradeTransactionDetails
////            }
////        }
////
////        return dbSale;
////    }



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


    @Override
    public TradeTransaction updateSaleStatus(TradeTransaction sale, TransactionStatus status) {
        if (status.equals(TransactionStatus.APPROVED)) {
            sale.setTransactionStatus(TransactionStatus.APPROVED);
            sale.setApprovedBy(HelperUtils.getLoggedinUser());
            sale.setApprovedDate(new Date());

            sale.setRejectedBy(null);
            sale.setRejectedDate(null);


            List<TradeTransactionDetails> outOfStockList = new ArrayList<>();
            // Group the transaction details by product and sum the quantities
            Map<Long, Integer> productQuantityMap = new HashMap<>();

            // Step 1: Sum quantities of the same type of product
            for (TradeTransactionDetails transactionDetails : sale.getTransactionDetails()) {
                Long productId = transactionDetails.getProduct().getId();
                productQuantityMap.put(productId, productQuantityMap.getOrDefault(productId, 0) + transactionDetails.getQuantity());
            }

            // Step 2: Check stock for each product
            for (Map.Entry<Long, Integer> entry : productQuantityMap.entrySet()) {
                Long productId = entry.getKey();
                Integer totalRequiredQuantity = entry.getValue();

                // Fetch the stock for this product in the store
                List<StockOverview> stockByStoreAndProduct = stockService.findStockByStoreAndProduct(sale.getStore().getId(), productId);

                // If stock is insufficient, add the product to the outOfStockList
                if (stockByStoreAndProduct.isEmpty() || stockByStoreAndProduct.get(0).getTotalQuantity() < totalRequiredQuantity) {
                    // Find the product details (you can fetch this from your transaction details list or another service)
                    TradeTransactionDetails outOfStockProduct = sale.getTransactionDetails().stream()
                            .filter(detail -> detail.getProduct().getId().equals(productId))
                            .findFirst()
                            .orElse(null);

                    if (outOfStockProduct != null) {
                        outOfStockList.add(outOfStockProduct);
                    }
                }
            }

            // Step 3: Build and throw the exception if any product is out of stock
            if (!outOfStockList.isEmpty()) {
                // Build a message with the product names that are out of stock
                StringBuilder productNames = new StringBuilder("Not enough stock for the following products: ");

                outOfStockList.forEach(stock -> productNames.append(stock.getProduct().getName())
                        .append(" (").append(stock.getProduct().getCategory().getName()).append("), ")
                );

                // Remove the last comma and space
                if (productNames.length() > 0) {
                    productNames.setLength(productNames.length() - 2);
                }

                // Throw the custom exception with the product names
                throw new StockException(productNames.toString());
            }


            //UPDATING STOCK
            stockService.updateStock(sale);
            
            
            Double productPriceTotal = 0.00;
			for (TradeTransactionDetails purchaseDetail : sale.getTransactionDetails()) {
				Double total = purchaseDetail.getQuantity() * purchaseDetail.getPrice();
				productPriceTotal = productPriceTotal + (total);		
			}

			Double grandTotal = productPriceTotal;
			grandTotal = sale.getDiscountAmount() == null ? grandTotal : grandTotal - sale.getDiscountAmount();
			grandTotal = sale.getChargeAmount() == null ? grandTotal : grandTotal + sale.getChargeAmount();

			sale.setTotalAmount(grandTotal);	
            
        } else if (status.equals(TransactionStatus.REJECTED)) {
            sale.setRejectedBy(HelperUtils.getLoggedinUser());
            sale.setRejectedDate(new Date());
            sale.setTransactionStatus(TransactionStatus.REJECTED);

            sale.setApprovedBy(null);
            sale.setApprovedDate(null);

        } else if (status.equals(TransactionStatus.CLOSED)) {
            if (sale.getTransactionStatus().equals(TransactionStatus.APPROVED) || sale.getTransactionStatus().equals(TransactionStatus.REJECTED)) {
                sale.setTransactionStatus(TransactionStatus.CLOSED);
            }
        }
        return transactionRepository.save(sale);
    }
}
