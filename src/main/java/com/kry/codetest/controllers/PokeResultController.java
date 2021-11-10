package com.kry.codetest.controllers;

import com.kry.codetest.models.PokeResult;
import com.kry.codetest.models.Service;
import com.kry.codetest.repositories.PokeRepository;
import com.kry.codetest.services.PollerService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@RestController
@AllArgsConstructor
@RequestMapping("/services/")
public class PokeResultController {

    PokeRepository repository;
    PollerService pollerService;

    @GetMapping(path = "/{serviceName}/results")
    @ApiOperation(value = "Find the last poller results from a specific service.", response = Service.class)
    private Flux<PokeResult> findResultsByServiceName(@PathVariable @NotBlank String serviceName,
                                                      @RequestParam(defaultValue = "10") @Max(100) Integer limit,
                                                      @ApiIgnore @AuthenticationPrincipal User user) {
        return repository.findAllByService_ServiceNameAndService_UsernameOrderByPerformedAt(serviceName, user.getUsername())
                         .takeLast(limit);
    }

    @PostMapping(path = "/{serviceName}/poke")
    private Mono<PokeResult> pokeServiceUri(@PathVariable @NotBlank String serviceName) {
        return pollerService.pollServices(serviceName);
    }
}
