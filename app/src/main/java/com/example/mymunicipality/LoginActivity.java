package com.example.mymunicipality;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

        private CallbackManager mCallbackManager;
        private FirebaseAuth mFirebaseAuth;
        private FirebaseAuth.AuthStateListener authStateListener;
        private TextView textViewUser;
        private LoginButton loginButton;
        private AccessTokenTracker accessTokenTracker;
        private static final String TAG = "FacebookAuthentication";


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_login);
                Button button = new Button(this);
                button = (Button)findViewById(R.id.buttonRegistration);
                button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                                LoginActivity.this.startActivity(myIntent);
                                finish();
                        }
                });
        }
}


