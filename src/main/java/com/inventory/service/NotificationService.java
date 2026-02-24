package com.inventory.service;

import com.inventory.model.Product;
import com.inventory.model.Staff;
import com.inventory.repository.StaffRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private StaffRepository staffRepository;

    private final String FROM_EMAIL = "kavyasakthivel1234@gmail.com";

    @Async
    public void sendStockAlert(Product product) {

        try {
            // ✅ Get all ADMIN users
            List<Staff> admins = staffRepository.findByRightsIgnoreCase("ADMIN");

            if (admins == null || admins.isEmpty()) {
                System.out.println("❌ No admin found to send email.");
                return;
            }

            for (Staff admin : admins) {

                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                helper.setFrom(FROM_EMAIL);
                helper.setTo(admin.getEmail());
                helper.setSubject("⚠️ Low Stock Alert: " + product.getProductName());

                String body = "<!DOCTYPE html>"
                        + "<html><body style='font-family:Arial;'>"
                        + "<h2 style='color:#e74c3c;'>⚠️ Low Stock Alert</h2>"
                        + "<p><b>Product Name:</b> " + product.getProductName() + "</p>"
                        + "<p><b>Model:</b> " + product.getModel() + "</p>"
                        + "<p><b>Remaining Quantity:</b> "
                        + "<span style='color:red;font-weight:bold;'>"
                        + product.getQuantity()
                        + "</span></p>"
                        + "<hr>"
                        + "<small>Generated on: "
                        + LocalDateTime.now().format(
                                DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"))
                        + "</small>"
                        + "</body></html>";

                helper.setText(body, true);

                mailSender.send(message);

                System.out.println("✅ Email sent to admin: " + admin.getEmail());
            }

        } catch (Exception e) {
            System.err.println("❌ Failed to send email.");
            e.printStackTrace();
        }
    }
}
