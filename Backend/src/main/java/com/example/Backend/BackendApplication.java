package com.example.Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
//	@Bean
//	public TomcatProtocolHandlerCustomizer<?> tomcatProtocolHandlerCustomizer(){
//	return protocolHandler -> {
//		protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
//	};
//	}
}
