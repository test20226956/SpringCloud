package com.neu.SP01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Sp01Application {

	public static void main(String[] args) {
		SpringApplication.run(Sp01Application.class, args);
	}
	
	

}
