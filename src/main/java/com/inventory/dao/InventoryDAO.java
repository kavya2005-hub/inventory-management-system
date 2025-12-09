package com.inventory.dao;

import com.inventory.model.Inventory;
import java.sql.*;

public class InventoryDAO {

    private Connection connection;

    public InventoryDAO(Connection connection) {
        this.connection = connection;
    }

    public void addProduct(Inventory product) throws SQLException {
        String sql = "INSERT INTO inventory (product_name, price_per_unit, quantity, total_price, status) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, product.getProductName());
        stmt.setDouble(2, product.getPricePerUnit());
        stmt.setInt(3, product.getQuantity());
        stmt.setDouble(4, product.getPricePerUnit() * product.getQuantity());
        stmt.setString(5, product.getStatus());
        stmt.executeUpdate();
        stmt.close();
        System.out.println("Product added successfully!");
    }

    // Optional: add methods for Get, Update, Delete later
}
