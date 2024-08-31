package com.amigoscode.customer;

import com.amigoscode.exception.DuplicateResourceException;
import com.amigoscode.exception.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {

    @Mock
    private CustomerDAO customerDAO;
    private AutoCloseable autoCloseable;
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        customerService = new CustomerService(customerDAO);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getAllCustomers() {
        Mockito.when(customerDAO.selectAllCustomers()).thenReturn(new ArrayList<>());
        List<Customer> customers = customerService.getAllCustomers();
        //Then
        assertNotNull(customers);
        Mockito.verify(customerDAO).selectAllCustomers();
    }

    @Test
    void getCustomer() {
        //Given

        //When
        Mockito.when(customerDAO.selectCustomerById(Mockito.anyLong())).thenReturn(Optional.of(new Customer()));
        Customer customer = customerService.getCustomer(1L);
        //Then
        assertNotNull(customer);
        Mockito.verify(customerDAO).selectCustomerById(Mockito.anyLong());
    }

    @Test
    void getCustomerNoCustomerFound() {
        //Given

        //When
        Mockito.when(customerDAO.selectCustomerById(Mockito.anyLong())).thenReturn(Optional.empty());
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> customerService.getCustomer(1L));
        //Then
        Mockito.verify(customerDAO).selectCustomerById(Mockito.anyLong());
    }

    @Test
    void addCustomer() {
        //Given
        Mockito.when(customerDAO.selectCustomerByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        //When
        CustomerRegistrationRequest request = new CustomerRegistrationRequest("name", "email@test.com", 55);
        customerService.addCustomer(request);
        //Then
        Mockito.verify(customerDAO, Mockito.times(1)).selectCustomerByEmail(Mockito.anyString());

        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);

        Mockito.verify(customerDAO, Mockito.times(1)).insertCustomer(customerCaptor.capture());

        Customer customer = customerCaptor.getValue();
        assertNotNull(customer);
        assertThat(customer.getEmail()).isEqualTo(request.email());
        assertThat(customer.getName()).isEqualTo(request.name() );
        assertThat(customer.getName()).isEqualTo(request.name());

    }

    @Test
    void addCustomerAlreadyExists() {
        //Given
        Mockito.when(customerDAO.selectCustomerByEmail(Mockito.anyString())).thenReturn(Optional.of(new Customer()));

        //When
        assertThatExceptionOfType(DuplicateResourceException.class)
                .isThrownBy(() -> customerService.addCustomer(new CustomerRegistrationRequest("name", "email@test.com", 55)));
        //Then
        Mockito.verify(customerDAO, Mockito.times(1)).selectCustomerByEmail(Mockito.anyString());
        Mockito.verify(customerDAO, Mockito.times(0)).insertCustomer(Mockito.any(Customer.class));
    }

    @Test
    void updateCustomerWhenCustomerDoesNotExists() {
        //Given
        Mockito.when(customerDAO.selectCustomerById(Mockito.anyLong())).thenReturn(Optional.empty());

        //When
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> customerService.updateCustomer(1L, new CustomerRegistrationRequest("name", "email@test.com", 55)));


        //Then
        Mockito.verify(customerDAO, Mockito.times(1)).selectCustomerById(Mockito.anyLong());
        Mockito.verify(customerDAO, Mockito.times(0)).updateCustomer(Mockito.any(Customer.class));

    }

    @Test
    void updateCustomerWhenNoChanges() {
        //Given
        Mockito.when(customerDAO.selectCustomerById(Mockito.anyLong())).thenReturn(Optional.of(new Customer("name", "email@test.com", 55)));

        //When
        assertThatExceptionOfType(DuplicateResourceException.class)
                .isThrownBy(() -> customerService.updateCustomer(1L, new CustomerRegistrationRequest("name", "email@test.com", 55)));


        //Then
        Mockito.verify(customerDAO, Mockito.times(1)).selectCustomerById(Mockito.anyLong());
        Mockito.verify(customerDAO, Mockito.times(0)).updateCustomer(Mockito.any(Customer.class));

    }

    @Test
    void updateCustomer() {
        //Given
        Mockito.when(customerDAO.selectCustomerById(Mockito.anyLong())).thenReturn(Optional.of(new Customer("newName", "email@test.com", 55)));

        //When
        customerService.updateCustomer(1L, new CustomerRegistrationRequest("oldName", "email@test.com", 55));

        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);

        //Then
        Mockito.verify(customerDAO, Mockito.times(1)).selectCustomerById(Mockito.anyLong());
        Mockito.verify(customerDAO, Mockito.times(1)).updateCustomer(customerCaptor.capture());

    }

    @Test
    void deleteCustomerWhenCustomerDoesNotExist() {
        //Given
        Mockito.when(customerDAO.selectCustomerById(Mockito.anyLong())).thenReturn(Optional.empty());

        //When
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> customerService.deleteCustomer(1L));

        //Then
        Mockito.verify(customerDAO, Mockito.times(1)).selectCustomerById(Mockito.anyLong());
        Mockito.verify(customerDAO, Mockito.times(0)).deleteCustomerById(Mockito.anyLong());
    }

    @Test
    void deleteCustomer() {
        //Given
        Mockito.when(customerDAO.selectCustomerById(Mockito.anyLong())).thenReturn(Optional.of(new Customer("newName", "email@test.com", 55)));

        //When
        customerService.deleteCustomer(1L);

        //Then
        Mockito.verify(customerDAO, Mockito.times(1)).selectCustomerById(Mockito.anyLong());
        Mockito.verify(customerDAO, Mockito.times(1)).deleteCustomerById(Mockito.anyLong());
    }
}