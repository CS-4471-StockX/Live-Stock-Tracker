package com.stockx.livestocktracker;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class Retrieval {

    @JsonAlias("c")
    private Double[] prices;

    @JsonAlias("t")
    private  long[] times;

}
