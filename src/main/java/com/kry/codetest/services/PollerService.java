package com.kry.codetest.services;

import com.kry.codetest.models.entities.PokeResult;
import com.kry.codetest.models.entities.Service;
import com.kry.codetest.repositories.PokeRepository;
import com.kry.codetest.repositories.ServiceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@AllArgsConstructor
public class PollerService {

    PokeRepository pokeRepository;
    ServiceRepository serviceRepository;
    WebClient client;

    @Scheduled(cron = "*/10 * * * * *")
    public void pollServices() {
        serviceRepository.findAll()
                         .flatMap(this::pokeService)
                         .flatMap(pokeRepository::save)
                         .subscribe();
    }

    private Mono<PokeResult> pokeService(Service service) {
        return client.get()
                     .uri(service.getUri())
                     .exchangeToMono(response -> translateResponse(response, service))
                     .onErrorResume(error -> Mono.just(PokeResult.fail(service.getId())));
    }

    private Mono<PokeResult> translateResponse(ClientResponse response, Service service) {
        return response.statusCode().is2xxSuccessful() ?
                Mono.just(PokeResult.success(service.getId())) : Mono.just(PokeResult.fail(service.getId()));
    }
}
