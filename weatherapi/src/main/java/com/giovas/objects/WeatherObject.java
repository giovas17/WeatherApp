package com.giovas.objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DarkGeat on 10/30/2015.
 */
public class WeatherObject implements Parcelable{
    private List<Forecast> weatherList;
    private String cityName;
    private String zipcode;

    public WeatherObject(){
        weatherList = new ArrayList<Forecast>();
        cityName = "";
        zipcode = "";
    }

    public WeatherObject(@Nullable ArrayList<Forecast> weatherList, @Nullable String cityName, String zipcode) {
        this.weatherList = weatherList != null ? weatherList : new ArrayList<Forecast>();
        this.cityName = cityName != null ? cityName : "";
        this.zipcode = zipcode;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<Forecast> getWeatherList() {
        return weatherList;
    }

    public void setWeatherList(ArrayList<Forecast> weatherList) {
        this.weatherList = weatherList;
    }

    public static Parcelable.Creator<WeatherObject> CREATOR = new Creator<WeatherObject>() {
        @Override
        public WeatherObject createFromParcel(Parcel source) {
            WeatherObject object = new WeatherObject();
            object.setCityName(source.readString());
            object.setZipcode(source.readString());
            source.readList(object.getWeatherList(),null);
            return object;
        }

        @Override
        public WeatherObject[] newArray(int size) {
            return new WeatherObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cityName);
        dest.writeString(zipcode);
        dest.writeTypedList(weatherList);
    }
}
