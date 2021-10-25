package com.stockx.livestocktracker;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LiveStockTrackerService {

    @Autowired
    private RetrievalServiceAdapter retrievalServiceAdapter;

    @Autowired
    private AmazonSNSClient amazonSNSClient;

    private Gson gson = new Gson();

    public Stock getStockByTicker(String ticker) {
        return retrievalServiceAdapter.getStockByTicker(ticker);
    }

    @Scheduled(fixedDelay = 30000)
    private void updateSubscribedStocks() {
        Map<String, Stock> updatedSubscribedStocks = retrievalServiceAdapter.getSubscribedStocks();

        for (var entry : updatedSubscribedStocks.entrySet()) {
            PublishRequest publishRequest =
                    new PublishRequest("arn:aws:sns:us-east-2:411676508182:MSFT", gson.toJson(entry.getValue()));
            amazonSNSClient.publish(publishRequest);
        }

    }
}
