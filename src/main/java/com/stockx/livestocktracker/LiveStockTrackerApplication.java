package com.stockx.livestocktracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LiveStockTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiveStockTrackerApplication.class, args);
	}

}
