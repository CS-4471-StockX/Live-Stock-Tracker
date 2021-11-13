package com.stockx.livestocktracker;

import com.stockx.livestocktracker.adapters.FinnhubAdapter;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.*;

public class HistoricalData {

    @Autowired
    private FinnhubAdapter finnhubAdapter;


    private Map<String, Graph> minutesData = new HashMap<>();
    private Map<String, Graph> hoursData = new HashMap<>();
    private Map<String, Graph> daysData = new HashMap<>();


    public Graph getMinutesData(String ticker) {
        if (minutesData.containsKey(ticker)) {
            return minutesData.get(ticker);
        }
        return (new Graph(finnhubAdapter.getStockHistorical(ticker, 'm'), 'm'));
    }

    public Graph getHoursData(String ticker) {
        if (hoursData.containsKey(ticker)) {
            return hoursData.get(ticker);
        }
        return (new Graph(finnhubAdapter.getStockHistorical(ticker, 'h'), 'h'));
    }

    public Graph getDaysData(String ticker) {
        if (daysData.containsKey(ticker)) {
            return daysData.get(ticker);
        }
        return (new Graph(finnhubAdapter.getStockHistorical(ticker, 'd'), 'd'));
    }

    public void updateMinute(String ticker){
        minutesData.put(ticker, new Graph(finnhubAdapter.getStockHistorical(ticker, 'm'), 'm'));
    }

    public void updateHours(String ticker){
        hoursData.put(ticker,  new Graph(finnhubAdapter.getStockHistorical(ticker, 'h'), 'h'));
    }

    public void updateDays(String ticker){
        daysData.put(ticker, new Graph(finnhubAdapter.getStockHistorical(ticker, 'd'), 'd'));
    }

}
