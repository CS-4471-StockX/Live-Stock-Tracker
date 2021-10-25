package com.stockx.livestocktracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LiveStockTrackerService {

    @Autowired
    private RetrievalServiceAdapter retrievalServiceAdapter;

    public Stock getStockByTicker(String ticker) {
        return retrievalServiceAdapter.getStockByTicker(ticker);
    }

    @Scheduled(fixedDelay = 10000)
    private void updateSubscribedStocks(){
        Map<String, Stock> updatedSubscribedStocks = retrievalServiceAdapter.getSubscribedStocks();

        System.out.println("*************************************************************");
        for (var entry : updatedSubscribedStocks.entrySet()) {
            System.out.println(entry.getKey() + "/" + entry.getValue());
        }
        // PUBLISH to SNS

    }
}
