package klu.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.exceptions.CsvValidationException;

import jakarta.servlet.http.HttpServletRequest;
import klu.model.Customer;
import klu.services.CustomerService;
import klu.utilities.CSVParser;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CSVParser csvParser;
    

    @GetMapping("/students")
    public ResponseEntity<List<Customer>> getAllStudents() {
        List<Customer> students = customerService.getAllCustomers();
        return ResponseEntity.ok(students);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) throws CsvValidationException {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please select a file to upload", HttpStatus.BAD_REQUEST);
        }

        try (InputStream inputStream = file.getInputStream()) {
            // Parse the CSV file and get the list of customers
            List<Customer> customers = csvParser.parse(inputStream);
            
            // Register customers using the service
            customerService.registerCustomers(customers);
            
            // Return success message
            return new ResponseEntity<>("Customers registered successfully!", HttpStatus.OK);
        } catch (IOException e) {
            // Return error message
            return new ResponseEntity<>("An error occurred while processing the file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/student/login")
    public ResponseEntity<String> login(@RequestBody Customer customer) {
        Customer authenticatedCustomer = customerService.authenticateUser(customer.getEmail(), customer.getPassword());
        if (authenticatedCustomer != null) {
            return ResponseEntity.ok("Login successful");
        } else {
            return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }
    }
    @PostMapping("/student/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        // Invalidate session if necessary
        if (request.getSession(false) != null) {
            request.getSession().invalidate();
        }
        return ResponseEntity.ok("Logout successful");
    }


}
