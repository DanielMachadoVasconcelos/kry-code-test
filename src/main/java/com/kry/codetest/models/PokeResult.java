package com.kry.codetest.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static com.kry.codetest.models.Result.FAIL;
import static com.kry.codetest.models.Result.OK;

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
    OffsetDateTime performedAt;

    @Field(type = FieldType.Keyword)
    Result result;

    public static PokeResult success(Service service) {
        return PokeResult.builder()
                .service(service)
                .result(OK)
                .performedAt(OffsetDateTime.now(ZoneOffset.UTC))
                .build();
    }

    public static PokeResult fail(Service service) {
        return PokeResult.builder()
                .service(service)
                .result(FAIL)
                .performedAt(OffsetDateTime.now(ZoneOffset.UTC))
                .build();
    }
}
