package com.giovas.weatherapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.giovas.adapters.ForecastAdapter;
import com.giovas.objects.EmptyRecyclerView;
import com.giovas.objects.Forecast;
import com.giovas.objects.WeatherObject;
import com.giovas.utils.Utility;
import com.giovas.weatherapi.WeatherAPI;

import java.util.ArrayList;

/**
 * Created by DarkGeat on 10/30/2015.
 */
public class WeatherFragment extends Fragment {

    private WeatherObject weather;
    private TextView zipcode,city;
    private TextView emptyView;
    private EmptyRecyclerView list;
    private ForecastAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weather = new WeatherObject();
        weather.setCityName(getArguments().getString("city"));
        weather.setZipcode(getArguments().getString("zip"));
        weather.setWeatherList(getArguments().<Forecast>getParcelableArrayList("forecast"));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page,null,false);

        zipcode = (TextView) view.findViewById(R.id.zipLabel);
        city = (TextView) view.findViewById(R.id.cityLabel);
        zipcode.setText(getArguments().getString("zip",""));
        city.setText(getArguments().getString("city",""));

        emptyView = (TextView) view.findViewById(R.id.emptyView);
        list = (EmptyRecyclerView) view.findViewById(R.id.list_weather);
        adapter = new ForecastAdapter(getActivity(),weather.getWeatherList());
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setHasFixedSize(true);
        list.setEmptyView(emptyView);
        list.setAdapter(adapter);
        updateEmptyView();
        return view;
    }

    public static Fragment Create(WeatherObject weather) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putString("city", weather.getCityName());
        bundle.putString("zip", weather.getZipcode());
        ArrayList<Forecast> aux = new ArrayList<>();
        for(Forecast item : weather.getWeatherList()){
            aux.add(item);
        }
        if(weather.getWeatherList().size() > 0) {
            bundle.putParcelableArrayList("forecast", aux);
        }else {
            bundle.putParcelableArrayList("forecast", new ArrayList<Forecast>());
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    public void updateEmptyView(){
        if(adapter.getItemCount() == 0) {
            if(null != emptyView){
                int message = R.string.no_info;
                @WeatherAPI.LocationStatus int location = Utility.getLocationStatus(getActivity());
                switch (location){
                    case WeatherAPI.LOCATION_STATUS_SERVER_DOWN:
                        message = R.string.empty_forecast_list_server_down;
                        break;
                    case WeatherAPI.LOCATION_STATUS_SERVER_INVALID:
                        message = R.string.empty_forecast_list_server_error;
                        break;
                    case WeatherAPI.LOCATION_STATUS_INVALID:
                        message = R.string.empty_forecast_list_invalid_location;
                        break;
                    default:
                        if(!Utility.isNetworkAvailable(getActivity())){
                            message = R.string.no_internet;
                            break;
                        }
                }
                emptyView.setText(message);
            }
        }
    }
}
