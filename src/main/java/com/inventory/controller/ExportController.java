package com.inventory.controller;

import com.inventory.model.Product;
import com.inventory.service.ExcelExportService;
import com.inventory.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/export")
@CrossOrigin("*")  
public class ExportController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ExcelExportService excelExportService;

    @GetMapping("/products/excel")
    public ResponseEntity<InputStreamResource> exportProductsToExcel() throws IOException {
        
        List<Product> products = productService.getAllProducts();

        // Convert to Excel file
        ByteArrayInputStream in = excelExportService.exportProductsToExcel(products);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=products.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                ))
                .body(new InputStreamResource(in));
    }
}
