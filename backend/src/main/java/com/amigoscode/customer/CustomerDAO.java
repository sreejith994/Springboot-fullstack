package com.amigoscode.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDAO {

    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Long id);
    Optional<Customer> selectCustomerByEmail(String email);
    void insertCustomer(Customer customer);
    void updateCustomer(Customer customer);
    void deleteCustomerById(Long id);

}
