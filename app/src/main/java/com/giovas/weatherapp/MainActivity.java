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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.giovas.data.DataBase;
import com.giovas.listeners.NetworkConnectionListener;
import com.giovas.objects.Forecast;
import com.giovas.objects.WeatherObject;
import com.giovas.weatherapi.WeatherAPI;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NetworkConnectionListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ArrayList<WeatherObject> list;
    private ImageView backNavigation;
    private ImageView nextNavigation;
    private ViewPager pageLocations;
    private TextView totalPages;
    public static boolean isUpdate = false;
    private PagerAdapter adapter;
    private TextView currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pageLocations = (ViewPager) findViewById(R.id.pagerLocations);
        backNavigation = (ImageView) findViewById(R.id.nav_back);
        nextNavigation = (ImageView) findViewById(R.id.nav_forward);
        currentPage = (TextView) findViewById(R.id.numPages);
        totalPages = (TextView) findViewById(R.id.totalPages);

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

        pageLocations.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position > 0) {
                    backNavigation.setEnabled(true);
                } else {
                    backNavigation.setEnabled(false);
                }

                if (position < list.size() - 1) {
                    nextNavigation.setEnabled(true);
                } else {
                    nextNavigation.setEnabled(false);
                }
                currentPage.setText(String.valueOf(position + 1));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey("list")){
            list = savedInstanceState.getParcelableArrayList("list");
        }else {
            //Just call the WeatherAPI to retieve info from the server and updates the UI
            WeatherAPI.with(this).getWeatherFrom(new DataBase(this).getZipcodes());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("list",list);
        super.onSaveInstanceState(outState);
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
        list = pages;
        totalPages.setText(String.valueOf(pages.size()));
        adapter = new PagerAdapter(getSupportFragmentManager(),list);
        pageLocations.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isUpdate) {
            WeatherAPI.with(this).getWeatherFrom(new DataBase(this).getZipcodes());
            isUpdate = !isUpdate;
        }
        if (list != null && list.size() > 0){
            totalPages.setText(String.valueOf(list.size()));
            adapter = new PagerAdapter(getSupportFragmentManager(),list);
            pageLocations.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        private ArrayList<WeatherObject> forecasts;

        public PagerAdapter(FragmentManager fm, ArrayList<WeatherObject> forecastArrayList){
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
