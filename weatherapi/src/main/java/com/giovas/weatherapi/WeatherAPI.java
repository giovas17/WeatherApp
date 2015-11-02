package com.giovas.weatherapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;

import com.giovas.listeners.DownloadListener;
import com.giovas.listeners.NetworkConnectionListener;
import com.giovas.network.NetworkConnection;
import com.giovas.objects.WeatherObject;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

/**
 * Created by DarkGeat on 10/29/2015.
 */
public class WeatherAPI implements DownloadListener{

    private static WeatherAPI INSTANCE = null;
    private static final int hours = 3;
    private static final int SYNC_INTERVAL = hours * 60; // 3 hours
    private static final int SYNC_FLEXTIME = SYNC_INTERVAL / hours;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private int numberOfCalls;
    private ArrayList<WeatherObject> weatherPages;
    private NetworkConnectionListener listener;
    private Context context;

    @IntDef({LOCATION_STATUS_OK, LOCATION_STATUS_SERVER_DOWN, LOCATION_STATUS_SERVER_INVALID,LOCATION_STATUS_UNKNOWN,LOCATION_STATUS_INVALID,STATUS_NO_ZIPCODES})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LocationStatus {}

    public static final int LOCATION_STATUS_OK = 0;
    public static final int LOCATION_STATUS_SERVER_DOWN = 1;
    public static final int LOCATION_STATUS_SERVER_INVALID = 2;
    public static final int LOCATION_STATUS_UNKNOWN = 3;
    public static final int LOCATION_STATUS_INVALID = 4;
    public static final int STATUS_NO_ZIPCODES = 5;


    public WeatherAPI(){
        numberOfCalls = 0;
        weatherPages = new ArrayList<>();
    }

    private synchronized static void createInstance(){
        if (INSTANCE == null){
            INSTANCE = new WeatherAPI();
        }
    }

    private static WeatherAPI getInstance(){
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }

    public static WeatherAPI with(Context context){
        getInstance().setContext(context);
        getInstance().setListener((NetworkConnectionListener)context);
        return getInstance();
    }

    public void getWeatherFrom(ArrayList<String> zipcodes){
        numberOfCalls = zipcodes.size();
        weatherPages = new ArrayList<>();
        for(String zip : zipcodes){
            NetworkConnection net = new NetworkConnection(context,this);
            net.execute(zip);
        }
    }

    public void setListener(NetworkConnectionListener listener) {
        this.listener = listener;
    }

    private void setContext(Context context) {
        this.context = context;
    }

    public static void setLocationStatus(Context context, @LocationStatus int location_status){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(context.getString(R.string.pref_server_status),location_status);
        editor.apply();
    }

    @Override
    public void obtainWeatherObject(WeatherObject weatherObject) {
        weatherPages.add(weatherObject);
        if (weatherPages.size() == numberOfCalls)
            listener.OnLoadFinished(weatherPages);
    }
}
