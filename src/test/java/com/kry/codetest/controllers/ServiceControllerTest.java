package com.kry.codetest.controllers;

import com.kry.codetest.models.Service;
import com.kry.codetest.repositories.ServiceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServiceControllerTest {

    public static final String TEST_URI = "https://www.kry.se/en/";

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ServiceRepository repository;

    @Test
    public void shouldSaveANewServiceWhenTheServiceNameAlreadyExists() {
        var serviceName = "great-service-poller";
        var newService = Service.builder()
                .serviceName(serviceName)
                .uri(TEST_URI)
                .build();

        webTestClient.post()
                .uri("/services/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(newService), Service.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void shouldNotSaveANewServiceWhenTheUrlIsInvalid() {
        var serviceName = "bad-service-poller";
        var newService = Service.builder()
                .serviceName(serviceName)
                .uri("not_a_url")
                .build();

        webTestClient.post()
                .uri("/services/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(newService), Service.class)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    public void shouldGetTheServiceWhenRequestingByServiceName() {
        var serviceName = "super-nice-health-service";
        var newService = Service.builder()
                .serviceName(serviceName)
                .uri(TEST_URI)
                .createdAt(OffsetDateTime.now())
                .build();

        repository.save(newService).subscribe();

        webTestClient.get()
                .uri(String.format("/services/%s", serviceName))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Service.class)
                .value(response -> response.getServiceName(), equalTo(serviceName))
                .value(response -> response.getUri(), equalTo(TEST_URI))
                .value(response -> response.getCreatedAt(), notNullValue());
    }

    @Test
    public void shouldDeleteTheServiceByServiceName() {
        var serviceName = "service-to-be-deleted";
        var newService = Service.builder()
                .serviceName(serviceName)
                .uri(TEST_URI)
                .createdAt(OffsetDateTime.now())
                .build();

        repository.save(newService).subscribe();

        webTestClient.delete()
                .uri(String.format("/services/%s", serviceName))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }

    @Test
    public void shouldGetTheMostRecentlyUrlsWhenRequestingAllService() {
        var serviceName = "service-updated-multiple-times";
        var newService = Service.builder()
                .serviceName(serviceName)
                .uri("https://localhost:1111")
                .createdAt(OffsetDateTime.now())
                .build();

        repository.save(newService).subscribe();
        repository.save(newService.withUri(TEST_URI)).subscribe();

        webTestClient.get()
                .uri(String.format("/services/%s", serviceName))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Service.class)
                .value(response -> response.getServiceName(), equalTo(serviceName))
                .value(response -> response.getUri(), equalTo(TEST_URI))
                .value(response -> response.getCreatedAt(), notNullValue());;
    }
}