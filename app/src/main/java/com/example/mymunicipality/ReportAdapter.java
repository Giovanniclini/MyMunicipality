package com.example.mymunicipality;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.io.Serializable;

public class ReportAdapter extends FirestoreRecyclerAdapter<ReportData, ReportAdapter.ReportHolder> implements Serializable {

    public ReportAdapter(@NonNull FirestoreRecyclerOptions<ReportData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ReportHolder holder, int position, @NonNull final ReportData reportData) {
        holder.textViewTitle.setText(String.valueOf(reportData.getTitle()));
        holder.textViewDescription.setText(String.valueOf(reportData.getDescription()));
        holder.textViewVia.setText(String.valueOf(reportData.getVia()));

        // Gestisco il click sull'intera view
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("XXXX", reportData.getTitle());
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

        public ReportHolder(View itemView){
            super(itemView);

            view = itemView;
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            //textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewVia = itemView.findViewById(R.id.text_view_via);

        }

    }

}

//skrt skrt1