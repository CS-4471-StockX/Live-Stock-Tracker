package com.stockx.livestocktracker.adapters;

import com.stockx.livestocktracker.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class FinnhubAdapter {

    @Autowired
    private RestTemplate restTemplate;

    private static final String BASE_URL = "https://finnhub.io/api/v1";

    public Stock getStockByTicker(String ticker) {
        String url = UriComponentsBuilder.fromUriString(BASE_URL).path("/quote")
                .queryParam("symbol", ticker)
                .queryParam("token", "c5q4mpqad3iaqkuehumg").build().toUriString();

        return restTemplate.getForObject(url, Stock.class);
    }

}
