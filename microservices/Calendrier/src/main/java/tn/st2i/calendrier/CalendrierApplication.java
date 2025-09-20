package tn.st2i.calendrier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "tn.st2i.calendrier.client")
@ComponentScan({"tn.st2i.calendrier", "org.springframework.security.config.annotation.web.configuration"})
public class CalendrierApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalendrierApplication.class, args);
    }
}
