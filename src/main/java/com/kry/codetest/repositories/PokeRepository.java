package com.kry.codetest.repositories;

import com.kry.codetest.models.entities.PokeResult;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface PokeRepository extends R2dbcRepository<PokeResult, String> {

}
