package com.example.mymunicipality;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

        //VARIABILI

        private static final int RC_SIGN_IN = 1;
        private static final String TAG = "LoginActivity" ;
        private static final String AUTH_TYPE = "rerequest";
        GoogleSignInClient mGoogleSignInClient;
        Button verify;
        private static FirebaseAuth mAuth;
        private CallbackManager callbackManager;
        private Button loginButton;

        //METODI


        @Override
        protected void onStart(){
                super.onStart();

                FirebaseUser user = mAuth.getCurrentUser();
                if(user != null){
                        Intent intent = new Intent(getApplicationContext(), BottomNavigationHandler.class);
                        startActivity(intent);
                }

                if (AccessToken.getCurrentAccessToken() != null) {
                        Intent loginIntent = new Intent(getApplicationContext(), BottomNavigationHandler.class);
                        startActivity(loginIntent);
                }

        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_login);



                //FACEBOOK CODE
                callbackManager = CallbackManager.Factory.create();

                loginButton = findViewById(R.id.customFBbutton);
                loginButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                LoginManager.getInstance()
                                        .logInWithReadPermissions(LoginActivity.this, Arrays.asList("email","public_profile"));
                                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                                        @Override
                                        public void onSuccess(LoginResult loginResult) {
                                                handleFacebookAccessToken(loginResult.getAccessToken());
                                                //Intent i = new Intent(getBaseContext(), BottomNavigationHandler.class);
                                                setResult(RESULT_OK);
                                                //startActivity(i);
                                                finish();
                                        }


                                        @Override
                                        public void onCancel() {
                                                setResult(RESULT_CANCELED);
                                                finish();
                                        }

                                        @Override
                                        public void onError(FacebookException error) {

                                        }
                                });
                        }
                });

                //..........

                Button bttn = findViewById(R.id.buttonRegistration);
                bttn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                                LoginActivity.this.startActivity(myIntent);
                        }
                });

                mAuth = FirebaseAuth.getInstance();

                createRequest();

                findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                signIn();
                        }
                });

        }

        private void handleFacebookAccessToken(AccessToken token) {
                Log.d(TAG, "handleFacebookAccessToken:" + token);

                AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                Log.d(TAG, "signInWithCredential:success");
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                updateUI(user);
                                        } else {
                                                // If sign in fails, display a message to the user.
                                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                        Toast.LENGTH_SHORT).show();
                                                updateUI(null);
                                        }

                                        // ...
                                }
                        });
        }

        private void updateUI(FirebaseUser user) {
                if (user != null){
                        Intent i = new Intent(LoginActivity.this, BottomNavigationHandler.class);
                        i.putExtra("name", user.getDisplayName());
                        i.putExtra("email", user.getEmail());
                        startActivity(i);
                }
        }

        private void createRequest() {
                // Configure Google Sign In
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                // Build a GoogleSignInClient with the options specified by gso.
                mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        }

        private void signIn() {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);

                //FACEBOOK CODE

                callbackManager.onActivityResult(requestCode, resultCode, data);

                //..............

                // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
                if (requestCode == RC_SIGN_IN) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                                // Google Sign In was successful, authenticate with Firebase
                                GoogleSignInAccount account = task.getResult(ApiException.class);
                                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                                firebaseAuthWithGoogle(account.getIdToken());
                        } catch (ApiException e) {
                                // Google Sign In failed
                                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                }
        }

        private void firebaseAuthWithGoogle(String idToken) {
                AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                Intent intent = new Intent(getApplicationContext(), BottomNavigationHandler.class);
                                                startActivity(intent);
                                        } else {
                                                // If sign in fails, display a message to the user.
                                                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                        }

                                        // ...
                                }
                        });
        }

}