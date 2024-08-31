package com.amigoscode.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CustomerJPADataAccessServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    AutoCloseable autoCloseable;
    private CustomerJPADataAccessService underTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        //Given

        //When
        Mockito.when(customerRepository.findAll()).thenReturn(List.of(new Customer()));
        List<Customer> result = underTest.selectAllCustomers();
        //Then
        assertNotNull(result);
        Mockito.verify(customerRepository, Mockito.times(1)).findAll();
    }

    @Test
    void selectCustomerById() {
        //When
        Mockito.when(customerRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Customer()));
        Optional<Customer> result = underTest.selectCustomerById(1L);
        //Then
        assertNotNull(result);
        Mockito.verify(customerRepository, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    void selectCustomerByEmail() {
        //When
        Mockito.when(customerRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(new Customer()));
        Optional<Customer> result = underTest.selectCustomerByEmail("test");
        //Then
        assertNotNull(result);
        Mockito.verify(customerRepository, Mockito.times(1)).findByEmail(Mockito.anyString());
    }

    @Test
    void insertCustomer() {
        underTest.insertCustomer(new Customer());
        //Then
        Mockito.verify(customerRepository, Mockito.times(1)).save(Mockito.any(Customer.class));
    }

    @Test
    void updateCustomer() {
        underTest.updateCustomer(new Customer());
        //Then
        Mockito.verify(customerRepository, Mockito.times(1)).save(Mockito.any(Customer.class));
    }

    @Test
    void deleteCustomerById() {
        Mockito.doNothing().when(customerRepository).deleteById(Mockito.anyLong());
        underTest.deleteCustomerById(1L);
        //Then
        Mockito.verify(customerRepository, Mockito.times(1)).deleteById(Mockito.anyLong());
    }
}