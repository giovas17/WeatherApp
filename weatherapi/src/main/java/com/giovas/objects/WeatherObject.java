package com.giovas.objects;

import java.util.ArrayList;

/**
 * Created by DarkGeat on 10/30/2015.
 */
public class WeatherObject {
    private ArrayList<Forecast> weatherList;
    private String cityName;

    public WeatherObject(ArrayList<Forecast> weatherList, String cityName) {
        this.weatherList = weatherList;
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public ArrayList<Forecast> getWeatherList() {
        return weatherList;
    }

    public void setWeatherList(ArrayList<Forecast> weatherList) {
        this.weatherList = weatherList;
    }
}
