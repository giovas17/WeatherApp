package com.giovas.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DarkGeat on 10/30/2015.
 */
public class Forecast implements Parcelable {
    private int weatherId;
    private String date;
    private int image;
    private int animation;
    private double max_temp;
    private double min_temp;
    private String shortDesc;
    private String description;
    private double pressure;
    private float humidity;
    private double windSpeed;
    private double windDirection;

    public Forecast() {
        this.weatherId = 0;
        this.date = "";
        this.image = 0;
        this.animation = 0;
        this.max_temp = 0;
        this.min_temp = 0;
        this.shortDesc = "";
        this.description = "";
        this.pressure = 0;
        this.humidity = 0;
        this.windSpeed = 0;
        this.windDirection = 0;
    }

    public Forecast(int weatherId, String date, int image, int animation, double max_temp, double min_temp,
                    String shortDesc, String description, double pressure, float humidity, double windSpeed, double windDirection) {
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

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
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

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    public static Parcelable.Creator<Forecast> CREATOR = new Creator<Forecast>() {
        @Override
        public Forecast createFromParcel(Parcel source) {
            Forecast forecast = new Forecast();
            forecast.setWeatherId(source.readInt());
            forecast.setDate(source.readString());
            forecast.setImage(source.readInt());
            forecast.setAnimation(source.readInt());
            forecast.setMax_temp(source.readDouble());
            forecast.setMin_temp(source.readDouble());
            forecast.setShortDesc(source.readString());
            forecast.setDescription(source.readString());
            forecast.setPressure(source.readDouble());
            forecast.setHumidity(source.readFloat());
            forecast.setWindSpeed(source.readDouble());
            forecast.setWindDirection(source.readDouble());
            return forecast;
        }

        @Override
        public Forecast[] newArray(int size) {
            return new Forecast[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(weatherId);
        dest.writeString(date);
        dest.writeInt(image);
        dest.writeInt(animation);
        dest.writeDouble(max_temp);
        dest.writeDouble(min_temp);
        dest.writeString(shortDesc);
        dest.writeString(description);
        dest.writeDouble(pressure);
        dest.writeFloat(humidity);
        dest.writeDouble(windSpeed);
        dest.writeDouble(windDirection);
    }
}
