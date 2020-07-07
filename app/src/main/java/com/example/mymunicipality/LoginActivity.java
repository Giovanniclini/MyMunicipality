package com.example.mymunicipality;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

        private static final int RC_SIGN_IN = 100 ;
        private CallbackManager mCallbackManager;
        private FirebaseAuth mFirebaseAuth;
        private FirebaseAuth.AuthStateListener authStateListener;
        private TextView textViewUser;
        private LoginButton loginButton;
        private AccessTokenTracker accessTokenTracker;
        private static final String TAG = "FacebookAuthentication";
        private GoogleSignInClient mGoogleSignInClient;
        private SignInButton signInButton;
        private Button btnSignOut;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_login);
                findViewById(R.id.buttonLogin).setOnClickListener(this);
        //        textViewUser = findViewById(R.id.textView);
                findViewById(R.id.buttonRegistration).setOnClickListener(this);
                findViewById(R.id.sign_in_button).setOnClickListener(this); //Google button

                // Configure sign-in to request the user's ID, email address, and basic
                // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail().build();

                // Build a GoogleSignInClient with the options specified by gso.
                final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

                signInButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                signIn();
                        }
                });

                btnSignOut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                mGoogleSignInClient.signOut();
                                Toast.makeText(LoginActivity.this, "You are logged out", Toast.LENGTH_SHORT.show());

                                btnSignOut.setVisibility(View.INVISIBLE);
                        }
                });

                mFirebaseAuth = FirebaseAuth.getInstance();
                FacebookSdk.sdkInitialize(getApplicationContext());
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

                // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
                if (requestCode == RC_SIGN_IN) {
                        // The Task returned from this call is always completed, no need to attach
                        // a listener.
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        handleSignInResult(task);
                }

        }

        private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
                try {
                        GoogleSignInAccount account = completedTask.getResult(ApiException.class);
                        Toast.makeText(LoginActivity.this, "Signed In Successfully", Toast.LENGTH_SHORT.show());
                        FirebaseGoogleAuth(account);

                } catch (ApiException e) {
                        Toast.makeText(LoginActivity.this, "Signed In Failed", Toast.LENGTH_SHORT.show());
                        FirebaseGoogleAuth(null);
                }
        }

        private void FirebaseGoogleAuth(GoogleSignInAccount acct) {
                AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
                mFirebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                        Toast.makeText(LoginActivity.this, "Successful", Toast.LENGTH_SHORT.show());
                                        FirebaseUser user = mFirebaseAuth.getCurrentUser();
                                        updateUI(user);
                                }else{
                                        Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT.show());
                                        updateUI(null);
                                }
                        }
                });
        }

        private void updateUI(FirebaseUser user) {
                btnSignOut.setVisibility(View.VISIBLE);
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

                if(account != null){
                        String personName = account.getDisplayName();
                        String personGivenName = account.getGivenName();
                        String personaFamilyName = account.getFamilyName();
                        String personEmail = account.getEmail();
                        String personId = account.getId();
                        Uri personPhoto = account.getPhotoUrl();

                        Toast.makeText(LoginActivity.this, personName + personEmail,Toast.LENGTH_SHORT).show();
                }


                if(user != null){
        //                textViewUser.setText(user.getDisplayName());
                        if(user.getPhotoUrl() != null){
                                String photoUrl = user.getPhotoUrl().toString();
                                 photoUrl = photoUrl + "?type=large";
                         //       Picasso.get().load(photoUrl).into();  per immagine del video su yt


                        }
                }else{
        //                textViewUser.setText("");
                }
        }

        @Override
        protected void onStart(){
                super.onStart();
                mFirebaseAuth.addAuthStateListener(authStateListener);

                // Check for existing Google Sign In account, if the user is already signed in
                // the GoogleSignInAccount will be non-null.
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
                updateUI(account);
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
                switch (v.getId()) {
                        case R.id.sign_in_button:
                                signIn();
                                break;
        }
}

        private void signIn() {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);   
        }

        @Override
        protected void OnActivityResult(int requestCode, int resultCode,@Nullable Intent data){
                super.onActivityResult(requestCode, resultCode, data);
                if(requestCode == RC_SIGN_IN){
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        handleSignInResult(task);
                }
        }
}


