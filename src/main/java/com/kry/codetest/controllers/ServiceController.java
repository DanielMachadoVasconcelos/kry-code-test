package com.kry.codetest.controllers;

import com.kry.codetest.models.Service;
import com.kry.codetest.repositories.ServiceRepository;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.URL;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

import static java.time.ZoneOffset.UTC;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/services")
public class ServiceController {

    ServiceRepository repository;

    @GetMapping("/{serviceName}")
    @ApiOperation(value = "Find a specific service. Tracked by the poller", response = Service.class)
    private Mono<Service> findByServiceName(@PathVariable String serviceName,
                                            @ApiIgnore @AuthenticationPrincipal User user) {
        return repository.findByServiceNameAndUsername(serviceName, user.getUsername());
    }

    @GetMapping("/")
    @ApiOperation(value = "Find all available services available to the authenticated user. Tracked by the poller", response = Service.class)
    private Flux<Service> findAllServices(@ApiIgnore @AuthenticationPrincipal User user) {
        return repository.findAllByUsername(user.getUsername());
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Include a new service to be track by the poller.", response = Service.class)
    private Mono<Service> save(@RequestBody @Valid Service request,
                               @ApiIgnore @AuthenticationPrincipal User user) {
        return repository.save(request.withUsername(user.getUsername())
                                      .withCreatedAt(OffsetDateTime.now(UTC)));
    }

    @PutMapping("/{serviceName}")
    @ApiOperation(value = "Update the service to be tracked by the poller.", response = Service.class)
    private Mono<Service> update(@PathVariable @NotBlank String serviceName,
                                 @RequestBody @Valid @URL @NotBlank String uri,
                                 @ApiIgnore @AuthenticationPrincipal User user) {
        return repository.findByServiceNameAndUsername(serviceName, user.getUsername())
                .map(service -> service.withUri(uri))
                .flatMap(repository::save);
    }

    @DeleteMapping("/{serviceName}")
    @ApiOperation(value = "Deletes the service being tracked by the poller.")
    private Mono<Void> delete(@PathVariable @NotBlank String serviceName) {
        return repository.deleteById(serviceName);
    }
}
