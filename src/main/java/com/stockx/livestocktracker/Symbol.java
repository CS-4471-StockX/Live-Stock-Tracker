package com.stockx.livestocktracker;

import lombok.Data;

@Data
public class Symbol {
    private String description;
    private String displaySymbol;
    private String symbol;
    private String type;
}
