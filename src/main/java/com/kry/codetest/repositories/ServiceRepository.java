package com.kry.codetest.repositories;

import com.kry.codetest.models.Service;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ServiceRepository extends ReactiveCrudRepository<Service, String> {

    Mono<Service> findByServiceNameAndUsername(String serviceName, String username);
    Flux<Service> findAllByUsername(String username);
}
