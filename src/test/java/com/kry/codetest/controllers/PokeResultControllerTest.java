package com.kry.codetest.controllers;

import com.kry.codetest.models.PokeResult;
import com.kry.codetest.models.Result;
import com.kry.codetest.models.Service;
import com.kry.codetest.repositories.PokeRepository;
import com.kry.codetest.repositories.ServiceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.OffsetDateTime;
import java.util.function.Function;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser(username = "user")
class PokeResultControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    PokeRepository pokeRepository;

    @AfterEach
    void afterAll() {
        serviceRepository.deleteAll().block();
        pokeRepository.deleteAll().block();
    }

    @Test
    public void shouldFetchTheServiceResults() {
        //given:
        var serviceName = "another-amazing-health-service";
        var service = new Service(serviceName, "user", "http://kry.com.se/en", OffsetDateTime.now());
        pokeRepository.save(PokeResult.success(service)).block();

        //when:
        var response = webTestClient.get()
                .uri(String.format("/services/%s/results", serviceName))
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        //then:
        response.expectStatus()
                .is2xxSuccessful()
                .expectBodyList(PokeResult.class)
                .hasSize(1)
                .value(hasItem(allOf(
                        hasProperty("id", notNullValue()),
                        hasProperty("result", is(Result.OK)),
                        hasProperty("service", hasProperty("serviceName", equalTo(service.getServiceName()))))));
    }

    @Test
    public void shouldNotBeAbleToFetchAnotherColleagueServiceResult() {
        //given:
        var serviceName = "yet-another-health-service";
        var service = new Service(serviceName, "colleague", "http://kry.uk/en", OffsetDateTime.now());
        pokeRepository.save(PokeResult.success(service)).block();

        //when:
        var response = webTestClient.get()
                .uri(String.format("/services/%s/results", serviceName))
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        //then:
        response.expectStatus()
                .isOk()
                .expectBodyList(PokeResult.class)
                .value(Function.identity(), empty());
    }

    @Test
    public void shouldImmediatelyPokeTheService() {
        //given:
        var serviceName = "kyr-health-service";
        var expectedUri = "https://unavailalble-url";
        var newService = Service.builder()
                .serviceName(serviceName)
                .uri(expectedUri)
                .createdAt(OffsetDateTime.now())
                .build();

        serviceRepository.save(newService).block();

        //when:
        var response = webTestClient.post()
                .uri(String.format("/services/%s/poke", serviceName))
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        //then:
        response.expectStatus()
                .is2xxSuccessful()
                .expectBody(PokeResult.class)
                .value(pokeResult -> pokeResult.getService().getServiceName(), equalTo(serviceName))
                .value(pokeResult -> pokeResult.getResult(), equalTo(Result.FAIL))
                .value(pokeResult -> pokeResult.getService().getUri(), equalTo(expectedUri));
    }
}