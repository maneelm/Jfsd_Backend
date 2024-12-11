package klu.utilities;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import klu.model.Customer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVParser {

    public List<Customer> parse(InputStream inputStream) throws IOException, CsvValidationException {
        List<Customer> customers = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream))) {
            String[] header = csvReader.readNext(); // Read header
            
            if (header != null) {
                // Example usage: Validate header or log it
                System.out.println("Header: " + String.join(", ", header));
            }

            String[] values;
            while ((values = csvReader.readNext()) != null) {
                Customer customer = new Customer();
                customer.setId(values[0] != null && !values[0].isEmpty() ? Long.parseLong(values[0]) : null);
               // customer.setAddress(values[1]);
                customer.setEmail(values[1]);
                customer.setName(values[2]);
             
                
               // customer.setPhoneNumber(values[4]);
                customer.setPassword(values[3]);
                customers.add(customer);
            }
        }
        return customers;
    }
}
