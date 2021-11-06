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

import javax.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "sample")
public class Service {

    @Id
    @NotBlank
    @ApiModelProperty(notes = "Name of the service to track", required = true, example = "kry-health-service")
    String serviceName;

    @ApiModelProperty(notes = "User that included the service to be tracked", hidden = true, example = "developer")
    String username;

    @URL
    @NotBlank
    @ApiModelProperty(notes = "The uri to periodically track availability", required = true, example = "https://kry.com.se/en")
    String uri;

    @ApiModelProperty(notes = "Date of Service creation", hidden = true)
    @Field(type = FieldType.Date, format = DateFormat.basic_date_time)
    OffsetDateTime createdAt;

}
