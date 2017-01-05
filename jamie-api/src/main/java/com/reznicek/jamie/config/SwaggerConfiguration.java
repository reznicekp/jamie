package com.reznicek.jamie.config;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.not;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.builders.RequestHandlerSelectors.withClassAnnotation;
import static springfox.documentation.builders.RequestHandlerSelectors.withMethodAnnotation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

	@Bean
	@SuppressWarnings("unchecked")
	public Docket phoneserviceApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				// .pathMapping("/jamie")
				.select()
				.apis(and(basePackage("com.reznicek"),
						not(withMethodAnnotation(ApiIgnore.class)),
						not(withClassAnnotation(ApiIgnore.class))))
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Jamie API")
				.build();
	}
}