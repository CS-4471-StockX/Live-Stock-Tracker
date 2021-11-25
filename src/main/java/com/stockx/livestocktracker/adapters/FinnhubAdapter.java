package com.stockx.livestocktracker.adapters;

import com.stockx.livestocktracker.Retrieval;
import com.stockx.livestocktracker.Stock;
import com.stockx.livestocktracker.SymbolLookupResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class FinnhubAdapter {

    @Autowired
    private RestTemplate restTemplate;

    private static final String BASE_URL = "https://finnhub.io/api/v1";

    private static final String API_TOKEN_1 = "c5q4mpqad3iaqkuehumg";
    private static final String API_TOKEN_2 = "c6fcue2ad3idclgqa9hg";


    public Stock getStockByTicker(String ticker) {
        Stock result = null;
        try {
            result = restTemplate.getForObject(getUrlForQuote(API_TOKEN_1, ticker), Stock.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                result = restTemplate.getForObject(getUrlForQuote(API_TOKEN_2, ticker), Stock.class);
            }
        }
        return result;
    }

    public SymbolLookupResult symbolLookup(String query) {
        String url = UriComponentsBuilder.fromUriString(BASE_URL).path("/search")
                .queryParam("q", query)
                .queryParam("token", API_TOKEN_2).build().toUriString();

        return restTemplate.getForObject(url, SymbolLookupResult.class);
    }

    //Interval is used by schedules methods in Service file to indicate frequency of returned historical data.
    public Retrieval getStockHistorical(String ticker, char interval) {
        String t1;
        String period;

        if (interval == 'm') {
            t1 = String.valueOf((Instant.ofEpochSecond(0L).until(Instant.now(), ChronoUnit.SECONDS)) - 3660);
            period = "1";
        } else if (interval == 'h') {
            t1 = String.valueOf((Instant.ofEpochSecond(0L).until(Instant.now(), ChronoUnit.SECONDS)) - 86400);
            period = "60";
        } else if (interval == 'd') {
            t1 = String.valueOf((Instant.ofEpochSecond(0L).until(Instant.now(), ChronoUnit.SECONDS)) - 31449600);
            period = "D";
        } else {
            return null;
        }

        String t2 = String.valueOf(Instant.ofEpochSecond(0L).until(Instant.now(), ChronoUnit.SECONDS));
        String url = UriComponentsBuilder.fromUriString(BASE_URL).path("/stock/candle")
                .queryParam("symbol", ticker)
                .queryParam("resolution", period)
                .queryParam("from", t1)
                .queryParam("to", t2)
                .queryParam("token", API_TOKEN_1).build().toUriString();

        return restTemplate.getForObject(url, Retrieval.class);
    }

    private String getUrlForQuote(String apiToken, String ticker) {
        return UriComponentsBuilder.fromUriString(BASE_URL).path("/quote")
                .queryParam("symbol", ticker)
                .queryParam("token", apiToken).build().toUriString();
    }


}
