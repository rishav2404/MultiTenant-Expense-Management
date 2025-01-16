package com.grok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ExpenseManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenseManagementApplication.class, args);
	}
}