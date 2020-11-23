package com.example.mymunicipality;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class BottomNavigationHandler extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "BottomActivity";
    final ReportsFragment fragment2 = new ReportsFragment();
    final Fragment fragment1 = new AppointmentsFragment();
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
        setContentView(R.layout.bottom_navigation_handler);

        fragmentManager.beginTransaction().add(R.id.fragmentcontainer, fragment1, "1").hide(fragment1).commit();
        fragmentManager.beginTransaction().add(R.id.fragmentcontainer, fragment2, "2").hide(fragment2).commit();
        fragmentManager.beginTransaction().add(R.id.fragmentcontainer, fragment3, "3").addToBackStack(null).commit();

        BottomNavigationView navigation = findViewById(R.id.NavView);
        navigation.getMenu().clear(); //clear old inflated items.
        navigation.inflateMenu(R.menu.bott_nav_menu);
        navigation.setSelectedItemId(R.id.action_personal);


        findViewById(R.id.action_appointments).setOnClickListener(this);
        findViewById(R.id.action_report).setOnClickListener(this);
        findViewById(R.id.action_personal).setOnClickListener(this);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setTitle("Area Personale");

    }

    @Override
    public void onBackPressed() {
        
    }

    public void menuEdit(MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }


    @Override
    public void onClick(View view) {
        BottomNavigationView navigation = findViewById(R.id.NavView);
        switch (view.getId()){

            case R.id.action_appointments:
                navigation.setSelectedItemId(R.id.action_appointments);
                getSupportActionBar().setTitle("Appuntamenti");
                fragmentManager.beginTransaction().hide(active).show(fragment1).commit();
                active = fragment1;
                break;

            case R.id.action_report:
                navigation.setSelectedItemId(R.id.action_report);
                getSupportActionBar().setTitle("Segnalazioni");
                fragmentManager.beginTransaction().hide(active).show(fragment2).commit();
                active = fragment2;
                break;

            case R.id.action_personal:
                navigation.setSelectedItemId(R.id.action_personal);
                getSupportActionBar().setTitle("Area Personale");
                fragmentManager.beginTransaction().hide(active).show(fragment3).commit();
                active = fragment3;
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG , "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);

    }

}


