package com.reznicek.jamie;

import javax.servlet.Filter;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.reznicek.jamie.config.rest.SimpleCORSFilter;

@SpringBootApplication
@ComponentScan(basePackages = { "com.reznicek.jamie" })
@EnableAutoConfiguration
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public Filter requestCORSFilter() {
		return new SimpleCORSFilter();
	}

	@Bean
	public Mapper mapper() {
		return new DozerBeanMapper();
	}
}