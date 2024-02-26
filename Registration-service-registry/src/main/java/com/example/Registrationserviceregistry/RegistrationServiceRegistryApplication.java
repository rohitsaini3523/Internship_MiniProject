package com.example.Registrationserviceregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class RegistrationServiceRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegistrationServiceRegistryApplication.class, args);
	}

}
