package com.BAN.Signature.Electronique;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin("*")
public class SignatureElectroniqueApplication {

	public static void main(String[] args) {
		SpringApplication.run(SignatureElectroniqueApplication.class, args);
	}

}
