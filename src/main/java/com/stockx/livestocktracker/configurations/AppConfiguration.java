package com.stockx.livestocktracker.configurations;

import com.stockx.livestocktracker.HistoricalData;
import com.stockx.livestocktracker.adapters.FinnhubAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfiguration {

    @Value("${finnhub.apiToken1}")
    private String finnhubApiToken1;
    @Value("${finnhub.apiToken1}")
    private String finnhubApiToken2;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public FinnhubAdapter finnhubAdapter(RestTemplate restTemplate) {
        return new FinnhubAdapter(restTemplate, finnhubApiToken1, finnhubApiToken2);
    }

    @Bean
    public HistoricalData historicalData() {return new HistoricalData();}
}
