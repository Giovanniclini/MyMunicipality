package com.example.mymunicipality;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;


public class SplashViewActivity extends AppCompatActivity  {

    public static final int LOGIN_REQUEST = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_view_activity);

/*
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN_REQUEST);
        */


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(SplashViewActivity.this,LoginActivity.class);
                startActivityForResult(i,LOGIN_REQUEST);
            }
        }, 2000);

    }

}

