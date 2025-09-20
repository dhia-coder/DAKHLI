package tn.st2i.programScholaire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "tn.st2i.programScholaire.client")

public class ProgramScholaireApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProgramScholaireApplication.class, args);
	}

}
