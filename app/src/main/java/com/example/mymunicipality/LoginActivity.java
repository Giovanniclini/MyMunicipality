package com.example.mymunicipality;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

        private static final int RC_SIGN_IN = 1;
        private static final String TAG = "LoginActivity" ;
        private GoogleSignInClient mGoogleSignInClient;
        private static FirebaseAuth mAuth;
        private CallbackManager callbackManager;
        private Button loginButton;
        private FirebaseFirestore db = FirebaseFirestore.getInstance();
        private FirebaseAuth.AuthStateListener mAuthListener;

        @Override
        protected void onStart(){
                super.onStart();

                mAuth.addAuthStateListener(mAuthListener);

                FirebaseUser user = mAuth.getCurrentUser();
                if(user != null){
                        Intent intent = new Intent(getApplicationContext(), BottomNavigationHandler.class);
                        startActivity(intent);
                }


        }

        @Override
        public void onStop() {
                super.onStop();
                // [START on_stop_remove_listener]
                if (mAuthListener != null) {
                        mAuth.removeAuthStateListener(mAuthListener);
                }
        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_login);

                // Initialize Firebase Auth
                mAuth = FirebaseAuth.getInstance();

                // [START auth_state_listener]
                mAuthListener = new FirebaseAuth.AuthStateListener() {
                        @Override
                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                if (user != null) {
                                        // User is signed in
                                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                                        Toast.makeText(LoginActivity.this, "Autenticazione riuscita",
                                                Toast.LENGTH_SHORT).show();
                                } else {
                                        // User is signed out
                                        Log.d(TAG, "onAuthStateChanged:signed_out");
                                        Toast.makeText(LoginActivity.this, "Uscita avvenuta con successo o nessun utente ancora registrato",
                                                Toast.LENGTH_SHORT).show();
                                }
                                // ...
                        }
                };

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
                                                Log.v("facebook - onSuccess", "Succed");
                                                handleFacebookAccessToken(loginResult.getAccessToken());
                                                Intent intent = new Intent(getApplicationContext(), BottomNavigationHandler.class);
                                                startActivity(intent);
                                        }

                                        @Override
                                        public void onCancel() {
                                                setResult(RESULT_CANCELED);
                                                Log.v("facebook - onCancel", "cancelled");
                                        }

                                        @Override
                                        public void onError(FacebookException error) {
                                                Log.v("facebook - onError", Objects.requireNonNull(error.getMessage()));
                                        }
                                });

                        }
                });



                //

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

                findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                                TextInputEditText email, password;
                                email = findViewById(R.id.yuser);
                                password = findViewById(R.id.ypassword);
                                String email1, password1;
                                email1 = Objects.requireNonNull(email.getText()).toString();
                                password1 = Objects.requireNonNull(password.getText()).toString();
                                signInWithEmailAndPassword(email1, password1);
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
                                                String email = user.getEmail();
                                                String firstlastname = user.getDisplayName();
                                                String[] parts = firstlastname.split(" ");
                                                String firstname = parts[0];
                                                String lastname = parts[1];
                                                Log.d(TAG, firstlastname + " " + email);
                                                addUser(email, null, firstname, lastname, null, null, null);

                                        } else {
                                                // If sign in fails, display a message to the user.
                                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                                                Toast.makeText(LoginActivity.this, "Autenticazione fallita",
                                                        Toast.LENGTH_SHORT).show();
                                        }

                                }
                        });
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


                callbackManager.onActivityResult(requestCode, resultCode, data);


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
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                String email = user.getEmail();
                                                String firstlastname = user.getDisplayName();
                                                String[] parts = firstlastname.split(" ");
                                                String firstname = parts[0];
                                                String lastname = parts[1];
                                                addUser(email, null, firstname, lastname, null, null, null);
                                                Intent intent = new Intent(getApplicationContext(), BottomNavigationHandler.class);
                                                startActivity(intent);
                                        } else {
                                                Toast.makeText(LoginActivity.this, "Autenticazione fallita", Toast.LENGTH_SHORT).show();
                                        }

                                }
                        });
        }

        private void signInWithEmailAndPassword(final String username, String password){
                mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                        Log.d(TAG, "Sign in with email: success");
                                        Intent i = new Intent(LoginActivity.this, BottomNavigationHandler.class);
                                        startActivity(i);
                                }else{
                                        Log.w(TAG, "Sign in with email: failed", task.getException());
                                        Toast.makeText(LoginActivity.this, "L'utente non esiste.",Toast.LENGTH_SHORT).show();
                                }
                        }
                });
        }

        public void addUser(String mail,String pass1, String nome, String cognome, String cellulare, String viapiazza,String datanascita){

                Map<String,Object> user = new HashMap<>();
                user.put("mail", mail);
                user.put("password", pass1);
                user.put("firstname", nome);
                user.put("lastname", cognome);
                user.put("cell", cellulare);
                user.put("viapiazza", viapiazza);
                user.put("datanascita", datanascita);

                db.collection("users")
                        .document(mail)
                        .set(user);
        }

}