package com.example.mymunicipality;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_login);
                findViewById(R.id.buttonLogin).setOnClickListener(this);
                findViewById(R.id.buttonRegistration).setOnClickListener(this);

        }





        @Override
        public void onClick(View v) {

        }
        }