package com.example.mymunicipality;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.StringValue;

import java.util.Calendar;
import java.util.Scanner;

public class AppointmentDetails extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "Appointment Details";
    MaterialTextView sectorTextView;
    MaterialTextView objectTextView;
    MaterialTextView dataoraTextView;
    MaterialTextView viaTextView;

    String username1;
    String object1;
    String sector1;
    String via;
    String data1;
    String ora1;
    String dataora1;

    private GoogleMap mMap;
    private LatLng latlng;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appointment_menu, menu);
        return  true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_appointment:

                final FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
                DocumentReference mFirestoreDetails = mFirestore.collection("Appointments").document(username1 + " " + object1);
                mFirestoreDetails.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){

                            mFirestore.collection("Appointments").document(username1 + " " + object1).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "Document snapshot successfully deleted");
                                            Toast.makeText(getApplicationContext(), "Sussessfully deleted", Toast.LENGTH_SHORT).show();
                                            finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error deleting document");
                                            }
                                        });
                        }
                    }
                });
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.calendar_appointment:
                String[] dates = data1.split("/");
                int day = Integer.parseInt(dates[0]);
                int month = Integer.parseInt(dates[1]);
                int year = Integer.parseInt(dates[2]);
                String[] time = ora1.split(":");
                int hour = Integer.parseInt(time[0]);
                int minutes = Integer.parseInt(time[1]);
                int endhour, endminutes;
                if(minutes + 30 == 60){
                    endhour = hour + 1;
                    endminutes = 00;
                }
                else{
                    endhour = hour;
                    endminutes = 30;
                }

                Calendar beginTime = Calendar.getInstance();
                beginTime.set(year, month, day, hour, minutes);
                Calendar endTime = Calendar.getInstance();
                endTime.set(year, month, day, endhour, endminutes);
                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                        .putExtra(CalendarContract.Events.TITLE, "Appuntamento con ufficio " + sector1)
                        .putExtra(CalendarContract.Events.DESCRIPTION, object1)
                        .putExtra(CalendarContract.Events.EVENT_LOCATION, via)
                        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
                startActivity(intent);

                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        sectorTextView = findViewById(R.id.sector_text_camp);
        objectTextView = findViewById(R.id.object_text_camp);
        dataoraTextView = findViewById(R.id.dataora_text_camp);
        viaTextView = findViewById(R.id.via_text_camp);

        Intent intent = getIntent();
        username1 = intent.getStringExtra("username");
        object1 = intent.getStringExtra("object");
        sector1 = intent.getStringExtra("sector");

        switch (sector1){
            case "Lavori Pubblici":
            case "Servizi Risorse Umane e Tecnologiche":
            case "Servizi Interni e Demografici":
            case "Servizi Finanziari":
                via = "Via San Francesco D'Assisi 76";
                latlng = new LatLng(43.843817,13.019380);
                break;
            case "Urbanistica":
                via = "Via Marino Francini 2";
                latlng = new LatLng(43.843791,13.018265);
                break;
            case "Servizi Sociali":
                via = "Via Sant'Eusebio 32";
                latlng = new LatLng(43.826132,13.011398);
                break;
            case "Servizi Educativi Cultura e Turismo":
                via = "Via Vitruvio 7";
                latlng = new LatLng(43.845744,13.016004);
                break;
            default:
                via = null;
                break;
        }

        Log.d(TAG, latlng.toString());

        Toolbar toolbar = findViewById(R.id.appointment_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(sector1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if(username1 != null){
            FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
            DocumentReference mFirestoreDetails = mFirestore.collection("Appointments").document(username1 + " " + object1);
            mFirestoreDetails.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){

                            data1 = String.valueOf(document.get("data"));
                            ora1 = String.valueOf(document.get("ora"));
                            dataora1 = data1 + " " + ora1;

                            sectorTextView.setText(sector1);
                            objectTextView.setText(object1);
                            dataoraTextView.setText(dataora1);
                            viaTextView.setText(via);

                        }
                    }
                }
            });
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(latlng).title("Meeting point"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
    }



}
