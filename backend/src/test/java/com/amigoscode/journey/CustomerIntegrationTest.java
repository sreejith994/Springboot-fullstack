package com.amigoscode.journey;

import com.amigoscode.customer.Customer;
import com.amigoscode.customer.CustomerRegistrationRequest;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.OptionalLong;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient client;
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void canRegisterCustomer() {
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String email = UUID.randomUUID() + faker.internet().emailAddress();
        int age = faker.number().numberBetween(1, 100);
        //create registration request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(firstName, email, age);
        //send post request
        webTestClient.post()
                .uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request),CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        //get all customer
        List<Customer> allCustomersResponse = webTestClient.get()
                .uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        Customer expected = new Customer(firstName, email, age);
        //test customer is present

        assertThat(allCustomersResponse)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expected);


        //get customer by ID
        OptionalLong id = allCustomersResponse
                .stream()
                        .filter(c -> email.equals(c.getEmail()))
                .mapToLong(c -> c.getId())
                                .findFirst();
        expected.setId(id.getAsLong());

        webTestClient.get()
                .uri("/api/v1/customers/{id}",id.getAsLong())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .isEqualTo(expected);

    }

    @Test
    void canDeleteCustomer() {
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String email = UUID.randomUUID() + faker.internet().emailAddress();
        int age = faker.number().numberBetween(1, 100);
        //create registration request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(firstName, email, age);
        //send post request
        webTestClient.post()
                .uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request),CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        //get all customer
        List<Customer> allCustomersResponse = webTestClient.get()
                .uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        Customer expected = new Customer(firstName, email, age);
        //test customer is present

        assertThat(allCustomersResponse)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expected);


        //delete customer by ID
        OptionalLong id = allCustomersResponse
                .stream()
                .filter(c -> email.equals(c.getEmail()))
                .mapToLong(c -> c.getId())
                .findFirst();
        expected.setId(id.getAsLong());

        webTestClient.delete()
                .uri("/api/v1/customers/{id}",id.getAsLong())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();



        //get all customer after delete
        List<Customer> allCustomersResponseAfterDelete = webTestClient.get()
                .uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        // check deleted customer is not present
        assertThat(allCustomersResponseAfterDelete)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .doesNotContain(expected);
    }

    @Test
    void updateCustomer() {
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String email = UUID.randomUUID() + faker.internet().emailAddress();
        int age = faker.number().numberBetween(1, 100);
        //create registration request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(firstName, email, age);
        //send post request for add customer
        webTestClient.post()
                .uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request),CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        //get all customer
        List<Customer> allCustomersResponse = webTestClient.get()
                .uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        Customer expected = new Customer(firstName, email, age);
        //test customer is present

        assertThat(allCustomersResponse)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expected);

        //get  by ID
        OptionalLong id = allCustomersResponse
                .stream()
                .filter(c -> email.equals(c.getEmail()))
                .mapToLong(c -> c.getId())
                .findFirst();
        expected.setId(id.getAsLong());

        //update customer
        CustomerRegistrationRequest updatedRequest = new CustomerRegistrationRequest("new", email, age);
        webTestClient.put()
                .uri("/api/v1/customers/{id}",id.getAsLong())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedRequest),CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get customer by id

        webTestClient.get()
                .uri("/api/v1/customers/{id}",id.getAsLong())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .isEqualTo(new Customer(id.getAsLong(),"new", email, age));

    }
}
