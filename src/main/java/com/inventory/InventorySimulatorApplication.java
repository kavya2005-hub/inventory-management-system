package com.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class InventorysimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventorysimulatorApplication.class, args);
    }
}
