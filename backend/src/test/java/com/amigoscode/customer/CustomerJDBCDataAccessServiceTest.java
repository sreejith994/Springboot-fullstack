package com.amigoscode.customer;

import com.amigoscode.AbstractTestContainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
class CustomerJDBCDataAccessServiceTest extends AbstractTestContainers {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();


    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(getJdbcTemplate(), customerRowMapper);
    }

    @Test
    void selectAllCustomers() {
        //Given
        Customer customer = new Customer("Milo", UUID.randomUUID()+"@test.com",29);
        underTest.insertCustomer(customer);
        //When
        List<Customer> customers = underTest.selectAllCustomers();
        //Then
        assertThat(customers).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        //Given
        String email = UUID.randomUUID() + "@test.com";
        Customer customer = new Customer("fresh", email,31);
        underTest.insertCustomer(customer);
        Optional<Long> id = underTest.selectAllCustomers()
                .stream()
                .filter(e -> e.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst();
        //When
        Optional<Customer> result = underTest.selectCustomerById(id.get());
        //Then
        assertThat(result).isNotEmpty();
        assertThat(result.get().getEmail()).isEqualTo(email);
        assertThat(result.get().getId()).isEqualTo(id.get());
        assertThat(result.get().getName()).isEqualTo(customer.getName());
        assertThat(result.get().getAge()).isEqualTo(customer.getAge());

    }

    @Test
    void emptyResultWhenSelectCustomerByIdNotFound() {
        //Given
        long id = -1;
        //When
        Optional<Customer> result = underTest.selectCustomerById(id);

        //Then
        assertThat(result).isEmpty();
    }

    @Test
    void selectCustomerByEmail() {
        //Given
        String email = UUID.randomUUID() + "@test.com";
        Customer customer = new Customer("myron", email,31);
        underTest.insertCustomer(customer);

        //When
        Optional<Customer> result = underTest.selectCustomerByEmail(email);
        //Then
        assertThat(result).isNotEmpty();
        assertThat(result.get().getEmail()).isEqualTo(email);
        assertThat(result.get().getName()).isEqualTo(customer.getName());
        assertThat(result.get().getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void insertCustomer() {
        //Given
        String email = UUID.randomUUID() + "@test.com";
        Customer customer = new Customer("chris", email,37);

        //When
        underTest.insertCustomer(customer);

        //Then
        Optional<Customer> result = underTest.selectCustomerByEmail(email);
        assertThat(result).isNotEmpty();
        assertThat(result.get().getEmail()).isEqualTo(email);
        assertThat(result.get().getName()).isEqualTo(customer.getName());
        assertThat(result.get().getAge()).isEqualTo(customer.getAge());

    }

    @Test
    void updateCustomer() {
        //Given
        String email = UUID.randomUUID() + "@test.com";
        Customer customer = new Customer("mo", email,31);
        underTest.insertCustomer(customer);
        Optional<Long> id = underTest.selectAllCustomers()
                .stream()
                .filter(e -> e.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst();
        customer.setName("bills");
        customer.setId(id.get());
        //When
        underTest.updateCustomer(customer);
        Optional<Customer> result = underTest.selectCustomerById(id.get());

        assertThat(result).isNotEmpty();
        assertThat(result.get().getEmail()).isEqualTo(email);
        assertThat(result.get().getName()).isEqualTo(customer.getName());
        assertThat(result.get().getAge()).isEqualTo(customer.getAge());

        //Then

    }

    @Test
    void deleteCustomerById() {
        //Given
        String email = UUID.randomUUID() + "@test.com";
        Customer customer = new Customer("kanye", email,31);
        underTest.insertCustomer(customer);
        Optional<Long> id = underTest.selectAllCustomers()
                .stream()
                .filter(e -> e.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst();
        //When
        underTest.deleteCustomerById(id.get());

        //Then
        Optional<Customer> result = underTest.selectCustomerById(id.get());
        assertThat(result).isEmpty();
    }
}