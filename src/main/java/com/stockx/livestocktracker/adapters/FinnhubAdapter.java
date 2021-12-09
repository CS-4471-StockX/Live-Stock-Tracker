package com.stockx.livestocktracker.adapters;

import com.stockx.livestocktracker.Retrieval;
import com.stockx.livestocktracker.Stock;
import com.stockx.livestocktracker.SymbolLookupResult;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
public class FinnhubAdapter {

    private static final String BASE_URL = "https://finnhub.io/api/v1";


    private final RestTemplate restTemplate;
    private final String apiToken1;
    private final String apiToken2;


    public Stock getStockByTicker(String ticker) {
        Stock result = null;
        try {
            result = restTemplate.getForObject(getUrlForQuote(apiToken1, ticker), Stock.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                result = restTemplate.getForObject(getUrlForQuote(apiToken2, ticker), Stock.class);
            }
        }
        return result;
    }

    public SymbolLookupResult symbolLookup(String query) {
        SymbolLookupResult result = null;
        try {
            result = restTemplate.getForObject(getUrlForSearch(apiToken1, query), SymbolLookupResult.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                result = restTemplate.getForObject(getUrlForSearch(apiToken2, query), SymbolLookupResult.class);
            }
        }
        return result;
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

        Retrieval result = null;
        try {
            result = restTemplate.getForObject(getUrlForHistoric(apiToken1, ticker, period, t1, t2), Retrieval.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                result = restTemplate.getForObject(getUrlForHistoric(apiToken2, ticker, period, t1, t2), Retrieval.class);
            }
        }

        return result;
    }

    private String getUrlForQuote(String apiToken, String ticker) {
        return UriComponentsBuilder.fromUriString(BASE_URL).path("/quote")
                .queryParam("symbol", ticker)
                .queryParam("token", apiToken).build().toUriString();
    }

    private String getUrlForSearch(String apiToken, String query) {
        return UriComponentsBuilder.fromUriString(BASE_URL).path("/search")
                .queryParam("q", query)
                .queryParam("token", apiToken).build().toUriString();
    }

    private String getUrlForHistoric(String apiToken, String ticker, String period, String t1, String t2) {
        return UriComponentsBuilder.fromUriString(BASE_URL).path("/stock/candle")
                .queryParam("symbol", ticker)
                .queryParam("resolution", period)
                .queryParam("from", t1)
                .queryParam("to", t2)
                .queryParam("token", apiToken).build().toUriString();
    }


}
