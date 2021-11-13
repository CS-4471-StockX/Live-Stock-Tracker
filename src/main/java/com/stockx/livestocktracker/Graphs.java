package com.stockx.livestocktracker;

import lombok.Data;

@Data
public class Graphs {

    private Graph minutes;
    private Graph hours;
    private Graph days;

}
