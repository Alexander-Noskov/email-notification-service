package com.example.emailnotification;

import org.springframework.boot.SpringApplication;

public class TestEmailNotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(EmailNotificationServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
