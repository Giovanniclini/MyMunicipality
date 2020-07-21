package com.example.mymunicipality;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class BottomNavigationHandler extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "BottomActivity";
    final ReportsFragment fragment2 = new ReportsFragment();
    final Fragment fragment1 = new Fragment();
    final Fragment fragment3 = new PersonalDataFragment();
    Fragment active = fragment3;

    final FragmentManager fragmentManager = getSupportFragmentManager();

    public static final String TAG_ACTIVITY_FROM = "RegisterActivity";
    public static final String TAG_ACTIVITY_FROM0 = "LoginActivity1";
    public static final String TAG_ACTIVITY_FROM1 = "LoginActivity2";
    public static final String TAG_ACTIVITY_FROM2 = "LoginActivity3";
    public static final String TAG_ACTIVITY_FROM3 = "CheckOnLogin";

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

        String value1 = null;
        String value2 = null;
        String value3 = null;
        String value4 = null;
        String value5 = null;
        if (getIntent().getExtras() != null) {
            value1 = (String) getIntent().getStringExtra(TAG_ACTIVITY_FROM0);
            value2 = (String) getIntent().getStringExtra(TAG_ACTIVITY_FROM1);
            value3 = (String) getIntent().getStringExtra(TAG_ACTIVITY_FROM);
            value4 = (String) getIntent().getStringExtra(TAG_ACTIVITY_FROM2);
            value5 = (String) getIntent().getStringExtra(TAG_ACTIVITY_FROM3);
        }

            //The key argument here must match that used in the other activity

        Log.d(TAG, value1 + value2 + value3 + value4);

        Bundle bundle=new Bundle();
        bundle.putString("name", value1);
        bundle.putString("email", value2);
        bundle.putString("emailDB", value3);
        bundle.putString("emailDB1", value4);
        bundle.putString("emailDB2", value5);
        fragment3.setArguments(bundle);

        //Bundle bundle = new Bundle();
        //bundle.putString("name", value1);
        //bundle.putString("email", value2);
        //PersonalDataFragment fragment = new PersonalDataFragment();
        //fragment.putArguments(bundle);

        fragmentManager.beginTransaction().add(R.id.fragmentcontainer, fragment1, "1").hide(fragment1).commit();
        fragmentManager.beginTransaction().add(R.id.fragmentcontainer, fragment2, "2").hide(fragment2).commit();
        fragmentManager.beginTransaction().add(R.id.fragmentcontainer, fragment3, "3").addToBackStack(null).commit();

        //fragmentManager.beginTransaction().add(R.id.fragmentcontainer,fragment3, "3").commit();

        BottomNavigationView navigation = findViewById(R.id.NavView);
        navigation.getMenu().clear(); //clear old inflated items.
        navigation.inflateMenu(R.menu.bott_nav_menu);
        navigation.setSelectedItemId(R.id.action_personal);

        findViewById(R.id.action_appointments).setOnClickListener(this);
        findViewById(R.id.action_report).setOnClickListener(this);
        findViewById(R.id.action_personal).setOnClickListener(this);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

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
                String value1 = null;
                String value2 = null;
                String value3 = null;
                String value4 = null;
                String value5 = null;
                if (getIntent().getExtras() != null) {
                    value1 = (String) getIntent().getStringExtra(TAG_ACTIVITY_FROM0);
                    value2 = (String) getIntent().getStringExtra(TAG_ACTIVITY_FROM1);
                    value3 = (String) getIntent().getStringExtra(TAG_ACTIVITY_FROM);
                    value4 = (String) getIntent().getStringExtra(TAG_ACTIVITY_FROM2);
                    value5 = (String) getIntent().getStringExtra(TAG_ACTIVITY_FROM3);
                }

                //The key argument here must match that used in the other activity

                Log.d(TAG, value1 + value2 + value3 + value4);

                Bundle bundle=new Bundle();
                bundle.putString("name", value1);
                bundle.putString("email", value2);
                bundle.putString("emailDB", value3);
                bundle.putString("emailDB1", value4);
                bundle.putString("emailDB2", value5);
                fragment3.setArguments(bundle);

                fragmentManager.beginTransaction().hide(active).show(fragment1).commit();
                active = fragment1;
                break;

            case R.id.action_report:
                navigation.setSelectedItemId(R.id.action_report);
                fragmentManager.beginTransaction().hide(active).show(fragment2).commit();
                active = fragment2;
                break;

            case R.id.action_personal:
                navigation.setSelectedItemId(R.id.action_personal);
                fragmentManager.beginTransaction().hide(active).show(fragment3).commit();
                active = fragment3;
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Uri: ", "ciao ciao");
        super.onActivityResult(requestCode, resultCode, data);

    }
}


