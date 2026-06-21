package com.poc.docker.poc.app.service;

import com.poc.docker.poc.app.model.Customer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CustomerService {

    // Thread-safe in-memory storage
    private final Map<Long, Customer> customerMap = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(101);

    // CREATE
    public Customer createCustomer(Customer customer) {
        long id = idGenerator.getAndIncrement();
        Customer newCustomer = new Customer(id, customer.custName(), customer.email(),customer.location());
        customerMap.put(id, newCustomer);
        return newCustomer;
    }

    // READ ALL
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    // READ ONE
    public Optional<Customer> getCustomerById(Long id) {
        return Optional.ofNullable(customerMap.get(id));
    }

    // UPDATE
    public Optional<Customer> updateCustomer(Long id, Customer updatedCustomer) {
        if (customerMap.containsKey(id)) {
            Customer customer = new Customer(id, updatedCustomer.custName(), updatedCustomer.email(), updatedCustomer.location());
            customerMap.put(id, customer);
            return Optional.of(customer);
        }
        return Optional.empty();
    }

    // DELETE
    public boolean deleteCustomer(Long id) {
        return customerMap.remove(id) != null;
    }
}