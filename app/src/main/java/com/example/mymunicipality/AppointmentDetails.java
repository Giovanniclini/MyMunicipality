package com.example.mymunicipality;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AppointmentDetails extends AppCompatActivity {

    MaterialTextView sectorTextView;
    MaterialTextView objectTextView;
    MaterialTextView dataoraTextView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.report_menu, menu);
        return  true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);

        sectorTextView = findViewById(R.id.sector_text_camp);
        objectTextView = findViewById(R.id.object_text_camp);
        dataoraTextView = findViewById(R.id.dataora_text_camp);

        Intent intent = getIntent();
        final String username1 = intent.getStringExtra("username");
        final String object1 = intent.getStringExtra("object");
        final String sector1 = intent.getStringExtra("sector");

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

                        }
                    }
                }
            });
        }

    }
}