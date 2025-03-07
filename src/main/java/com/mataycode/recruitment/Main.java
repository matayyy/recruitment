package com.mataycode.recruitment;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

//    @Bean
//    CommandLineRunner commandLineRunner(OrderRepository orderRepository) {
//        return args -> {
//            //CODE
//        };
//    }
}
