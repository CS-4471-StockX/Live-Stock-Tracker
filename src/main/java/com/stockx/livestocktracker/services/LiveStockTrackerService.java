package com.stockx.livestocktracker.services;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.Topic;
import com.google.gson.Gson;
import com.stockx.livestocktracker.Stock;
import com.stockx.livestocktracker.adapters.FinnhubAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LiveStockTrackerService {

    @Autowired
    private FinnhubAdapter finnhubAdapter;

    @Autowired
    private AmazonSNSClient amazonSNSClient;

    private final Gson gson = new Gson();

    public Stock getStockByTicker(String ticker) {
        return finnhubAdapter.getStockByTicker(ticker);
    }

    @Scheduled(fixedDelay = 30000)
    private void updateSubscribedStocks() {
        List<Topic> topicsList = amazonSNSClient.listTopics().getTopics();

        for (Topic topic : topicsList) {
            try {
                String stockTicker = topic.getTopicArn().split(":")[5];
                Stock stock = finnhubAdapter.getStockByTicker(stockTicker);

                PublishRequest publishRequest =
                        new PublishRequest(topic.getTopicArn(), gson.toJson(stock));
                amazonSNSClient.publish(publishRequest);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

    }
}
