package com.kry.codetest.controllers;

import com.kry.codetest.models.entities.PokeResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PokeResultControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void shouldStreamTheUrisResults() {
        webTestClient.get()
                .uri("/services/metrics")
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(PokeResult.class);
    }
}