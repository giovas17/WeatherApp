package com.giovas.weatherapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.giovas.objects.Forecast;

/**
 * Created by DarkGeat on 10/30/2015.
 */
public class WeatherFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page,container,false);

        return view;
    }

    public static Fragment Create(Forecast forecast) {
        return null;
    }
}
