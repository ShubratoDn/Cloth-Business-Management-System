package com.cloth.business;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ClothBusinessManagementSystemApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ClothBusinessManagementSystemApplication.class, args);
    }

    @Bean
    ModelMapper mapper(){
        return  new ModelMapper();
    }


    @Override
    public void run(String... args) throws Exception {

    }

}
