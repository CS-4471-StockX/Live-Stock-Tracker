package com.stockx.livestocktracker.services;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.Topic;
import com.google.gson.Gson;
import com.stockx.livestocktracker.HistoricalData;
import com.stockx.livestocktracker.HistoricalRetrieval;
import com.stockx.livestocktracker.Stock;
import com.stockx.livestocktracker.SymbolLookupResult;
import com.stockx.livestocktracker.adapters.FinnhubAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class LiveStockTrackerService {

    @Autowired
    private FinnhubAdapter finnhubAdapter;

    @Autowired
    private AmazonSNSClient amazonSNSClient;

    @Autowired
    private HistoricalData historicalData;

    private final Gson gson = new Gson();

    public Stock getStockByTicker(String ticker) {return finnhubAdapter.getStockByTicker(ticker);}

    public SymbolLookupResult findSymbol(String query) {
        return finnhubAdapter.symbolLookup(query);
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

    @Scheduled(cron = "0 * 09-17 * * *")
    private void updateHistoricalMinutes(){
        List<Topic> topicsList = amazonSNSClient.listTopics().getTopics();

        for(Topic topic : topicsList){
            try{
                String stockTicker = topic.getTopicArn().split(":")[5];
                HistoricalRetrieval Retrieval = finnhubAdapter.getStockHistorical(stockTicker, 'm');

                historicalData.setMinutesData(stockTicker, Arrays.asList(Retrieval.getClosingPrices()));
            } catch(Exception e){
                System.err.println(e.getMessage());
            }
        }
    }

    @Scheduled(cron = "0 0 09-17 * * *")
    private void updateHistoricalHours(){
        List<Topic> topicsList = amazonSNSClient.listTopics().getTopics();

        for(Topic topic : topicsList){
            try {
                String stockTicker = topic.getTopicArn().split(":")[5];
                HistoricalRetrieval Retrieval = finnhubAdapter.getStockHistorical(stockTicker, 'h');

                historicalData.setHoursData(stockTicker, Arrays.asList(Retrieval.getClosingPrices()));
            } catch(Exception e){
                System.err.println(e.getMessage());
            }
        }
    }

    @Scheduled(cron = "0 0 10 * * *")
    private void updateHistoricalDays(){
        List<Topic> topicsList = amazonSNSClient.listTopics().getTopics();

        for(Topic topic : topicsList){
            try {
                String stockTicker = topic.getTopicArn().split(":")[5];
                HistoricalRetrieval Retrieval = finnhubAdapter.getStockHistorical(stockTicker, 'd');

                historicalData.setDaysData(stockTicker, Arrays.asList(Retrieval.getClosingPrices()), Arrays.asList(Retrieval.getTimes()));
            } catch(Exception e){
                System.err.println(e.getMessage());
            }
        }
    }
}
