package com.poc.git.actions.demo.service;

import com.poc.git.actions.demo.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerServiceTest {

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        // Start with a fresh, empty service instance before each test
        customerService = new CustomerService();
    }

    @Test
    @DisplayName("Should successfully create a customer with auto-incremented ID starting at 101")
    void shouldCreateCustomer() {
        // Given
        Customer inputCustomer = new Customer(null, "Alice Smith", "alice@example.com", "New York");

        // When
        Customer savedCustomer = customerService.createCustomer(inputCustomer);

        // Then
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.custId()).isEqualTo(101L); // First ID should be 101 based on your AtomicLong(101)
        assertThat(savedCustomer.custName()).isEqualTo("Alice Smith");
        assertThat(savedCustomer.email()).isEqualTo("alice@example.com");
        assertThat(savedCustomer.location()).isEqualTo("New York");
    }

    @Test
    @DisplayName("Should retrieve all customers stored in memory")
    void shouldGetAllCustomers() {
        // Given
        customerService.createCustomer(new Customer(null, "Alice", "alice@example.com", "LA"));
        customerService.createCustomer(new Customer(null, "Bob", "bob@example.com", "Chicago"));

        // When
        List<Customer> customers = customerService.getAllCustomers();

        // Then
        assertThat(customers).hasSize(2);
        assertThat(customers).extracting(Customer::custName).containsExactlyInAnyOrder("Alice", "Bob");
    }

    @Test
    @DisplayName("Should find a customer by their ID if they exist")
    void shouldGetCustomerById_WhenCustomerExists() {
        // Given
        Customer saved = customerService.createCustomer(new Customer(null, "Alice", "alice@example.com", "LA"));

        // When
        Optional<Customer> foundCustomer = customerService.getCustomerById(saved.custId());

        // Then
        assertThat(foundCustomer).isPresent();
        assertThat(foundCustomer.get().custName()).isEqualTo("Alice");
    }

    @Test
    @DisplayName("Should return empty Optional when searching for a non-existent ID")
    void shouldReturnEmpty_WhenCustomerDoesNotExist() {
        // When
        Optional<Customer> foundCustomer = customerService.getCustomerById(999L);

        // Then
        assertThat(foundCustomer).isEmpty();
    }

    @Test
    @DisplayName("Should update customer details successfully if the ID exists")
    void shouldUpdateCustomer_WhenCustomerExists() {
        // Given
        Customer original = customerService.createCustomer(new Customer(null, "Alice", "alice@example.com", "LA"));
        Customer updatedDetails = new Customer(null, "Alice Jones", "alice.jones@example.com", "Miami");

        // When
        Optional<Customer> result = customerService.updateCustomer(original.custId(), updatedDetails);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().custId()).isEqualTo(original.custId()); // ID must remain unchanged
        assertThat(result.get().custName()).isEqualTo("Alice Jones");
        assertThat(result.get().email()).isEqualTo("alice.jones@example.com");
        assertThat(result.get().location()).isEqualTo("Miami");
    }

    @Test
    @DisplayName("Should return empty Optional when attempting to update a non-existent customer")
    void shouldReturnEmpty_WhenUpdatingNonExistentCustomer() {
        // Given
        Customer updatedDetails = new Customer(null, "Stranger", "stranger@example.com", "Unknown");

        // When
        Optional<Customer> result = customerService.updateCustomer(999L, updatedDetails);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return true and remove customer when deleting an existing record")
    void shouldDeleteCustomer_WhenCustomerExists() {
        // Given
        Customer saved = customerService.createCustomer(new Customer(null, "Alice", "alice@example.com", "LA"));

        // When
        boolean isDeleted = customerService.deleteCustomer(saved.custId());
        Optional<Customer> searchResult = customerService.getCustomerById(saved.custId());

        // Then
        assertThat(isDeleted).isTrue();
        assertThat(searchResult).isEmpty(); // Verify it's actually missing from the map now
    }

    @Test
    @DisplayName("Should return false when attempting to delete a non-existent customer")
    void shouldReturnFalse_WhenDeletingNonExistentCustomer() {
        // When
        boolean isDeleted = customerService.deleteCustomer(999L);

        // Then
        assertThat(isDeleted).isFalse();
    }
}