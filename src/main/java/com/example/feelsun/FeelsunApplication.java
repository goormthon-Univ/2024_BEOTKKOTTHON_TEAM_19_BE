package com.example.feelsun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FeelsunApplication {
	public static void main(String[] args) {SpringApplication.run(FeelsunApplication.class, args);}

}
