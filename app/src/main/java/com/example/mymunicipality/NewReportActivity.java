package com.example.mymunicipality;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class NewReportActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG ="NewReportActivity" ;
    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextVia;
    private ShapeableImageView photo_report;
    private MaterialButton add_photo_button;
    private Uri mImageUri;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextVia = findViewById(R.id.edit_text_via);
        editTextDescription = findViewById(R.id.edit_text_description);
        add_photo_button = findViewById(R.id.add_photo);
        photo_report = findViewById(R.id.photo_report);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        Toolbar myToolbar = findViewById(R.id.new_report_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Nuova Segnalazione");

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        add_photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
    }



    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        mImageUri = data.getData();
        Picasso.get().load(mImageUri).into(photo_report);
        String reportPictures = "reportPictures/";
        String path = reportPictures + editTextTitle.getText();
        Log.d(TAG, path);
        StorageReference reverseRef = mStorageRef.child(path);
        reverseRef.putFile(mImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.save_note:
                saveNote();
                return true;
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        String via = editTextVia.getText().toString();
        Integer priority = 0;

        if(title.trim().isEmpty() || description.trim().isEmpty()){
            Toast.makeText(this, "Please insert a Title and Description", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String username = null;
        if (user != null){
            username = user.getDisplayName();
        }

       FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
       DocumentReference mFirestoreReports = mFirestore.collection("Reports").document(username + " " + title);
        ReportData reports = new ReportData(title,description,via,priority,username);
        mFirestoreReports.set(reports).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Report written successfully", Toast.LENGTH_SHORT).show();
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference mFirestoreVotes = db.collection("Votes").document(username + " " + title);
        Integer votesCount = 0;
        VotesData votes = new VotesData(title,username,votesCount);
        mFirestoreVotes.set(votes).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Votes written successfully", Toast.LENGTH_SHORT).show();
            }
        });
        finish();

    }

}