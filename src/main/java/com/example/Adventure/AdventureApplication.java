package com.example.Adventure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AdventureApplication {
	public static void main(String[] args) {
		SpringApplication.run(AdventureApplication.class, args);
//		String rawPassword = "TaroYamamoto05";
//		String encodedPassword = "$2a$10$Had7DDV1Ne0X7uXcHxBCEubnVxStD66PlpiQkuAI7Su0YyO45ImwG";
//		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//		boolean isMatching = encoder.matches(rawPassword, encodedPassword);
//		System.out.println(isMatching);
	}


}
