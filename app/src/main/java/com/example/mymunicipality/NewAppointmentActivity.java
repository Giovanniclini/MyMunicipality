package com.example.mymunicipality;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class NewAppointmentActivity extends AppCompatActivity {

    private static final int TIME_PICKER_INTERVAL = 30;
    final String TAG = "10";
    Spinner spinner;
    TextInputEditText object;
    TextInputEditText calendar;
    TimePicker picker;
    MaterialButton tryConfirmButton;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_appointment_menu, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_appointment:
                saveNote();
                finish();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);

        Toolbar toolbar = findViewById(R.id.newappointment_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Nuovo Appuntamento");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        spinner = findViewById(R.id.sector_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sector_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        object = findViewById(R.id.appointment_object);



        calendar = findViewById(R.id.appointment_calendar);
        calendar.setInputType(InputType.TYPE_NULL);
        calendar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                DatePickerDialog picker = new DatePickerDialog(NewAppointmentActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker View, int year, int monthOfYear, int dayOfMonth) {
                                calendar.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        picker = findViewById(R.id.appointment_clock);
        setTimePickerInterval(picker);
        picker.setIs24HourView(true);
    }

    private void saveNote() {
        Object sector = spinner.getSelectedItem();
        String object_string = Objects.requireNonNull(object.getText()).toString();
        String data = calendar.getText().toString();
       int ora = picker.getHour();
       int minuti = picker.getMinute();
       String ora1 = Integer.toString(ora);
       String minuti1 = Integer.toString(minuti);
       if (minuti1.equals("1")){
           minuti1 = "30";
       }
       else {
           minuti1 = "00";
       }
       String orario = ora1 + ":" + minuti1;


        if(data.trim().isEmpty() || object_string.trim().isEmpty()){
            Toast.makeText(this, "Inserire Data e Oggetto", Toast.LENGTH_LONG).show();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String username = null;
        if (user != null){
            username = user.getDisplayName();
        }

        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        DocumentReference mFirestoreAppointments = mFirestore.collection("Appointments").document(username + " " + object_string);
        AppointmentData appointmentData = new AppointmentData((String) sector,object_string,data,orario,username);
        mFirestoreAppointments.set(appointmentData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Document written successfully", Toast.LENGTH_SHORT).show();
            }
        });
        finish();

    }

    private void setTimePickerInterval(TimePicker timePicker) {
        try {

            NumberPicker minutePicker = timePicker.findViewById(Resources.getSystem().getIdentifier(
                    "minute", "id", "android"));
            minutePicker.setMinValue(0);
            minutePicker.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
            List<String> displayedValues = new ArrayList<>();
            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                displayedValues.add(String.format("%02d", i));
            }
            minutePicker.setDisplayedValues(displayedValues.toArray(new String[0]));
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e);
        }
    }
}