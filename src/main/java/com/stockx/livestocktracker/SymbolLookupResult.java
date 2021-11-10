package com.stockx.livestocktracker;

import lombok.Data;

import java.util.List;

@Data
public class SymbolLookupResult {
    private int count;
    private List<Symbol> result;
}
