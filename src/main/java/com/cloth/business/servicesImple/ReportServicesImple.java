package com.cloth.business.servicesImple;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.cloth.business.entities.Purchase;
import com.cloth.business.entities.PurchaseDetails;
import com.cloth.business.helpers.HelperUtils;
import com.cloth.business.payloads.ReportProductDetails;
import com.cloth.business.services.ReportServices;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ReportServicesImple implements ReportServices{

	@Override
	public byte[] generatePODetails(Purchase purchaseInfo) {		
        try {
        	File file = ResourceUtils.getFile("classpath:static/reports/pdfTemplates/purchase.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            
            // Create parameters for the main report
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("poNumber", purchaseInfo.getPoNumber());
            parameters.put("storeName", purchaseInfo.getStore().getStoreName());
            parameters.put("storeCode", purchaseInfo.getStore().getStoreCode());
            parameters.put("storeAddress", purchaseInfo.getStore().getAddress());
            
            parameters.put("supplierName", purchaseInfo.getSupplier().getName());
            parameters.put("supplierAddress", purchaseInfo.getSupplier().getAddress());
            parameters.put("supplierPhone", purchaseInfo.getSupplier().getPhone());            
            parameters.put("supplierEmail", purchaseInfo.getSupplier().getEmail());
            parameters.put("supplierImage", HelperUtils.getBaseURL()+"/" + purchaseInfo.getSupplier().getImage());
            
            parameters.put("purchaseDate", purchaseInfo.getPurchaseDate().toString());

            parameters.put("remark", purchaseInfo.getRemark());
            parameters.put("itemsTotal", purchaseInfo.getTotalAmount() + purchaseInfo.getDiscountAmount() - purchaseInfo.getChargeAmount());
            parameters.put("discount", purchaseInfo.getDiscountAmount());
            parameters.put("discountRemark", purchaseInfo.getDiscountRemark());
            parameters.put("charge", purchaseInfo.getChargeAmount());
            parameters.put("chargeRemark", purchaseInfo.getChargeRemark());
            parameters.put("grandTotal", purchaseInfo.getTotalAmount());



            List<ReportProductDetails> purchaseDetails = new ArrayList<>();
            for(PurchaseDetails detail : purchaseInfo.getPurchaseDetails()){
            	ReportProductDetails productDetails = new ReportProductDetails();
            	productDetails.setItemId(detail.getId());
            	productDetails.setItemName(detail.getProduct().getName());
            	productDetails.setItemUOM(detail.getProduct().getSize());
            	productDetails.setItemCategory(detail.getProduct().getCategory().getName());
            	productDetails.setItemPrice(detail.getPrice());
            	productDetails.setItemQuantity(detail.getQuantity());
            	productDetails.setItemTotal(detail.getPrice() * detail.getQuantity());
            	
            	purchaseDetails.add(productDetails);
            }
            
            JRBeanCollectionDataSource purchaseDetailsDataset = new JRBeanCollectionDataSource(purchaseDetails);
            
            parameters.put("purchaseDetails",purchaseDetailsDataset);            
                        
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

            
//	        String tempDir = System.getProperty("java.io.tmpdir");
//	        JasperExportManager.exportReportToHtmlFile(jasperPrint, tempDir + "report.html");
//	        JasperExportManager.exportReportToPdfFile(jasperPrint, tempDir + "report.pdf");
//			
//	        System.out.println(tempDir);
	     // Export the report to a PDF file (or another format)
	        return JasperExportManager.exportReportToPdf(jasperPrint);
    
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}
	
}
