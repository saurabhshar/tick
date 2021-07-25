package com.sol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class TickApplication {

	public static void main(String[] args) {
		SpringApplication.run(TickApplication.class, args);
	}

}
