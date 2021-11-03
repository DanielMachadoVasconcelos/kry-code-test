package com.kry.codetest.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.function.Predicate;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket postsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("kry-code.test")
                .apiInfo(apiInfo())
                .select()
                .paths(postPaths())
                .build();
    }

    private Predicate<String> postPaths() {
        return Predicate.isEqual(regex("/services.*"));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Kry Code Test API")
                .description("Kry Code Assignment")
				.contact(new Contact("Daniel Machado Vasconcelos",
						"https://www.linkedin.com/in/danielmachadovasconcelos/",
						"danielm.vasconcelos@gmail.com"))
                .license("Apache 2.0")
                .version("1.0")
                .build();
    }
}