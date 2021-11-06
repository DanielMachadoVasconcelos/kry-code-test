package com.kry.codetest.repositories;

import com.kry.codetest.models.PokeResult;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PokeRepository extends ReactiveCrudRepository<PokeResult, String> {

    Flux<PokeResult> findAllByService_ServiceNameAndService_UsernameOrderByPerformedAt(String serviceName, String username);
}
