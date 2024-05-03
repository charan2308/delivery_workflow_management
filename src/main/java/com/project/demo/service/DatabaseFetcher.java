package com.project.demo.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.project.demo.model.Customer;
import com.project.demo.model.Delivery;
import com.project.demo.model.DeliveryStatus;
import com.project.demo.model.Warehouse;

@Service
// Service pattern : classes are used to implement business logic 
// and facilitate interaction between controller and data access layer.
public class DatabaseFetcher {

    private static DatabaseFetcher instance;

    private final Environment env;

    // Private constructor to prevent instantiation from outside
    private DatabaseFetcher(Environment env) {
        this.env = env;
    }

    // Method to get instance of DatabaseFetcher (Singleton)
    public static synchronized DatabaseFetcher getInstance(Environment env) {
        if (instance == null) {
            instance = new DatabaseFetcher(env);
        }
        return instance;
    }

    // Fetch delivery data from the database
    public List<Delivery> fetchData() {
        List<Delivery> deliveries = new ArrayList<>();
        try {
            String url = env.getProperty("spring.datasource.url");
            String username = env.getProperty("spring.datasource.username");
            String password = env.getProperty("spring.datasource.password");

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM Delivery");
                while (resultSet.next()) {
                    int deliveryId = resultSet.getInt("delivery_id");
                    String recipientName = resultSet.getString("recipient_name");
                    String address = resultSet.getString("address");
                    String deliveryDate = resultSet.getString("delivery_date");
                    deliveries.add(new Delivery(deliveryId, recipientName, address, deliveryDate));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deliveries;
    }

    // Fetch delivery status based on order ID
    public DeliveryStatus fetchDeliveryStatus(int orderId) {
        String status = "Pending"; // Default status
        try (Connection connection = DriverManager.getConnection(
                env.getProperty("spring.datasource.url"),
                env.getProperty("spring.datasource.username"),
                env.getProperty("spring.datasource.password"))) {

            String query = "SELECT status FROM DeliveryStatus WHERE delivery_id = " + orderId;
            try (Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query)) {
                if (resultSet.next()) {
                    status = resultSet.getString("status");
                    return (new DeliveryStatus(orderId, status));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (new DeliveryStatus(orderId, "Order not found"));
    }

    // Add a new customer to the database
    public void addCustomer(String firstName, String lastName, String contactInfo) {
        String fullName = firstName + " " + lastName; // Merge first and last name
        String sql = "INSERT INTO Customer (name, contact_info) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(
                env.getProperty("spring.datasource.url"),
                env.getProperty("spring.datasource.username"),
                env.getProperty("spring.datasource.password"))) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, contactInfo);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Fetch all customers from the database
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        try {
            String url = env.getProperty("spring.datasource.url");
            String username = env.getProperty("spring.datasource.username");
            String password = env.getProperty("spring.datasource.password");

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM Customer");
                while (resultSet.next()) {
                    int custID = resultSet.getInt("customer_id");
                    String custName = resultSet.getString("name");
                    String contactInfo = resultSet.getString("contact_info");
                    customers.add(new Customer(custID, custName, contactInfo));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    // Add a new order to the database
    public void addOrder(int customerId, String deliveryDate, String deliveryAddress) {
        String customerName = ""; // Initialize customerName with an empty string
        try {
            // Your implementation to add the order to the database
            LocalDate currentDate = LocalDate.now();
            String orderDate = currentDate.toString(); //to get current date
            String sql = "INSERT INTO orders (customer_id, order_date, delivery_date) VALUES (?, ?, ?)";
            Connection connection = DriverManager.getConnection(
                    env.getProperty("spring.datasource.url"),   
                    env.getProperty("spring.datasource.username"),
                    env.getProperty("spring.datasource.password"));
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            {
                preparedStatement.setInt(1, customerId);
                preparedStatement.setString(2, orderDate);
                preparedStatement.setString(3, deliveryDate);
                preparedStatement.executeUpdate();
            }

            // Fetch customer name
            String query = "SELECT name FROM customer WHERE customer_id = " + customerId;
            try (Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query)) {
                if (resultSet.next()) {
                    customerName = resultSet.getString("name");
                }
            }

            String sql_for_delivery = "INSERT INTO Delivery (recipient_name, address, delivery_date) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql_for_delivery);
            {
                preparedStatement1.setString(1, customerName);
                preparedStatement1.setString(2, deliveryAddress);
                preparedStatement1.setString(3, deliveryDate);
                preparedStatement1.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Fetch all warehouses from the database
    public List<Warehouse> getAllWarehousesFromDB() {
        List<Warehouse> warehouses = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/delivery",
                    "root",
                    "root"
            );

            String query = "SELECT * FROM Warehouse";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("warehouse_id");
                String name = resultSet.getString("name");
                String location = resultSet.getString("location");

                Warehouse warehouse = new Warehouse(id, name, location);
                warehouses.add(warehouse);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return warehouses;
    }

    // Add a new warehouse to the database
    public void addWarehouse(String name, String location) {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/delivery",
                    "root",
                    "root"
            );

            String query = "INSERT INTO Warehouse (name, location) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, location);
            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
