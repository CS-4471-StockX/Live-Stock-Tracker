package com.stockx.livestocktracker;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class HistoricalData {

    private Map<String,List<Double>> minutesData = new HashMap<>();
    private Map<String, List<Double>> hoursData = new HashMap<>();
    private Map<String, List<List<Double>>> daysData = new HashMap<>();

    private long minutesUpdateTime;
    private long hoursUpdateTime;
    private long daysUpdateTime;

    public List<Double> getMinutesData(String ticker){
        return minutesData.get(ticker);
    }

    public List<Double> getHoursData(String ticker){
        return hoursData.get(ticker);
    }

    public List<List<Double>> getDaysData(String ticker){
        return daysData.get(ticker);
    }

    public void setMinutesData(String ticker, List<Double> minutesData){
        this.minutesData.put(ticker, minutesData);
        minutesUpdateTime = Instant.ofEpochSecond(0L).until(Instant.now(), ChronoUnit.SECONDS);
    }

    public void setHoursData(String ticker, List<Double> hoursData) {
        this.hoursData.put(ticker, hoursData);
        hoursUpdateTime = Instant.ofEpochSecond(0L).until(Instant.now(), ChronoUnit.SECONDS);
    }

    public void setDaysData(String ticker, List<Double> daysData, List<Double> timeData) {
        List<List<Double>> l = new ArrayList<List<Double>>(2);
        l.add(daysData);
        l.add(timeData);
        this.daysData.put(ticker, l);
        daysUpdateTime = Instant.ofEpochSecond(0L).until(Instant.now(), ChronoUnit.SECONDS);
    }

    public long getMinutesUpdateTime(){
        return minutesUpdateTime;
    }

    public long getHoursUpdateTime() {
        return hoursUpdateTime;
    }

    public long getDaysUpdateTime() {
        return daysUpdateTime;
    }
}
