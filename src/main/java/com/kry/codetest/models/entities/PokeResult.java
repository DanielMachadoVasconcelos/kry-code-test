package com.kry.codetest.models.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
public class PokeResult {

    @Id
    String id;
    String serviceName;

    LocalDateTime performedAt;

    @Enumerated(EnumType.STRING)
    Result Result;

}
