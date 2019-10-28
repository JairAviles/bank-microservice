package com.wipro.bank.configuration;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket documentation() {
        return new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo())
                    .select()
                    .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                    .paths(paths())
                    .paths(Predicates.not(PathSelectors.regex("/error.*")))
                    .build();
    }

    private Predicate<String> paths() {
        return or(regex("/api/customers.*"), regex("/api/accounts.*"));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Bank - Customer Account Tracker V3")
                .description("Microservices Foundation Certification - Batch 8")
                .version("1.0.0")
                .license("MIT License")
                .licenseUrl("https://choosealicense.com/licenses/mit/")
                .contact(new Contact("Jair Israel Aviles Eusebio", "http://jairaviles.mx", "jair.eusebio@wipro.com"))
                .extensions(Collections.emptyList())
                .build();

    }
}
