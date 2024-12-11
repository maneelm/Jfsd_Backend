package klu.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import klu.model.Customer;
import klu.repository.CustomerRepository;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public void registerCustomers(List<Customer> customers) {
        customerRepository.saveAll(customers);
    }
    

    public List<Customer> getAllCustomers() {
    	List<Customer> customers = customerRepository.findAll();
    	 System.out.println("Fetched customers: " + customers);
        return customerRepository.findAll();
    }
    public Customer authenticateUser(String email, String password) {
        Customer customer = customerRepository.findByEmail(email);
        if (customer != null && customer.getPassword().equals(password)) {
            return customer; // User authenticated successfully
        }
        return null; // Authentication failed
    }
}
