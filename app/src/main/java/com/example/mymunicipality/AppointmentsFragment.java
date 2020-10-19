package com.example.mymunicipality;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AppointmentsFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference appointmentsRef = db.collection("Appointments");

    private ReportAdapter adapter;
    RecyclerView recyclerView;
    FloatingActionButton buttonAddAppointment;
    Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onStop() {
        super.onStop();

        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();

        adapter.startListening();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointments, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        buttonAddAppointment = view.findViewById(R.id.button_add);
        return view;
    }


    private void setUpRecyclerView() {
        Query query = appointmentsRef.orderBy("priority", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ReportData> options = new FirestoreRecyclerOptions
                .Builder<ReportData>()
                .setQuery(query, ReportData.class).build();

        adapter = new ReportAdapter(options);

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context)); //getActivity al posto di this del tutorial
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonAddAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, NewReportActivity.class));
            }
        });

        setUpRecyclerView();

    }

}