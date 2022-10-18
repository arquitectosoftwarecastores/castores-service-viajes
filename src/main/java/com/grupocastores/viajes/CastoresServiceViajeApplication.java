package com.grupocastores.viajes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EntityScan({ "com.grupocastores.commons.castoresdb", "com.grupocastores.commons.inhouse", "com.grupocastores.commons.oficinas"})
public class CastoresServiceViajeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CastoresServiceViajeApplication.class, args);
    }

}
