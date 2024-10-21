package com.cloth.business.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloth.business.entities.Purchase;
import com.cloth.business.services.PurchaseServices;

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
	
	@GetMapping("/generate-pdf")
	public ResponseEntity<?> generatePdf(){
		generateReport2();
		return ResponseEntity.ok("OK OK");
	}
	
	
	public void generateReport2() {
		Purchase purchaseInfo = purchaseServices.getPurchaseInfoByIdAndPO(4L, "PO8S3");
		
        try {
        	File file = ResourceUtils.getFile("classpath:purchase_report.jrxml");
            // Load the JRXML file
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            // Create parameters for the main report
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("poNumber", purchaseInfo.getPoNumber());
            
            
            // Prepare the sub-report data source
            JRBeanCollectionDataSource detailsDataSource = new JRBeanCollectionDataSource(purchaseInfo.getPurchaseDetails());
            parameters.put("purchaseDetails", detailsDataSource);
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, detailsDataSource);

            
	        String tempDir = System.getProperty("java.io.tmpdir");
	        JasperExportManager.exportReportToHtmlFile(jasperPrint, tempDir + "report.html");
	        JasperExportManager.exportReportToPdfFile(jasperPrint, tempDir + "report.pdf");
			
	        System.out.println(tempDir);

        } catch (Exception e) {
            e.printStackTrace();
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
