package br.com.dealership.api_client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ApiClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiClientApplication.class, args);
	}

}