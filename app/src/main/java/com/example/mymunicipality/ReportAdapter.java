package com.example.mymunicipality;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;

public class ReportAdapter extends FirestoreRecyclerAdapter<ReportData, ReportAdapter.ReportHolder> implements Serializable {


    public ReportAdapter(@NonNull FirestoreRecyclerOptions<ReportData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ReportHolder holder, int position, @NonNull final ReportData reportData) {
        holder.textViewTitle.setText(String.valueOf(reportData.getTitle()));
        //holder.textViewDescription.setText(String.valueOf(reportData.getDescription()));
        holder.textViewVia.setText(String.valueOf(reportData.getVia()));
        holder.textViewPriority.setText(String.valueOf(reportData.getPriority()));


        //OnClick del UpVote
        holder.button_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer priority = reportData.getPriority() + 1;
                FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();
                mFirebaseFirestore
                        .collection("Reports")
                        .document(reportData.getTitle())
                        .update("priority", priority);


            }
        });

        //OnClick del DownVote
        holder.button_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer priority = reportData.getPriority() - 1;
                FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();
                mFirebaseFirestore
                        .collection("Reports")
                        .document(reportData.getTitle())
                        .update("priority", priority);


            }
        });

        //Onclick sull'intera view
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("XXXX", reportData.getTitle());
                Intent intent = new Intent(view.getContext() , ReportDetails.class);
                intent.putExtra("titolo", reportData.getTitle());
                view.getContext().startActivity(intent);
                //Intent per activity di dettaglio report
            }
        });

    }

    @NonNull
    @Override
    public ReportHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item, parent, false);

        return new ReportHolder(v);
    }

    static class ReportHolder extends RecyclerView.ViewHolder {

        View view;
        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewVia;
        TextView textViewPriority;
        FloatingActionButton button_up;
        FloatingActionButton button_down;

        public ReportHolder(View itemView){
            super(itemView);

            view = itemView;
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            //textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewVia = itemView.findViewById(R.id.text_view_via);
            textViewPriority =itemView.findViewById(R.id.text_view_priority);
            button_up = itemView.findViewById(R.id.button_up);
            button_down = itemView.findViewById(R.id.button_down);

        }

    }

}
