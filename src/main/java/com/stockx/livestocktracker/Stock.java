package com.stockx.livestocktracker;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class Stock {

    @JsonAlias("c")
    private Double currentPrice;

    @JsonAlias("d")
    private Double priceChange;

    @JsonAlias("dp")
    private Double percentageChange;

    @JsonAlias("h")
    private Double dayHigh;

    @JsonAlias("l")
    private Double dayLow;

    @JsonAlias("o")
    private Double openingPrice;

    @JsonAlias("pc")
    private Double previousClosingPrice;

}