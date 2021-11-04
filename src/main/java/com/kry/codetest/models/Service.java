package com.kry.codetest.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.OffsetDateTime;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "sample")
public class Service {

    @Id
    String serviceName;

    @URL
    String uri;

    @ApiModelProperty(hidden = true)
    @Field(type = FieldType.Date, format = DateFormat.basic_date_time)
    OffsetDateTime createdAt;

}
