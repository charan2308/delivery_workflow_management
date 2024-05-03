package com.project.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.project.demo.model.Delivery;
import com.project.demo.model.DeliveryStatus;
import com.project.demo.model.Customer;
import com.project.demo.model.Warehouse;
import com.project.demo.service.DatabaseFetcher;
import java.util.List;

@Controller
// Controller responsibility: Handling user requests and interacting with models
public class DeliveryController {

    private final DatabaseFetcher databaseFetcher;
    // Dependency injection of DatabaseFetcher via constructor
    // Decoupling: By injecting DatabaseFetcher into DeliveryController,
    // the two classes are decoupled. DeliveryController does not need
    // to know how to create or obtain a DatabaseFetcher instance;
    // it just relies on Spring to provide it.
    @Autowired //dependency injection of DatabaseFetcher
    public DeliveryController(DatabaseFetcher databaseFetcher) {
        this.databaseFetcher = databaseFetcher;
    }

    // Handler to display the index page
    @GetMapping("/")
    public String index(Model model) {
        // Fetch delivery data
        List<Delivery> deliveries = databaseFetcher.fetchData();
        // Add delivery data to the model
        model.addAttribute("deliveries", deliveries);
        // Return the view name
        return "index";
    }

    // Handler to display all customers
    @GetMapping("/view-customers")
    public String viewCustomers(Model model) {
        // Fetch all customers from the database
        List<Customer> customers = databaseFetcher.getAllCustomers();
        // Add customers to the model
        model.addAttribute("customers", customers);
        // Return the view template for displaying all customers
        return "show-customers";
    }

    // Handler to display order history
    @GetMapping("/order-history")
    public String index1(Model model) {
        // Fetch delivery data
        List<Delivery> deliveries = databaseFetcher.fetchData();
        // Add delivery data to the model
        model.addAttribute("deliveries", deliveries);
        // Return the view name
        return "index";
    }

    // High cohesion: Each method focuses on a single aspect of the application
    // Handler to display the delivery status form
    @GetMapping("/delivery")
    public String showDeliveryStatusForm(Model model) {
        // Display the delivery status form without any data
        return "deliver-status";
    }

    // High cohesion: Each method focuses on a single aspect of the application
    // Handler to display the create order form
    @GetMapping("/create-order")
    public String showCreateOrderForm(Model model) {
        // Return the view template for creating a new order
        return "create-order";
    }

    // Handler to process the create order form submission
    @PostMapping("/create-order")
    public String createOrder(@RequestParam("customerId") int customerId,
            @RequestParam("deliveryDate") String deliveryDate,
            @RequestParam("address") String deliveryAddress) {
        // Call the addOrder function from DatabaseFetcher to add the order to the database
        databaseFetcher.addOrder(customerId, deliveryDate, deliveryAddress);
        // Redirect to the delivery status page after successful submission
        return "redirect:/order-history";
    }

    // Handler to display the create customer form
    // High cohesion: Each method focuses on a single aspect of the application
    @GetMapping("/create-customer")
    public String showCreateCustomerForm() {
        // Return the view template for creating a new customer
        return "create-customer";
    }

    // Handler to process the create customer form submission
    @PostMapping("/create-customer")
    public String createCustomer(@RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("contactInfo") String contactInfo) {
        // Call the addCustomer function from DatabaseFetcher to add the new customer to the database
        databaseFetcher.addCustomer(firstName, lastName, contactInfo);
        // Redirect to the view customers page after successful submission
        return "redirect:/view-customers";
    }

    // Handler to process the delivery status check form submission
    @PostMapping("/check-status")
    public String checkDeliveryStatus(@RequestParam("orderId") int orderId, Model model) {
        // Fetch delivery status from the service
        DeliveryStatus deliveryStatus = databaseFetcher.fetchDeliveryStatus(orderId);
        // Add delivery status to the model
        model.addAttribute("deliveryStatus", deliveryStatus);
        // Return the view template for displaying delivery status
        return "deliver-status";
    }

    // Handler to display all warehouses
    @GetMapping("/view-warehouses")
    public String viewWarehouses(Model model) {
        // Fetch all warehouses from the database
        List<Warehouse> warehouses = databaseFetcher.getAllWarehousesFromDB();
        // Add warehouses to the model
        model.addAttribute("warehouses", warehouses);
        // Return the view template for displaying all warehouses
        return "show-warehouses";
    }

    // Handler to display the add new warehouse form
    // High cohesion: Each method focuses on a single aspect of the application
    @GetMapping("/create-warehouse")
    public String showAddWarehouseForm() {
        // Return the view template for adding a new warehouse
        return "add-warehouse";
    }

    // Handler to process the add new warehouse form submission
    @PostMapping("/create-warehouse")
    public String addNewWarehouse(String name, String location, DatabaseFetcher databaseFetcher) {
        // Call the addWarehouse function from DatabaseFetcher to add the new warehouse to the database
        databaseFetcher.addWarehouse(name, location);
        // Redirect to the view warehouses page after successful submission
        return "redirect:/view-warehouses";
    }
}
