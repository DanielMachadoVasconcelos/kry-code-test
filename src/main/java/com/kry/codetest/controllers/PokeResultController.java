package com.kry.codetest.controllers;

import com.kry.codetest.models.entities.PokeResult;
import com.kry.codetest.models.entities.Service;
import com.kry.codetest.repositories.PokeRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
@AllArgsConstructor
@RequestMapping("/services/metrics")
public class PokeResultController {

    PokeRepository repository;

    @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    private Flux<PokeResult> findResultsForAllServices() {
        return repository.findAll();
    }
}
