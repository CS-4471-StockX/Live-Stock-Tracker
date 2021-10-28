package com.stockx.livestocktracker.configurations;

import com.stockx.livestocktracker.HistoricalData;
import com.stockx.livestocktracker.adapters.FinnhubAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public FinnhubAdapter finnhubAdapter() {
        return new FinnhubAdapter();
    }

    @Bean
    public HistoricalData historicalData() {return new HistoricalData();}
}
