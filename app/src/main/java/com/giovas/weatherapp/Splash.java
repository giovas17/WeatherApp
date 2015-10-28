package com.giovas.weatherapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imagen = (ImageView)findViewById(R.id.imageSplash);
        Glide.with(this).load(R.drawable.art_clear_animated).asGif()
                .placeholder(R.drawable.art_clear)
                .into(imagen);
        Thread tr = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(7000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    Intent intent = new Intent(Splash.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        tr.start();
    }
}
