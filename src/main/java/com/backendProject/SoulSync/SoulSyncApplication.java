package com.backendProject.SoulSync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class SoulSyncApplication {

	public static void main(String[] args) {
//		  Dotenv dotenv = Dotenv.load();
//        dotenv.entries().forEach(entry ->
//            System.setProperty(entry.getKey(), entry.getValue())
//        );
//		System.out.println("SPRING_MAIL_HOST from env = " + System.getProperty("SPRING_MAIL_HOST"));
		SpringApplication.run(SoulSyncApplication.class, args);
		System.out.println("Running");
	}

}
