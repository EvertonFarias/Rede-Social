package com.example.inovaTest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class inovaTest {

	public static void main(String[] args) {
		SpringApplication.run(inovaTest.class, args);
	}

}
