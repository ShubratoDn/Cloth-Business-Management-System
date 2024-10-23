package com.cloth.business.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloth.business.configurations.constants.Constants;
import com.cloth.business.entities.Purchase;
import com.cloth.business.exceptions.ResourceNotFoundException;
import com.cloth.business.services.PurchaseServices;
import com.cloth.business.services.ReportServices;

import jakarta.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {
	
	@Autowired
	private PurchaseServices purchaseServices;
	
	@Autowired
	private ReportServices reportServices;
	
	@GetMapping("/generate-pdf/{id}/{po}")
	public ResponseEntity<?> getPurchaseReport(@PathVariable Long id, @PathVariable String po, HttpServletRequest req) throws Exception {
	
		Purchase purchase = purchaseServices.getPurchaseInfoByIdAndPO(id, po);

		byte[] report;
		
		if(purchase != null) {
			report = reportServices.generatePODetails(purchase);
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+purchase.getPoNumber()+".pdf").contentType(MediaType.APPLICATION_PDF).body(report);
		}else {
			throw new ResourceNotFoundException("Item not found!");
		}
	}
	
	public byte[] generateReport2() {
		Purchase purchaseInfo = purchaseServices.getPurchaseInfoByIdAndPO(4L, "PO8S3");
		
        try {
        	File file = ResourceUtils.getFile("classpath:purchase.jrxml");
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
            
            parameters.put("purchaseDate", purchaseInfo.getPurchaseDate().toString());
            
                        
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
	
	
	
	private void generateReport () {
		Purchase purchaseInfo = purchaseServices.getPurchaseInfoByIdAndPO(4L, "PO8S3");
		try {
			File file = ResourceUtils.getFile("classpath:test_file.jrxml");
			JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
			JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(purchaseInfo.getPurchaseDetails());
			
			Map<String, Object> map = new HashMap<>();
			map.put("createdBy", "Shubrato Debnath");
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, datasource);
	        String tempDir = System.getProperty("java.io.tmpdir");
	        JasperExportManager.exportReportToHtmlFile(jasperPrint, tempDir + "report.html");
	        JasperExportManager.exportReportToPdfFile(jasperPrint, tempDir + "report.pdf");
			
	        System.out.println(tempDir);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
