package com.domain.certification.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.domain.certification.api"})
@SpringBootApplication
public class CertificationApplication {

    public static void main(String args[]) {
        SpringApplication.run(CertificationApplication.class, args);
    }
}
