package com.inventory.controller;

import com.inventory.model.Product;
import com.inventory.repository.ProductRepository;
import com.inventory.service.ProductService;
import com.inventory.service.ExcelExportService;
import com.inventory.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {


    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ExcelExportService excelExportService;
    @Autowired
private NotificationService notificationService;


    // Delete Product
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "Product deleted successfully!";
    }
    // Save product (for add + update)
public Product saveProduct(Product product) {
    return productRepository.save(product);
}



    

    // 🔹 Get all products
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // 🔹 Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Add new product
    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

   @PutMapping("/{id}")
public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product newData) {

    return productService.getProductById(id).map(existing -> {

        existing.setProductCode(newData.getProductCode());
        existing.setProductName(newData.getProductName());
        existing.setModel(newData.getModel());
        existing.setPricePerQuantity(newData.getPricePerQuantity());
        existing.setQuantity(newData.getQuantity());
        existing.setTotalPrice(newData.getPricePerQuantity() * newData.getQuantity());
        existing.setStatus(newData.getStatus());
        existing.setUpdatedDate(java.time.LocalDateTime.now());

        Product updated = productService.saveProduct(existing);

        return ResponseEntity.ok(updated);

    }).orElse(ResponseEntity.notFound().build());
}




    // 🟣 Total products
    @GetMapping("/count")
    public long getTotalProducts() {
        return productRepository.count();
    }

    // 🟣 Low stock (qty < 10)
    @GetMapping("/lowstock")
    public long getLowStockItems() {
        return productRepository.countByQuantityLessThan(10);
    }

    // 🟣 Out of stock (qty = 0)
    @GetMapping("/outofstock")
    public long getOutOfStockItems() {
        return productRepository.countByQuantity(0);
    }

    

    // 🔹 Excel Export
    @GetMapping("/export/excel")
    public ResponseEntity<Resource> exportToExcel() throws IOException {
        List<Product> products = productService.getAllProducts();
        ByteArrayInputStream stream = excelExportService.exportProductsToExcel(products);
        InputStreamResource resource = new InputStreamResource(stream);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=products.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);
    }
// 🟢 Place Order
@PutMapping("/order")
public ResponseEntity<String> placeOrder(@RequestBody Map<String, Object> request) {
    try {
        String productName = request.get("productName").toString();
        String model = request.get("model").toString();
        Object unitObj = request.get("Unit");

        if (unitObj == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("❌ Missing field: Unit");
        }

        int orderQuantity = Integer.parseInt(unitObj.toString());
        Product product = productRepository.findByProductNameAndModel(productName, model);

        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("❌ Product not found: " + productName + " (" + model + ")");
        }

        int currentStock = product.getQuantity();

        // Out of stock
        if (currentStock <= 0) {
            product.setStatus("OUT_OF_STOCK");
            productService.saveProduct(product);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("⚠️ No available stock for " + productName + " (" + model + ")");
        }

        // Low stock
        if (currentStock < orderQuantity) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("⚠️ Insufficient stock! Only " + currentStock + " units available for " + productName + " (" + model + ")");
        }

        // Update stock
        int updatedStock = currentStock - orderQuantity;
        product.setQuantity(updatedStock);
        product.setTotalPrice(product.getPricePerQuantity() * updatedStock);

        if (updatedStock == 0) {
            product.setStatus("OUT_OF_STOCK");
        }

        Product updatedProduct = productService.saveProduct(product);

       if (updatedStock < 4) {
    try {
        notificationService.sendStockAlert(updatedProduct);
    } catch (Exception e) {
        e.printStackTrace();
        System.err.println("❌ Email failed, but order is placed!");
    }
}

        String message = updatedStock == 0
                ? "✅ Order placed successfully! Product now OUT OF STOCK!"
                : "✅ Order placed! Remaining stock: " + updatedStock;

        return ResponseEntity.ok(message);

    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("❌ Error while placing order: " + e.getMessage());
    }
}
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/mail")
    public String sendMail() {
        try {
            notificationService.sendTestMail();
            return "Mail send attempt done!";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Error sending test mail: " + e.getMessage();
        }
    }
}



    
}
