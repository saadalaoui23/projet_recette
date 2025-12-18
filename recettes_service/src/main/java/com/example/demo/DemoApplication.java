package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
// 1. Scanner les contrôleurs, services, et clients qui sont dehors
@ComponentScan(basePackages = {
		"com.example.demo",
		"com.controllers",
		"com.services",
		"com.clients",
		"com.events",
		"com.exception"
})
// 2. Scanner les Repositories (Indispensable pour RecetteRepository)
@EnableJpaRepositories(basePackages = "com.repositories")
// 3. Scanner les Entités (Indispensable pour corriger l'erreur "Not a managed type")
@EntityScan(basePackages = "com.models")
@EnableFeignClients(basePackages = "com.clients")
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}