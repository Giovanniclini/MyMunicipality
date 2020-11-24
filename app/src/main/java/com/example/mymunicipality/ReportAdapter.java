package com.example.mymunicipality;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Range;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.Enum;

import java.io.Serializable;
import java.util.Objects;


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

        final String title = reportData.getTitle();
        final String username = reportData.getUsername();
        Integer votesCount = reportData.getPriority();
        final VotesData votesData = new VotesData(title,username, votesCount);
        final String loggedUserEmail =FirebaseAuth.getInstance().getCurrentUser().getEmail();
        final String key = loggedUserEmail + " " + title;

        Log.d("Utente", loggedUserEmail);

        //OnClick del UpVote
        holder.button_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Integer votesCount = votesData.getVotesCount() + 1;
                VotesData votes = new VotesData(title,loggedUserEmail,votesCount);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db
                        .collection("Votes")
                        .document(key)
                        .set(votes);

                if(votesCount >= -1 && votesCount <= 1){

                    Integer priority = reportData.getPriority() + 1;
                    FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();
                    mFirebaseFirestore
                            .collection("Reports")
                            .document(key)
                            .update("priority", priority);

                }
            }
        });

        //OnClick del DownVote
        holder.button_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Integer votesCount = votesData.getVotesCount() - 1;
                VotesData votes = new VotesData(title,loggedUserEmail,votesCount);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db
                        .collection("Votes")
                        .document(key)
                        .set(votes);

                if(votesCount >= -1 && votesCount <= 1){

                    Integer priority = reportData.getPriority() - 1;
                    FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();
                    mFirebaseFirestore
                            .collection("Reports")
                            .document(key)
                            .update("priority", priority);

                }
            }
        });

        //Onclick sull'intera view
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("XXXX", reportData.getTitle());
                Intent intent = new Intent(view.getContext() , ReportDetails.class);
                intent.putExtra("titolo", reportData.getTitle());
                intent.putExtra("utente", reportData.getUsername());
                view.getContext().startActivity(intent);
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
