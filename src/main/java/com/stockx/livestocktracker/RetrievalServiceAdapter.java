package com.stockx.livestocktracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

public class RetrievalServiceAdapter {

    @Autowired
    private RestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8080"; //TODO: Change when pushing to AWS

    public Stock getStockByTicker(String ticker) {
        String url = UriComponentsBuilder.fromUriString(BASE_URL).path("/stock-quote")
                .queryParam("ticker", ticker).build().toUriString();

        return restTemplate.getForObject(url, Stock.class);
    }

    public Map<String, Stock> getSubscribedStocks() {
        String url = UriComponentsBuilder.fromUriString(BASE_URL).path("/subscribed/stock-quotes").build().toUriString();
        ParameterizedTypeReference<HashMap<String, Stock>> responseType = new ParameterizedTypeReference<>() {
        };
        RequestEntity<Void> request = RequestEntity.get(url).accept(MediaType.APPLICATION_JSON).build();

        return restTemplate.exchange(request, responseType).getBody();
    }


}
