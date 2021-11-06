package com.kry.codetest.controllers;

import com.kry.codetest.models.Service;
import com.kry.codetest.repositories.ServiceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.function.Function;

import static com.kry.codetest.configuration.ValidationHandler.ErrorMessage.ErrorDetails;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser(username = ServiceControllerTest.USERNAME)
class ServiceControllerTest {

    public static final String TEST_URI = "https://www.kry.se/en/";
    public static final String USERNAME = "user";

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ServiceRepository repository;

    @AfterEach
    void afterAll() {
        repository.deleteAll();
    }

    @Test
    public void shouldUpdateTheServiceWhenTheServiceAlreadyExists() {
        // given:
        var serviceName = "great-service-poller";
        var expectedNewUri = "https://localhost:443/new-api";
        var newService = Service.builder()
                .serviceName(serviceName)
                .uri(TEST_URI)
                .username(USERNAME)
                .build();

        repository.save(newService).block();

        //when:
        var response = webTestClient.post()
                .uri("/services/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(newService.withUri(expectedNewUri)), Service.class)
                .exchange();

        //then:
        response.expectStatus()
                .is2xxSuccessful()
                .expectBody(Service.class)
                .value(Service::getServiceName, equalTo(serviceName))
                .value(Service::getUri, equalTo(expectedNewUri));
    }

    @Test
    public void shouldNotSaveANewServiceWhenTheUrlIsInvalid() {
        // given:
        var serviceName = "bad-service-poller";
        var newService = Service.builder()
                .serviceName(serviceName)
                .uri("not_a_url")
                .build();

        //when:
        var response = webTestClient.post()
                .uri("/services/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(newService), Service.class)
                .exchange();

        //then:
        response.expectStatus()
                .is4xxClientError()
                .expectBodyList(ErrorDetails.class)
                .hasSize(1)
                .contains(new ErrorDetails("uri", "must be a valid URL"));
    }

    @Test
    public void shouldGetTheServiceWhenRequestingByServiceName() {
        // given:
        var serviceName = "super-nice-health-service";
        var newService = Service.builder()
                .serviceName(serviceName)
                .uri(TEST_URI)
                .createdAt(OffsetDateTime.now())
                .username(USERNAME)
                .build();

        repository.save(newService).block();

        //when:
        var response = webTestClient.get()
                .uri(String.format("/services/%s", serviceName))
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        //then:
        response.expectStatus()
                .isOk()
                .expectBody(Service.class)
                .value(Service::getServiceName, equalTo(serviceName))
                .value(Service::getUri, equalTo(TEST_URI))
                .value(Service::getUsername, equalTo(USERNAME))
                .value(Service::getCreatedAt, notNullValue());
    }

    @Test
    public void shouldDeleteTheServiceByServiceName() {
        //given:
        var serviceName = "service-to-be-deleted";
        var newService = Service.builder()
                .serviceName(serviceName)
                .uri(TEST_URI)
                .createdAt(OffsetDateTime.now())
                .username(USERNAME)
                .build();

        repository.save(newService).block();

        //when:
        var response = webTestClient.delete()
                .uri(String.format("/services/%s", serviceName))
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        //then:
        response.expectStatus()
                .isOk()
                .expectBody(Void.class);

        //and:
        Assertions.assertNull(repository.findById(serviceName).block());
    }

    @Test
    public void shouldGetTheMostRecentlyUriWhenRequestingByServiceName() {
        // given:
        var serviceName = "service-updated-multiple-times";
        var newService = Service.builder()
                .serviceName(serviceName)
                .uri(TEST_URI)
                .createdAt(OffsetDateTime.now())
                .username(USERNAME)
                .build();

        //and:
        var expectedNewUri = "http://new-service-uri:8080";
        repository.save(newService).block();
        repository.save(newService.withUri(expectedNewUri)).block();

        //when:
        var response = webTestClient.get()
                .uri(String.format("/services/%s", serviceName))
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        //then:
        response.expectStatus()
                .isOk()
                .expectBody(Service.class)
                .value(Service::getServiceName, equalTo(serviceName))
                .value(Service::getUri, equalTo(expectedNewUri))
                .value(Service::getCreatedAt, notNullValue());
    }

    @Test
    public void shouldNotBeAbleToFetchAnotherColleagueService() {
        // given:
        var serviceName = "another-colleague-service";
        var colleague = "a-nice-and-funny-colleague";
        var newService = Service.builder()
                .serviceName(serviceName)
                .uri(TEST_URI)
                .createdAt(OffsetDateTime.now())
                .username(colleague)
                .build();

        repository.save(newService).block();

        //when:
        var response = webTestClient.get()
                .uri(String.format("/services/%s", serviceName))
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        //then:
        response.expectStatus()
                .isOk()
                .expectBody(Service.class)
                .value(Function.identity(), nullValue());
    }
}