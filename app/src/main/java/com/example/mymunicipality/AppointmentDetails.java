package com.example.mymunicipality;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AppointmentDetails extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "Appointment Details";
    MaterialTextView sectorTextView;
    MaterialTextView objectTextView;
    MaterialTextView dataoraTextView;
    MaterialTextView viaTextView;

    String username1;
    String object1;
    String sector1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.report_menu, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_report:

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

        final String via;

        switch (sector1){
            case "Lavori Pubblici":
            case "Servizi Risorse Umane e Tecnologiche":
            case "Servizi Interni e Demografici":
            case "Servizi Finanziari":
                via = "Via San Francesco D'Assisi 76";
                break;
            case "Urbanistica":
                via = "Via Marino Francini 2";
                break;
            case "Servizi Sociali":
                via = "Via Sant'Eusebio 32";
                break;
            case "Servizi Educativi Cultura e Turismo":
                via = "Via Vitruvio 7";
                break;
            default:
                via = null;
                break;
        }

        //Toolbar da sistemare
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

                            String sector1 = document.getString("sector");
                            String object1 = document.getString("object");
                            String data1 = String.valueOf(document.get("data"));
                            String ora1 = String.valueOf(document.get("ora"));
                            String dataora1 = data1 + " " + ora1;

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
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));
    }
}
