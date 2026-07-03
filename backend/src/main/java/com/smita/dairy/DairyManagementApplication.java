package com.smita.dairy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;


@SpringBootApplication

public class DairyManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(DairyManagementApplication.class, args);
	}

    @GetMapping
	public String helloworld() {
		return "Hello World!";
	}

}
