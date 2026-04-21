package com.prepai.prepai_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PrepaiBackendApplication {

	public static void main(String[] args) {

		SpringApplication.run(PrepaiBackendApplication.class, args);
	}

}
