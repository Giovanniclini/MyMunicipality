package com.example.mymunicipality;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = "RegisterActivity";
    TextInputEditText username, password1, password2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        /*final EditText eText = findViewById(R.id.editText1);
        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                DatePickerDialog picker = new DatePickerDialog(RegisterActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker View, int year, int monthOfYear, int dayOfMonth) {
                                eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
         */
        username = findViewById(R.id.xuser);
        password1 = findViewById(R.id.xpassword1);
        password2 = findViewById(R.id.xpassword2);

        findViewById(R.id.buttonRegistration).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user, pass1, pass2;
                user = Objects.requireNonNull(username.getText()).toString();
                pass1 = Objects.requireNonNull(password1.getText()).toString();
                pass2 = Objects.requireNonNull(password2.getText()).toString();
                if (pass1.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password must contains at least 6 character", Toast.LENGTH_SHORT).show();
                } else {
                    if (pass1.equals(pass2)) {
                        createAccount(user, pass1);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Passwords dont match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void createAccount(String username, String password){
        mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "Create user with email: success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    Intent i = new Intent(RegisterActivity.this, BottomNavigationHandler.class);
                    startActivity(i);
                }else{
                    Log.w(TAG, "Create user with email: failed", task.getException());
                    Toast.makeText(RegisterActivity.this, "Authentication failed",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}



