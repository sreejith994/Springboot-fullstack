package com.amigoscode.customer;

import com.amigoscode.exception.DuplicateResourceException;
import com.amigoscode.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerService(@Qualifier("jdbc") CustomerDAO  customerDAO) {
        this.customerDAO = customerDAO;
    }

    public List<Customer> getAllCustomers() {
        return customerDAO.selectAllCustomers();
    }

    public Customer getCustomer(Long id) {
        return customerDAO.selectCustomerById(id).orElseThrow(() -> new ResourceNotFoundException("Customer id: %s not found".formatted(id)));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        if(customerDAO.selectCustomerByEmail(customerRegistrationRequest.email()).isEmpty()){
            customerDAO.insertCustomer(new Customer(customerRegistrationRequest.name(),
                    customerRegistrationRequest.email(),
                    customerRegistrationRequest.age()));
        } else {
            throw new DuplicateResourceException("Customer already exists with email: %s".formatted(customerRegistrationRequest.email()));
        }
    }

    public void updateCustomer(Long id,CustomerRegistrationRequest customerRegistrationRequest) {
        Optional<Customer> customer = customerDAO.selectCustomerById(id);
        if(customer.isEmpty()){
            throw new ResourceNotFoundException("Customer id: %s not found".formatted(id));
        }
        if(customer.get().getName().equals(customerRegistrationRequest.name()) &&
        customer.get().getEmail().equals(customerRegistrationRequest.email()) &&
        customer.get().getAge() == customerRegistrationRequest.age()){
            throw new DuplicateResourceException("");
        }
        customer.get().setName(customerRegistrationRequest.name());
        customer.get().setEmail(customerRegistrationRequest.email());
        customer.get().setAge(customerRegistrationRequest.age());

        customerDAO.updateCustomer(customer.get());
    }

    public void deleteCustomer(Long id) {
        if(customerDAO.selectCustomerById(id).isEmpty()){
            throw new ResourceNotFoundException("Customer id: %s not found".formatted(id));
        }
        customerDAO.deleteCustomerById(id);
    }
}
