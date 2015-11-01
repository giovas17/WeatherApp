package com.giovas.listeners;

import com.giovas.objects.WeatherObject;

import java.util.ArrayList;

/**
 * Created by DarkGeat on 11/1/2015.
 */
public interface NetworkConnectionListener {
    void OnLoadFinished(ArrayList<WeatherObject> pages);
}
