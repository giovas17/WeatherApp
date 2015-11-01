package com.giovas.objects;

/**
 * Created by DarkGeat on 10/30/2015.
 */
public class Forecast {
    private int weatherId;
    private String date;
    private int image;
    private int animation;
    private double max_temp;
    private double min_temp;
    private String shortDesc;
    private String description;
    private double pressure;
    private int humidity;
    private double windSpeed;
    private double windDirection;

    public Forecast(int weatherId, String date, int image, int animation, double max_temp, double min_temp,
                    String shortDesc, String description, double pressure, int humidity, double windSpeed, double windDirection) {
        this.weatherId = weatherId;
        this.date = date;
        this.image = image;
        this.animation = animation;
        this.max_temp = max_temp;
        this.min_temp = min_temp;
        this.shortDesc = shortDesc;
        this.description = description;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
    }

    public int getAnimation() {
        return animation;
    }

    public void setAnimation(int animation) {
        this.animation = animation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public double getMax_temp() {
        return max_temp;
    }

    public void setMax_temp(double max_temp) {
        this.max_temp = max_temp;
    }

    public double getMin_temp() {
        return min_temp;
    }

    public void setMin_temp(double min_temp) {
        this.min_temp = min_temp;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(double windDirection) {
        this.windDirection = windDirection;
    }
}
