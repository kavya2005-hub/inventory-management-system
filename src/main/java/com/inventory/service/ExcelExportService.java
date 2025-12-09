package com.inventory.service;

import com.inventory.model.Product;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelExportService {

    public ByteArrayInputStream exportProductsToExcel(List<Product> products) throws IOException {

        String[] columns = {
                "ID", "Product Name", "Model", "Price per Quantity",
                "Quantity", "Total Price", "Status", "Created Date", "Updated Date"
        };

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Products");

            // Header style
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);

            // Header row creation
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data rows
            int rowIdx = 1;
            for (Product product : products) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(product.getId());
                row.createCell(1).setCellValue(product.getProductName());
                row.createCell(2).setCellValue(product.getModel());
                row.createCell(3).setCellValue(product.getPricePerQuantity());
                row.createCell(4).setCellValue(product.getQuantity());
                row.createCell(5).setCellValue(product.getTotalPrice());
                row.createCell(6).setCellValue(product.getStatus());

                row.createCell(7).setCellValue(
                        product.getCreatedDate() != null ? product.getCreatedDate().toString() : ""
                );
                row.createCell(8).setCellValue(
                        product.getUpdatedDate() != null ? product.getUpdatedDate().toString() : ""
                );
            }

            // Auto-size columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
