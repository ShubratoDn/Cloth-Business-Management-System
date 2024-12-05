package com.cloth.business.controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloth.business.configurations.annotations.CheckRoles;
import com.cloth.business.configurations.constants.Constants;
import com.cloth.business.entities.TradeTransaction;
import com.cloth.business.entities.enums.TransactionStatus;
import com.cloth.business.entities.enums.TransactionType;
import com.cloth.business.exceptions.ResourceNotFoundException;
import com.cloth.business.services.PurchaseServices;
import com.cloth.business.services.ReportPDFServices;
import com.cloth.business.servicesImple.ReportExcelServiceImple;

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
	private ReportPDFServices reportServices;
	
	@GetMapping("/generate-pdf/{id}/{po}")
	public ResponseEntity<?> getPurchaseReport(@PathVariable Long id, @PathVariable String po, HttpServletRequest req) throws Exception {
	
		TradeTransaction purchase = purchaseServices.getPurchaseInfoByIdAndPO(id, po);

		byte[] report;
		
		if(purchase != null) {
			report = reportServices.generatePODetails(purchase);
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+purchase.getTransactionNumber()+".pdf").contentType(MediaType.APPLICATION_PDF).body(report);
		}else {
			throw new ResourceNotFoundException("Item not found!");
		}
	}
	
	public byte[] generateReport2() {
		TradeTransaction purchaseInfo = purchaseServices.getPurchaseInfoByIdAndPO(4L, "PO8S3");
		
        try {
        	File file = ResourceUtils.getFile("classpath:purchase.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
           
	    return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	
	
	private void generateReport () {
		TradeTransaction purchaseInfo = purchaseServices.getPurchaseInfoByIdAndPO(4L, "PO8S3");
		try {
			File file = ResourceUtils.getFile("classpath:test_file.jrxml");
			JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
			JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(purchaseInfo.getTransactionDetails());
			
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
	
	
	
	
	
	@Autowired
    private ReportExcelServiceImple excelService;

    @GetMapping("/excel")
    public ResponseEntity<byte[]> exportToExcel() {
        List<String[]> data = Arrays.asList(
            new String[]{"Data1", "Data2", "Data3"},
            new String[]{"Row2Data1", "Row2Data2", "Row2Data3"},
            new String[]{"Row3Data1", "Row3Data2", "Row3Data3"}
        );

        ByteArrayInputStream excelData = excelService.generateExcel(data);
		byte[] bytes = excelData.readAllBytes();

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=data.xlsx");

		return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }
    
    
    
    
    
//    @CheckRoles({"ROLE_ADMIN", "ROLE_REPORT_PROFITABILITY"})
    @GetMapping("/profitability")
    public ResponseEntity<?> downloadReport(
            @RequestParam(value = "storeId", required = false) Long storeId,
            @RequestParam(value = "supplierId", required = false) Long supplierId,
            @RequestParam(value = "poNumber", required = false) String poNumber,
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
            @RequestParam(value = "transactionStatus", required = false) TransactionStatus transactionStatus,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,
            @RequestParam(value = "transactionType", required = false) TransactionType transactionType) {


    	ByteArrayInputStream reportStream = excelService.downloadProfitabilityReport(storeId, supplierId, poNumber, transactionStatus, fromDate, toDate, transactionType);
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=profitability_report.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(reportStream.readAllBytes());
    }
	
	
}
