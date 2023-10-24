package com.azeez.learning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class CentralConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CentralConfigServerApplication.class, args);
	}

}
