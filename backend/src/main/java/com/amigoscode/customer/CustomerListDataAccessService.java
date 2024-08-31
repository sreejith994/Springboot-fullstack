package com.amigoscode.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDAO {
    static List<Customer> customers;

    static {

        customers = new ArrayList<>();
        customers.add(new Customer(1L, "taylor", "email.taylor", 27));
        customers.add(new Customer(2L, "nao", "email.nao", 28));

    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        return customers.stream().filter(c -> c.getId() == id).findFirst();
    }

    @Override
    public Optional<Customer> selectCustomerByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public void insertCustomer(Customer customer) {

    }

    @Override
    public void updateCustomer(Customer customer) {

    }

    @Override
    public void deleteCustomerById(Long id) {

    }
}
