package com.giovas.weatherapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.giovas.data.DataBase;
import com.giovas.listeners.NetworkConnectionListener;
import com.giovas.objects.Forecast;
import com.giovas.objects.WeatherObject;
import com.giovas.weatherapi.WeatherAPI;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NetworkConnectionListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ImageView backNavigation;
    private ImageView nextNavigation;
    private ViewPager pageLocations;
    private PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pageLocations = (ViewPager) findViewById(R.id.pagerLocations);
        backNavigation = (ImageView) findViewById(R.id.nav_back);
        nextNavigation = (ImageView) findViewById(R.id.nav_forward);

        backNavigation.setEnabled(false);
        backNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (pageLocations != null) {
                    if (pageLocations.getCurrentItem() > 0) {
                        backNavigation.setEnabled(true);
                        pageLocations.setCurrentItem(pageLocations.getCurrentItem() - 1);
                        if (pageLocations.getCurrentItem() == 0) {
                            backNavigation.setEnabled(false);
                        }
                    } else {
                        backNavigation.setEnabled(false);
                    }

                    if (pageLocations.getCurrentItem() == adapter.getCount() - 1) {
                        nextNavigation.setEnabled(false);
                    } else {
                        nextNavigation.setEnabled(true);
                    }
                }
            }
        });

        backNavigation.setEnabled(false);
        nextNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (pageLocations != null) {
                    pageLocations.setCurrentItem(pageLocations.getCurrentItem() + 1);
                    if (pageLocations.getCurrentItem() == adapter.getCount() - 1) {
                        nextNavigation.setEnabled(false);
                    } else {
                        nextNavigation.setEnabled(true);
                    }

                    if (pageLocations.getCurrentItem() > 0) {
                        backNavigation.setEnabled(true);
                    } else {
                        backNavigation.setEnabled(false);
                    }
                }
            }
        });

        WeatherAPI.with(this).getWeatherFrom(new DataBase(this).getZipcodes());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            Intent intent = new Intent(MainActivity.this,ZipCodes.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void OnLoadFinished(ArrayList<WeatherObject> pages) {
        Log.d(LOG_TAG, "Weather API finished to load the content with " + pages.size() + " pages");
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        private ArrayList<Forecast> forecasts;

        public PagerAdapter(FragmentManager fm, ArrayList<Forecast> forecastArrayList){
            super(fm);
            forecasts = forecastArrayList;
        }

        @Override
        public Fragment getItem(int position) {
            return WeatherFragment.Create(forecasts.get(position));
        }

        @Override
        public int getCount() {
            return forecasts.size();
        }
    }
}
