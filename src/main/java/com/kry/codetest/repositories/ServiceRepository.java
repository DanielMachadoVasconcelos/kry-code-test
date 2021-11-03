package com.kry.codetest.repositories;

import com.kry.codetest.models.entities.Service;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface ServiceRepository extends R2dbcRepository<Service, String> {

    Mono<Service> findTopByServiceNameOrderByCreatedAt(String serviceName);
}
