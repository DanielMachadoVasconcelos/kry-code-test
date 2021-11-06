package com.kry.codetest.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.OffsetDateTime;

import static com.kry.codetest.models.Result.FAIL;
import static com.kry.codetest.models.Result.OK;
import static java.time.ZoneOffset.UTC;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "service-result-latest")
public class PokeResult {

    @Id
    String id;

    @Field(type = FieldType.Object)
    Service service;

    @Field(type = FieldType.Date, format = DateFormat.basic_date_time)
    @ApiModelProperty(notes = "When the request operation was performed")
    OffsetDateTime performedAt;

    @Field(type = FieldType.Keyword)
    @ApiModelProperty(notes = "The result of the request operation", example = "OK, FAIL")
    Result result;

    public static PokeResult success(Service service) {
        return PokeResult.builder()
                .service(service)
                .result(OK)
                .performedAt(OffsetDateTime.now(UTC))
                .build();
    }

    public static PokeResult fail(Service service) {
        return PokeResult.builder()
                .service(service)
                .result(FAIL)
                .performedAt(OffsetDateTime.now(UTC))
                .build();
    }
}
