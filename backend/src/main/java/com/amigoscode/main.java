package com.amigoscode;

import com.amigoscode.customer.Customer;
import com.amigoscode.customer.CustomerRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class main {


    public static void main(String[] args) {
        SpringApplication.run(main.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository) {
        return args -> {
            Faker faker = new Faker();
            Random random = new Random();
            List<Customer> customers = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                String name = faker.name().fullName(); // Miss Samanta Schmidt
                String email = faker.internet().emailAddress(); // Emory
                Integer age = random.nextInt(16, 99); // Barton

                customers.add(new Customer(name, email, age));
            }

            customerRepository.saveAll(customers);
        };
    }





}
