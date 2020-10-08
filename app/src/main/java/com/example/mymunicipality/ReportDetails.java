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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class ReportDetails extends AppCompatActivity {

    private static final String TAG = "ActivityDetails: ";
    MaterialTextView textViewStreet;
    MaterialTextView textViewDescription;
    MaterialTextView textViewPriority;
    MaterialTextView textViewUser;
    ShapeableImageView imageViewReport;
    private StorageReference mStorageRef;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.report_menu, menu);
        return  true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_details);

        textViewStreet = findViewById(R.id.view_street);
        textViewDescription = findViewById(R.id.view_description);
        textViewPriority = findViewById(R.id.view_priority);
        textViewUser = findViewById(R.id.view_user);
        imageViewReport = findViewById(R.id.photo_report);

        Intent intent = getIntent();
        final String title = intent.getStringExtra("titolo");

        Toolbar toolbar = findViewById(R.id.report_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);

        if (title != null){
            FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
            DocumentReference mFirestoreDetails = mFirestore.collection("Reports").document(title);
            mFirestoreDetails.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()){
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                            String street = document.getString("via");
                            String description = document.getString("description");
                            String priority = String.valueOf(document.get("priority"));
                            String user = document.getString("username");
                            textViewStreet.setText(street);
                            textViewPriority.setText(priority);
                            textViewDescription.setText(description);
                            textViewUser.setText(user);

                            mStorageRef = FirebaseStorage.getInstance().getReference();
                            String reportPictures = "reportPictures/";
                            String path = reportPictures + title;
                            StorageReference reportRef = mStorageRef.child(path);
                            File localFile = null;
                            try {
                                localFile = File.createTempFile("images", "jpg");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            final File finalLocalFile = localFile;
                            reportRef.getFile(localFile)
                                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                            // Successfully downloaded data to local file
                                            Picasso.get().load(finalLocalFile).into(imageViewReport);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle failed download
                                    // ...
                                }
                            });

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with", task.getException());
                    }
                }
            });
        }
    }
}