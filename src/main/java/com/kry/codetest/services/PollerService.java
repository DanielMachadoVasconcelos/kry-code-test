package com.kry.codetest.services;

import com.kry.codetest.models.PokeResult;
import com.kry.codetest.models.Service;
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

    public Mono<PokeResult> pollServices(String serviceName) {
        return serviceRepository.findById(serviceName)
                         .flatMap(this::pokeService)
                         .flatMap(pokeRepository::save);
    }

    private Mono<PokeResult> pokeService(Service service) {
        return client.get()
                     .uri(service.getUri())
                     .exchangeToMono(response -> translateResponse(response, service))
                     .onErrorResume(error -> Mono.just(PokeResult.fail(service)));
    }

    private Mono<PokeResult> translateResponse(ClientResponse response, Service service) {
        return response.statusCode().is2xxSuccessful() ?
                Mono.just(PokeResult.success(service)) : Mono.just(PokeResult.fail(service));
    }
}
