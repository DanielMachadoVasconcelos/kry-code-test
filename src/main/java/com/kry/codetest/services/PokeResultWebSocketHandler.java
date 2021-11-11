package com.kry.codetest.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kry.codetest.models.PokeResult;
import com.kry.codetest.repositories.PokeRepository;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Component
public class PokeResultWebSocketHandler implements WebSocketHandler {

    PokeRepository pokeRepository;
    private Flux<Long> intervalFlux;

    @PostConstruct
    private void setup() {
        intervalFlux = Flux.interval(Duration.ofSeconds(2));
    }

    /**
     * Eventually I thought it would be nice to have a websocket transmitting the poke results.
     * Unfortunately It didn't work well. :(
     */
    @Override
    public Mono<Void> handle(WebSocketSession session) {
        var username = (String) session.getAttributes().get("username");
        var serviceName = (String) session.getAttributes().get("serviceName");

        Publisher<WebSocketMessage> publisher =
                intervalFlux.flatMap(it -> pokeRepository.findAllByService_ServiceNameAndService_UsernameOrderByPerformedAt(serviceName, username))
                        .map(this::toJson)
                        .map(session::textMessage);

        return session.send(publisher).name("service-poke-result")
                .and(session.receive().map(WebSocketMessage::getPayloadAsText).log());
    }

    private String toJson(PokeResult pokeResult) {
        try {
            var mapper = new ObjectMapper();
            return mapper.writeValueAsString(pokeResult);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}
