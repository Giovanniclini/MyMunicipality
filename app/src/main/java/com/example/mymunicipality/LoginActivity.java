package com.example.mymunicipality;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

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
                findViewById(R.id.buttonLogin).setOnClickListener(this);
                findViewById(R.id.buttonRegistration).setOnClickListener(this);

                mFirebaseAuth = FirebaseAuth.getInstance();
                FacebookSdk.sdkInitialize(getApplicationContext());
                textViewUser = findViewById(R.id.textView);
                loginButton = findViewById(R.id.login_button);
                loginButton.setReadPermissions("email", "public profile");
                mCallbackManager = CallbackManager.Factory.create();
                loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                                Log.d(TAG, "onSuccess" + loginResult);
                                handleFacebookToken(loginResult.getAccessToken());
                        }

                        @Override
                        public void onCancel() {
                                Log.d(TAG, "onCancel");
                        }

                        @Override
                        public void onError(FacebookException error) {
                                Log.d(TAG, "onError" + error);

                        }
                });

                authStateListener = new FirebaseAuth.AuthStateListener() {
                        @Override
                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                if(user != null){
                                        updateUI(user);
                                }else{
                                        updateUI(null);
                                }
                        }
                };

                accessTokenTracker = new AccessTokenTracker() {
                        @Override
                        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                                if(currentAccessToken == null){
                                        mFirebaseAuth.signOut();
                                }
                        }
                };

        }

        private void handleFacebookToken(AccessToken token) {
                Log.d(TAG, "handleFacebookToken" + token);
                AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
                mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                        Log.d(TAG, "Sign in with credential: successful");
                                        FirebaseUser user = mFirebaseAuth.getCurrentUser();
                                        updateUI(user);
                                }else{
                                        Log.d(TAG, "Sign in with credential: failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                }
                        }
                });


        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
                mCallbackManager.onActivityResult(requestCode, resultCode, data);
                super.onActivityResult(requestCode, resultCode, data);

        }

        private void updateUI(FirebaseUser user) {
                if(user != null){
                        textViewUser.setText(user.getDisplayName());
                        if(user.getPhotoUrl() != null){
                                String photoUrl = user.getPhotoUrl().toString();
                                 photoUrl = photoUrl + "?type=large";
                         //       Picasso.get().load(photoUrl).into();  per immagine del video su yt


                        }
                }else{
                        textViewUser.setText("");
                }
        }

        @Override
        protected void onStart(){
                super.onStart();
                mFirebaseAuth.addAuthStateListener(authStateListener);
        }

        @Override
        protected void onStop(){
                super.onStop();
                if(authStateListener != null){
                        mFirebaseAuth.removeAuthStateListener(authStateListener);
                }
        }

        @Override
        public void onClick(View v) {

        }
}