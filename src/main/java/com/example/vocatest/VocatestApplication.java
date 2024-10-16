package com.example.vocatest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@OpenAPIDefinition(servers = {@Server(url = "https://vocalist.kro.kr", description = "vocalist api swagger")})
@SpringBootApplication
public class VocatestApplication {

	public static void main(String[] args) {
		SpringApplication.run(VocatestApplication.class, args);
	}

}
