package com.stockx.livestocktracker.adapters;

import com.stockx.livestocktracker.HistoricalRetrieval;
import com.stockx.livestocktracker.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

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

    //Interval is used by schedules methods in Service file to indicate frequency of returned historical data.
    public HistoricalRetrieval getStockHistorical(String ticker, char interval){
        String t1;
        String period;

        if(interval == 'm'){
            t1 = String.valueOf((Instant.ofEpochSecond(0L).until(Instant.now(), ChronoUnit.SECONDS)) -3600);
            period = "1";
        }
        else if(interval == 'h'){
            t1 = String.valueOf((Instant.ofEpochSecond(0L).until(Instant.now(), ChronoUnit.SECONDS)) -86400);
            period = "60";
        }
        else if(interval == 'd'){
            t1 = String.valueOf((Instant.ofEpochSecond(0L).until(Instant.now(), ChronoUnit.SECONDS)) - 31449600);
            period = "D";
        }
        else{
            return null;
        }

        String t2 = String.valueOf(Instant.ofEpochSecond(0L).until(Instant.now(), ChronoUnit.SECONDS));
        String url = UriComponentsBuilder.fromUriString(BASE_URL).path("/stock/candle")
                .queryParam("symbol", ticker)
                .queryParam("resolution", period)
                .queryParam("from", t1)
                .queryParam("to", t2)
                .queryParam("token", "c5q4mpqad3iaqkuehumg").build().toUriString();

        return restTemplate.getForObject(url, HistoricalRetrieval.class);
    }


}
