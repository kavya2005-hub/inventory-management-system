package com.inventory.service;

import com.inventory.model.Product;
import com.inventory.repository.ProductRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ProductRepository productRepository;

    @Value("${minimum.stock.value}")
    private int minimumStock;

    // Prevent duplicate emails for same product
    private Set<Long> mailedProducts = new HashSet<>();


    /***
     * SEND STOCK ALERT EMAIL (Interactive + HTML)
     */
    public void sendStockAlert(Product product) {

        // Avoid sending duplicate mail
        if (mailedProducts.contains(product.getId())) {
            System.out.println("Mail already sent for product: " + product.getProductName());
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo("kavyasakthivel1234@gmail.com");
            helper.setFrom("kavyasakthivel1234@gmail.com");
            helper.setSubject("⚠️ Stock Alert: " + product.getProductName());

            String statusColor = product.getQuantity() == 0 ? "#d00000" : "#ffb703";
            String heading = product.getQuantity() == 0 ? "❌ Out of Stock!" : "🚨 Low Stock Alert!";
            String extraMessage = product.getQuantity() == 0
                    ? "The product is completely OUT OF STOCK. Immediate restocking is required."
                    : "The product is running LOW on stock. Please restock soon.";

            String body = """
                <html>
                <body style='font-family: Arial, sans-serif; background-color:#f4f6f8; padding:25px;'>
                    <div style='max-width:600px; margin:auto; background:#ffffff; padding:20px;
                                border-radius:10px; box-shadow:0 4px 12px rgba(0,0,0,0.1);'>
                        <h2 style='color:%s;'>%s</h2>
                        <p>Hello <b>Admin</b>,</p>
                        <p>The following product requires your attention:</p>
                        <table style='width:100%%; border-collapse:collapse;'>
                            <tr>
                                <td style='padding:8px; font-weight:bold;'>Product Name:</td>
                                <td style='padding:8px;'>%s</td>
                            </tr>
                            <tr>
                                <td style='padding:8px; font-weight:bold;'>Model:</td>
                                <td style='padding:8px;'>%s</td>
                            </tr>
                            <tr>
                                <td style='padding:8px; font-weight:bold;'>Current Quantity:</td>
                                <td style='padding:8px;'>%d</td>
                            </tr>
                        </table>
                        <p style='margin-top:20px; font-size:15px;'>%s</p>
                        <br><br>
                        <p style='font-size:12px; color:gray;'>
                            – Inventory Management System<br>
                            Automated alert sent by your application
                        </p>
                    </div>
                </body>
                </html>
                """.formatted(
                    statusColor,
                    heading,
                    product.getProductName(),
                    product.getModel(),
                    product.getQuantity(),
                    extraMessage
            );

            helper.setText(body, true);
            mailSender.send(message);

            // Mark as mailed
            mailedProducts.add(product.getId());

            System.out.println("📩 Stock alert sent for: " + product.getProductName());

        } catch (MessagingException e) {
            System.err.println("❌ Error sending email: " + e.getMessage());
        }
    }


    /***
     * CLEAR MAIL FLAG WHEN PRODUCT STOCK IS RESTORED
     */
    public void clearMailedFlag(Long productId) {
        mailedProducts.remove(productId);
        System.out.println("🔄 Mail flag cleared for product ID: " + productId);
    }


    /***
     * SCHEDULED TASK — CHECK LOW STOCK EVERY 1 MIN
     */
    @Scheduled(fixedDelay = 60000)
    public void checkLowStockAndSendMail() {

        List<Product> lowStockProducts = productRepository.findAll().stream()
                .filter(p -> p.getQuantity() < minimumStock)
                .toList();

        for (Product product : lowStockProducts) {
            sendStockAlert(product);
        }
    }


    /***
     * SEND TEST MAIL
     */
    public void sendTestMail() {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo("kavyasakthivel1234@gmail.com");
            helper.setFrom("kavyasakthivel1234@gmail.com");
            helper.setSubject("✅ Test Mail - Inventory System");
            helper.setText("<h2>Mail Test Success</h2><p>Your email system is working fine.</p>", true);

            mailSender.send(message);
            System.out.println("📩 Test mail sent successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
