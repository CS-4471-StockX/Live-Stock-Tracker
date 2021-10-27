package com.stockx.livestocktracker;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class HistoricalRetrieval {

    @JsonAlias("c")
    private Double[] closingPrices;

    @JsonAlias("t")
    private Double[] times;

    public Double[] getClosingPrices(){
        return closingPrices;
    }

    public Double[] getTimes() {
        return times;
    }
}
