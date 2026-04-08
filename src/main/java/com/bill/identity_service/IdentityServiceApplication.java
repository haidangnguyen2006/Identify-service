/* (C)2025 */
package com.bill.identity_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
public class IdentityServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(IdentityServiceApplication.class, args);
    }
}
