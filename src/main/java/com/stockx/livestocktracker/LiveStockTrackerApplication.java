package com.stockx.livestocktracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(exclude = {
        org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration.class,
        org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration.class,
        org.springframework.cloud.aws.autoconfigure.context.ContextRegionProviderAutoConfiguration.class
})
public class LiveStockTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiveStockTrackerApplication.class, args);
    }

}
