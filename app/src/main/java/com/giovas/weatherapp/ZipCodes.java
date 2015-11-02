package com.giovas.weatherapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;

import com.giovas.adapters.ZipCodeAdapter;
import com.giovas.data.DataBase;
import com.giovas.dialogs.AddZipCodeDialog;
import com.giovas.listeners.ZipCodeListener;

import java.util.ArrayList;

public class ZipCodes extends AppCompatActivity implements ZipCodeListener{

    private RecyclerView list;
    private ZipCodeAdapter zipCodeAdapter;
    private ArrayList<String> items = new ArrayList<>();
    private DataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip_codes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataBase = new DataBase(this);
        items = dataBase.getZipcodes();
        if(items == null || items.size() == 0) {
            items.add("54130MX");
            dataBase.newEntryZipcodes(items.get(0));
        }

        list = (RecyclerView)findViewById(R.id.list_zipcodes);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(true);

        zipCodeAdapter = new ZipCodeAdapter(this,items);
        list.setAdapter(zipCodeAdapter);

        Animation animation = new ScaleAnimation((float) 1.0, (float) 1.0, (float) 0, (float) 1.0);
        animation.setDuration(500);
        LayoutAnimationController controller = new LayoutAnimationController(animation,0.9f);
        list.setLayoutAnimation(controller);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddZipCodeDialog addZipCodeDialog = new AddZipCodeDialog(ZipCodes.this);
                addZipCodeDialog.setListener(ZipCodes.this);
                addZipCodeDialog.show();
            }
        });
    }

    @Override
    public void AddZipCode(String zipcode) {
        items.add(zipcode);
        dataBase.newEntryZipcodes(zipcode);
        zipCodeAdapter.notifyItemInserted(items.size());
    }
}
