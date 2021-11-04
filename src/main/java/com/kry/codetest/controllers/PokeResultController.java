package com.kry.codetest.controllers;

import com.kry.codetest.models.PokeResult;
import com.kry.codetest.repositories.PokeRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@AllArgsConstructor
@RequestMapping("/services/")
public class PokeResultController {

    PokeRepository repository;

    @GetMapping(path = "/{serviceName}/results")
    private Flux<PokeResult> findResultsByServiceName(@PathVariable String serviceName) {
        return repository.findAllByService_ServiceName(serviceName);
    }
}
