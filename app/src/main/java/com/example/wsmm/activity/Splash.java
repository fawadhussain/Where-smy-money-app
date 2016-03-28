package com.example.wsmm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.wsmm.R;

/**
 * Created by abubaker on 19/03/2016.
 */
public class Splash extends AppCompatActivity {

    private static final int SPLASH_TIME = 2 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                startActivity(new Intent(Splash.this, MainActivity.class));

                Splash.this.finish();

            }
        }, SPLASH_TIME);

    }

}
