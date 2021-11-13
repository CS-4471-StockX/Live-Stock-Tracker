package com.stockx.livestocktracker.services;

import com.stockx.livestocktracker.Graphs;
import com.stockx.livestocktracker.HistoricalData;
import com.stockx.livestocktracker.Stock;
import com.stockx.livestocktracker.SymbolLookupResult;
import com.stockx.livestocktracker.adapters.FinnhubAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LiveStockTrackerService {

    @Autowired
    private FinnhubAdapter finnhubAdapter;

    @Autowired
    private HistoricalData historicalData;

    public Stock getStockByTicker(String ticker) {return finnhubAdapter.getStockByTicker(ticker);}

    public SymbolLookupResult findSymbol(String query) {
        return finnhubAdapter.symbolLookup(query);
    }

    public Graphs getGraphs(String ticker) {

        Graphs graphs = new Graphs();
        graphs.setDays(historicalData.getDaysData(ticker));
        graphs.setHours(historicalData.getHoursData(ticker));
        graphs.setMinutes(historicalData.getMinutesData(ticker));
        return graphs;

    }

    public void updateMinutes(String ticker){
        historicalData.updateMinute(ticker);
    }

    public void updateHours(String ticker){
        historicalData.updateHours(ticker);
    }

    public void updateDays(String ticker){
        historicalData.updateDays(ticker);
    }

}