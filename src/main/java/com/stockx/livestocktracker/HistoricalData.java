package com.stockx.livestocktracker;

import java.util.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.format.*;

public class HistoricalData {

    private Map<String,List<Double>> minutesData = new HashMap<>();
    private Map<String, List<Double>> hoursData = new HashMap<>();
    private Map<String, List<List<Double>>> daysData = new HashMap<>();
    private Map<String, Double[]> dayPrices = new HashMap<>();
    private Map<String, String[]> dayLabels = new HashMap<>();

    private long minutesUpdateTime;
    private long hoursUpdateTime;
    private long daysUpdateTime;
    private ZoneId currentTimeZone = ZoneId.systemDefault();    //Gets system time zone
    private int marketOpenHour = 9;


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

    /* Original code
    public void setDaysData(String ticker, List<Double> daysData, List<Double> timeData) {
        List<List<Double>> dateTimeList = new ArrayList<List<Double>>(2);
        dateTimeList.add(daysData);
        dateTimeList.add(timeData);
        this.daysData.put(ticker, dateTimeList);
        daysUpdateTime = Instant.ofEpochSecond(0L).until(Instant.now(), ChronoUnit.SECONDS);
    }
    */

    public void setDaysData(String ticker, List<Double> daysData, List<Double> timeData) {
        List<List<Double>> dateTimeList = new ArrayList<List<Double>>(2);
        dateTimeList.add(daysData);
        dateTimeList.add(timeData);
        this.daysData.put(ticker, dateTimeList);
        daysUpdateTime = Instant.ofEpochSecond(0L).until(Instant.now(), ChronoUnit.SECONDS);

        Double[] dayPricesArray = new Double[daysData.size()];
        String[] dayLabelsArray = new String[timeData.size()];
        String formattedTime;
        long dayEpochTime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");  //Date with short year

        for (int i = 0; i < daysData.size(); i++) {
            dayPricesArray[i] = daysData.get(i);
        }
        
        for (int i = 0; i < timeData.size(); i++) {
            dayEpochTime = daysData.get(i).longValue();
            ZonedDateTime dayTime = this.getZonedTime(dayEpochTime);
            formattedTime = dayTime.format(formatter);  //Gets the date and time in a readable string
            dayLabelsArray[i] = formattedTime;
        }

        dayPrices.put(ticker, dayPricesArray);
        dayLabels.put(ticker, dayLabelsArray);
    }

    //Sets the timeZone, but shouldn't be needed (Ex. "America/Toronto")
    public Boolean setTimeZone(String inputTimeZone){
        try {
            currentTimeZone = ZoneId.of(inputTimeZone);
        }
        catch(Exception e) {
            //System.out.println("Invalid Zone ID");
            return false;
        }
        return true;
    }

    //Converts Epoch Seconds to a Time Zoned datetime to get a nice string from
    private ZonedDateTime getZonedTime(long unix_seconds) {
        Instant instant = Instant.ofEpochSecond(unix_seconds);   //Converts to Instant
        return instant.atZone(currentTimeZone); //Get time stuff in this time zone
    }

    /* We can hard code in the front end
    public String[] getHourLabels(){
        String[] hourLabels = new String[] {"9am", "10am", "11am", "12pm", "1pm", "2pm", "3pm", "4pm", "5pm"};
        return hourLabels;
    }
     */

    //Converts the current day's hourly data from a list to array
    public Double[] getHourPricesArray(String ticker){
        Double[] prices = new Double[9];    //Hours the exchange is open (9-17)
        long currentEpochTime = Instant.ofEpochSecond(0L).until(Instant.now(), ChronoUnit.SECONDS);

        ZonedDateTime currentTime = this.getZonedTime(currentEpochTime);
        ZonedDateTime latestTime = this.getZonedTime(hoursUpdateTime);
        int currentDay = currentTime.getDayOfYear();
        int latestDay = latestTime.getDayOfYear();
        int hour = latestTime.getHour();

        if (latestDay == currentDay) {  //Checks if the last updated data is for today. If not, the array is full of 0s
            for (int i = 0; i <= (hour - marketOpenHour); i++) {    //Updates indexes till the current time, later times are zero
                prices[i] = hoursData.get(ticker).get(i);
            }
        }
        return prices;
    }

    //Labels the last 60 minutes of data
    public String[] getMinuteLabels() {
        String[] minuteLabels = new String[60];
        String formattedTime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");  //Hour and minutes

        ZonedDateTime latestTime = this.getZonedTime(minutesUpdateTime);

        for (int i = 59; i >= 0; i--) { //Puts the latest data in the last index, first
            formattedTime = latestTime.format(formatter);  //Gets the date and time in a readable string
            minuteLabels[i] = formattedTime;
            latestTime.minusMinutes(1); //Gets an earlier time
        }
        return minuteLabels;
    }

    //Stores the price data
    public Double[] getMinutePricesArray(String ticker){
        Double[] prices = new Double[60];

        ZonedDateTime latestTime = this.getZonedTime(minutesUpdateTime);
        int hour = latestTime.getHour();
        int minute = latestTime.getMinute();

        if (hour >= marketOpenHour + 1 && hour <= 17) { //Gets the full hour's data from 10am to 5pm
            if (hour != 17 || minute == 0){ //False if the time later than 5pm
                for (int i = 0; i < 60; i++) {
                    prices[i] = minutesData.get(ticker).get(i);
                }
            }
        } else if (hour == marketOpenHour) { //If the hour is on first hour
            for (int i = prices.length - 1; i >= minute; i--) { //Gets only the amount of data that in list (30 items if 9:30 am).
                prices[i] = minutesData.get(ticker).get(i); //Gets the latest price and puts that in the last index. Fills last indexes and leaves rest empty
            }
        }
        return prices;
    }

    //Combines Day arrays
    public Object[] getDayArrays(String ticker) {
        return new Object[]{dayPrices.get(ticker), dayLabels.get(ticker)};
    }

    //Combines Minute arrays
    public Object[] getMinuteArrays(String ticker) {
        return new Object[]{this.getMinutePricesArray(ticker), this.getMinuteLabels()};
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

    public String getTimeZone(){
        return currentTimeZone.toString();
    }
}
