package com.example.mymunicipality;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

public class ReportDetails extends AppCompatActivity {

    MaterialTextView textViewStreet;
    MaterialTextView textViewDescription;
    MaterialTextView textViewPriority;
    ShapeableImageView imageViewReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_details);

        textViewStreet = findViewById(R.id.view_street);
        textViewDescription = findViewById(R.id.view_description);
        textViewPriority = findViewById(R.id.view_priority);

        imageViewReport = findViewById(R.id.photo_report);
    }
}