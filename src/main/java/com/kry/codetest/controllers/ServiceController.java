package com.kry.codetest.controllers;

import com.kry.codetest.models.entities.Service;
import com.kry.codetest.models.requests.ServiceRequest;
import com.kry.codetest.repositories.ServiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/services")
public class ServiceController {

    ServiceRepository repository;

    @GetMapping("/{serviceName}")
    private Mono<Service> findByServiceName(@PathVariable String serviceName) {
        return repository.findTopByServiceNameOrderByCreatedAt(serviceName);
    }

    @GetMapping("/")
    private Flux<Service> findAllServices() {
        return repository.findAll();
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    private Mono<Service> save(@RequestBody @Valid ServiceRequest request) {
        return repository.save(Service.builder()
                                      .serviceName(request.getServiceName())
                                      .uri(request.getUri())
                                      .build());
    }

    @PutMapping("/{serviceName}")
    @ResponseStatus(HttpStatus.CREATED)
    private Mono<Service> update(@RequestBody @Valid ServiceRequest request) {
        return repository.save(Service.builder()
                                      .serviceName(request.getServiceName())
                                      .uri(request.getUri())
                                      .build());
    }
}
