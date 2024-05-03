package com.project.demo;

// Imports for Spring Boot application setup and configuration
import org.springframework.boot.SpringApplication; // Entry point of Spring Boot application
import org.springframework.boot.autoconfigure.SpringBootApplication; // Annotation for enabling Spring Boot auto-configuration
import org.springframework.context.annotation.Bean; // Annotation for defining beans
import org.springframework.core.env.Environment; // Interface for accessing environment properties

// Imports for JDBC database connectivity
import java.sql.Connection; // Interface representing a database connection
import java.sql.DriverManager; // Class for managing JDBC drivers
import java.sql.ResultSet; // Interface representing a result set of a database query
import java.sql.SQLException; // Exception class for database-related errors
import java.sql.Statement; // Interface representing an SQL statement

import com.project.demo.model.Delivery;


import java.util.List;
import java.util.ArrayList;

@SpringBootApplication
public class YourApplication {

    public static void main(String[] args) {
        SpringApplication.run(YourApplication.class, args);
        DatabaseFetcher.getInstance().fetchData(args);
    }

    @Bean
    public DatabaseFetcher databaseFetcher(Environment env) {
        return DatabaseFetcher.getInstance(env);
    }
}

// DatabaseFetcher class implementing Singleton pattern
class DatabaseFetcher {

    private static DatabaseFetcher instance;
    @SuppressWarnings("unused")
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

    // Method to get instance of DatabaseFetcher (Singleton) with default environment
    public static synchronized DatabaseFetcher getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Environment must be provided for initialization");
        }
        return instance;
    }
    // Fetch data from the database and return as a list of objects
    public List<Delivery> fetchDataSample() {
        // Your database fetching logic here

        // Example data
        List<Delivery> deliveries = new ArrayList<>();
        deliveries.add(new Delivery(1, "John Doe", "123 Main St", "2024-04-25"));
        deliveries.add(new Delivery(2, "Jane Smith", "456 Elm St", "2024-04-26"));

        return deliveries;
    }
    
    // Method to fetch data from database
    public void fetchData(String[] args) {
        Environment env = SpringApplication.run(YourApplication.class, args).getBean(Environment.class);
        
        String url = env.getProperty("spring.datasource.url");
        String username = env.getProperty("spring.datasource.username");
        String password = env.getProperty("spring.datasource.password");

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("\n---------------------");
            System.out.println("---------------------");
            System.out.println("Connected to database.\n");

            // Fetch data from the database
            String query = "SELECT * FROM Delivery";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                // Print the fetched data
                while (resultSet.next()) {
                    int deliveryId = resultSet.getInt("delivery_id");
                    String recipientName = resultSet.getString("recipient_name");
                    String address = resultSet.getString("address");
                    String deliveryDate = resultSet.getString("delivery_date");

                    System.out.println("Delivery ID: " + deliveryId);
                    System.out.println("Recipient Name: " + recipientName);
                    System.out.println("Address: " + address);
                    System.out.println("Delivery Date: " + deliveryDate);
                    System.out.println("---------------------");
                }
            } catch (SQLException e) {
                System.err.println("Error executing query: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
        }
    }
}
