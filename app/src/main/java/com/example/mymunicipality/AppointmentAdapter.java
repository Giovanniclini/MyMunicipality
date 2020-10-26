package com.example.mymunicipality;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;

public class AppointmentAdapter extends FirestoreRecyclerAdapter<AppointmentData, AppointmentAdapter.AppointmentHolder> implements Serializable {

    private static final String TAG = "AppointmentAdapter";

    public AppointmentAdapter(@NonNull FirestoreRecyclerOptions<AppointmentData> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull final AppointmentAdapter.AppointmentHolder holder, int position, @NonNull final AppointmentData appointmentData) {

        holder.textViewSector.setText(String.valueOf(appointmentData.getSector()));

        String data = appointmentData.getData();
        String ora = appointmentData.getOra();
        String dataora = data + " " + ora;

        holder.textViewDataOra.setText(dataora);



        //Onclick sull'intera view
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AppointmentDetails.class);
                intent.putExtra("username", appointmentData.getUsername());
                intent.putExtra("object", appointmentData.getObject());
                intent.putExtra("sector", appointmentData.getSector());
                Log.d(TAG, appointmentData.getUsername());
                Log.d(TAG, appointmentData.getSector());
                Log.d(TAG, appointmentData.getObject());
                view.getContext().startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public AppointmentAdapter.AppointmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_item, parent, false);

        return new AppointmentAdapter.AppointmentHolder(v);
    }


    static class AppointmentHolder extends RecyclerView.ViewHolder {

        View view;
        TextView textViewSector;
        TextView textViewDataOra;

        public AppointmentHolder(View itemView){
            super(itemView);

            view = itemView;
            textViewSector = itemView.findViewById(R.id.text_view_sector);
            textViewDataOra = itemView.findViewById(R.id.text_view_dataora);
        }

    }
}
