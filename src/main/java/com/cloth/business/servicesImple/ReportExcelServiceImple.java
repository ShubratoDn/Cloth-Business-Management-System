package com.cloth.business.servicesImple;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloth.business.entities.TradeTransaction;
import com.cloth.business.entities.enums.TransactionStatus;
import com.cloth.business.entities.enums.TransactionType;
import com.cloth.business.services.ReportExcelService;
import com.cloth.business.services.TransactionService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class ReportExcelServiceImple implements ReportExcelService {
	
	@Autowired
	private TransactionService transactionService;

    public ByteArrayInputStream generateExcel(List<String[]> data){
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Data");

            // Create Header Row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Column 1", "Column 2", "Column 3"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
            }

            // Add Data Rows
            int rowIdx = 1;
            for (String[] rowData : data) {
                Row row = sheet.createRow(rowIdx++);
                for (int colIdx = 0; colIdx < rowData.length; colIdx++) {
                    row.createCell(colIdx).setCellValue(rowData[colIdx]);
                }
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return null;
		}
    }
    
    
    
    

    @Override
    public ByteArrayInputStream downloadProfitabilityReport(
            Long storeId,
            Long supplierId,
            String transactionNumber,
            TransactionStatus transactionStatus,
            Date fromDate,
            Date toDate,
            TransactionType transactionType
    ) {
        List<TradeTransaction> transactionList = transactionService.searchTransaction(
                storeId, supplierId, transactionNumber, transactionStatus, fromDate, toDate, transactionType
        );

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Profitability Report");

            // Create Header Row
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                    "Transaction Number", "Store", "Partner", "Processed By",
                    "Total Amount", "Discount Amount", "Charge Amount", "Transaction Date",
                    "Transaction Status", "Transaction Type", "Remark"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
            }

            // Populate Data Rows
            int rowIdx = 1;
            for (TradeTransaction transaction : transactionList) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(transaction.getTransactionNumber());
                row.createCell(1).setCellValue(transaction.getStore() != null ? transaction.getStore().getStoreName() : "");
                row.createCell(2).setCellValue(transaction.getPartner() != null ? transaction.getPartner().getName() : "");
                row.createCell(3).setCellValue(transaction.getProcessedBy() != null ? transaction.getProcessedBy().getName() : "");
                row.createCell(4).setCellValue(transaction.getTotalAmount() != null ? transaction.getTotalAmount() : 0);
                row.createCell(5).setCellValue(transaction.getDiscountAmount() != null ? transaction.getDiscountAmount() : 0);
                row.createCell(6).setCellValue(transaction.getChargeAmount() != null ? transaction.getChargeAmount() : 0);
                row.createCell(7).setCellValue(transaction.getTransactionDate() != null ? transaction.getTransactionDate().toString() : "");
                row.createCell(8).setCellValue(transaction.getTransactionStatus() != null ? transaction.getTransactionStatus().toString() : "");
                row.createCell(9).setCellValue(transaction.getTransactionType() != null ? transaction.getTransactionType().toString() : "");
                row.createCell(10).setCellValue(transaction.getRemark() != null ? transaction.getRemark() : "");
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel report", e);
        }
    }
}
