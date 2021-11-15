package com.stockx.livestocktracker.controllers;

import com.stockx.livestocktracker.Graphs;
import com.stockx.livestocktracker.SymbolLookupResult;
import com.stockx.livestocktracker.services.LiveStockTrackerService;
import com.stockx.livestocktracker.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping("/graphs")
    public Graphs getGraphs(@RequestParam("ticker") String ticker) {
        return liveStockTrackerService.getGraphs(ticker);
    }

    @PutMapping("/update-minutes")
    public void updateMinutes(@RequestParam("ticker") String ticker){
        liveStockTrackerService.updateMinutes(ticker);
    }

    @PutMapping("/update-hours")
    public void updateHours(@RequestParam("ticker") String ticker){
        liveStockTrackerService.updateHours(ticker);
    }

    @PutMapping("/update-days")
    public void updateDays(@RequestParam("ticker") String ticker){
        liveStockTrackerService.updateDays(ticker);
    }

}
