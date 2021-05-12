package com.scoperetail.fusion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@EnableJpaRepositories
public class OrchestratorApplication {

	public static void main(final String[] args) {
		SpringApplication.run(OrchestratorApplication.class, args);
	}
}
