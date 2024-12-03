package com.cloth.business.servicesImple;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelServiceImple {

    public ByteArrayInputStream generateExcel(List<String[]> data) throws IOException {
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
        }
    }
}
