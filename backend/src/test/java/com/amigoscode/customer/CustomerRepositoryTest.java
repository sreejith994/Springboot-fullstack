package com.amigoscode.customer;

import com.amigoscode.AbstractTestContainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestContainers {
    @Autowired
    private CustomerRepository underTest;

    @BeforeEach
    void setUp() {
    }

    @Test
    void findByEmail() {
        //Given
        String email = UUID.randomUUID() + "@test.com";
        Customer customer = new Customer("myron", email,31);
        underTest.save(customer);

        //When
        Optional<Customer> result = underTest.findByEmail(email);
        //Then
        assertThat(result).isNotEmpty();
        assertThat(result.get().getEmail()).isEqualTo(email);
        assertThat(result.get().getName()).isEqualTo(customer.getName());
        assertThat(result.get().getAge()).isEqualTo(customer.getAge());
    }
}