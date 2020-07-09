package com.example.mymunicipality;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class GoogleProfile extends AppCompatActivity implements View.OnClickListener {

    final Fragment fragment1 = new Fragment();
    final Fragment fragment2 = new Fragment();
    final Fragment fragment3 = new PersonalDataFragment();

    Fragment active = fragment3;

    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_profile);

        fragmentManager.beginTransaction().add(R.id.fragmentcontainer, fragment3, "3").hide(fragment3).commit();
        fragmentManager.beginTransaction().add(R.id.fragmentcontainer, fragment2, "2").hide(fragment2).commit();
        fragmentManager.beginTransaction().add(R.id.fragmentcontainer,fragment1, "1").commit();

        findViewById(R.id.action_appointments).setOnClickListener(this);
        findViewById(R.id.action_report).setOnClickListener(this);
        findViewById(R.id.action_personal).setOnClickListener(this);

        BottomNavigationView navigation = findViewById(R.id.NavView);
        navigation.getMenu().clear(); //clear old inflated items.
        navigation.inflateMenu(R.menu.bott_nav_menu);
        navigation.setSelectedItemId(R.id.action_personal);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

    }

    public void menuEdit(MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.action_appointments:
                fragmentManager.beginTransaction().hide(active).show(fragment1).commit();
                active = fragment1;
                break;

            case R.id.action_report:
                fragmentManager.beginTransaction().hide(active).show(fragment2).commit();
                active = fragment2;
                break;

            case R.id.action_personal:
                fragmentManager.beginTransaction().hide(active).show(fragment3).commit();
                active = fragment3;
                break;

        }
    }
}