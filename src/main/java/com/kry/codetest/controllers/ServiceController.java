package com.kry.codetest.controllers;

import com.kry.codetest.models.Service;
import com.kry.codetest.repositories.ServiceRepository;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.http.HttpStatus;
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
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@RestController
@AllArgsConstructor
@RequestMapping("/services")
public class ServiceController {

    ServiceRepository repository;

    @GetMapping("/{serviceName}")
    @ApiOperation(value = "Find a specific service. Tracked by the poller", response = Service.class)
    private Mono<Service> findByServiceName(@PathVariable String serviceName) {
        return repository.findTopByServiceNameOrderByCreatedAt(serviceName);
    }

    @GetMapping("/")
    @ApiOperation(value = "Find all available services. Tracked by the poller", response = Service.class)
    private Flux<Service> findAllServices() {
        return repository.findAll();
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Include a new service to be tracked by the poller.", response = Service.class)
    private Mono<Service> save(@RequestBody @Valid Service request) {
        return repository.save(request.withCreatedAt(OffsetDateTime.now(ZoneOffset.UTC)));
    }

    @PutMapping("/{serviceName}")
    @ApiOperation(value = "Update the service to be tracked by the poller.", response = Service.class)
    private Mono<Service> update(@PathVariable String serviceName,
                                 @RequestBody @Valid @URL String uri) {
        return repository.findById(serviceName)
                .map(service -> service.withUri(uri))
                .flatMap(repository::save);
    }
}
