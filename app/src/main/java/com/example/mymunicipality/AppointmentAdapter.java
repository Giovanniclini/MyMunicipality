package com.example.mymunicipality;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

public class AppointmentAdapter {

    public AppointmentAdapter(@NonNull FirestoreRecyclerOptions<AppointmentData> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull final ReportAdapter.ReportHolder holder, int position, @NonNull final ReportData reportData) {
        holder.textViewTitle.setText(String.valueOf(reportData.getTitle()));
        //holder.textViewDescription.setText(String.valueOf(reportData.getDescription()));
        holder.textViewVia.setText(String.valueOf(reportData.getVia()));
        holder.textViewPriority.setText(String.valueOf(reportData.getPriority()));



        //Onclick sull'intera view
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("XXXX", reportData.getTitle());
                Intent intent = new Intent(view.getContext() , ReportDetails.class);
                intent.putExtra("titolo", reportData.getTitle());
                view.getContext().startActivity(intent);
            }
        });

    }
}
