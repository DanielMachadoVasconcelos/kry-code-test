package com.kry.codetest.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

import static com.kry.codetest.models.entities.Result.FAIL;
import static com.kry.codetest.models.entities.Result.OK;

@Data
@Entity
@Builder
public class PokeResult {

    @Id
    String id;

    String serviceId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime performedAt;

    @Enumerated(EnumType.STRING)
    Result result;

    public static PokeResult success(String serviceId) {
        return PokeResult.builder()
                .serviceId(serviceId)
                .result(OK)
                .build();
    }

    public static PokeResult fail(String serviceId) {
        return PokeResult.builder()
                .serviceId(serviceId)
                .result(FAIL)
                .build();
    }
}
