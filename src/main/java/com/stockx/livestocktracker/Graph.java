package com.stockx.livestocktracker;

import lombok.Data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Graph {

    private Double[] prices;
    private List<String> times;
    private String updatedTime;

    public Graph(Retrieval retrieval, char format){
        this.setPrices(retrieval.getPrices());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm z");

        Date date = new Date(System.currentTimeMillis());
        String dateS = dateFormat.format(date);
        updatedTime = dateS;

        if(format == 'm') {
            dateFormat = new SimpleDateFormat("HH:mm z");
        }
        else if(format == 'h'){
            dateFormat = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm z");
        }
        else{
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }


        List<String> dateTimes = new ArrayList<String>();
        if(retrieval.getTimes() == null){
            return;
        }
        for(long time : retrieval.getTimes()){
            date = new Date(time*1000);
            dateS = dateFormat.format(date);
            dateTimes.add(dateS);
        }

        times = dateTimes;

    }
}
