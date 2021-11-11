package com.stockx.livestocktracker.controllers;

import com.stockx.livestocktracker.SymbolLookupResult;
import com.stockx.livestocktracker.services.LiveStockTrackerService;
import com.stockx.livestocktracker.Stock;
import com.stockx.livestocktracker.HistoricalData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class LiveStockTrackerController {

    @Autowired
    private LiveStockTrackerService liveStockTrackerService;

    @GetMapping("/stock-quote")
    public Stock getStockByTicker(@RequestParam("ticker") String ticker) {
        return liveStockTrackerService.getStockByTicker(ticker);
    }

    @GetMapping("/find-symbol")
    public SymbolLookupResult findSymbol(@RequestParam("query") String query) {
        return liveStockTrackerService.findSymbol(query);
    }

    /*
    @GetMapping("/stock-day")
    public HistoricalData getDayArrays(@RequestParam("ticker") String ticker) {
        return liveStockTrackerService.getDayArrays(ticker);
    }

    @GetMapping("/stock-hour")
    public HistoricalData getHourPricesArray(@RequestParam("ticker") String ticker) {
        return liveStockTrackerService.getHourPricesArray(ticker);
    }

    @GetMapping("/stock-minute")
    public HistoricalData getMinuteArrays(@RequestParam("ticker") String ticker) {
        return liveStockTrackerService.getMinuteArrays(ticker);
    }

     */
}
