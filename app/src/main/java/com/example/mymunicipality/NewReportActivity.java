package com.example.mymunicipality;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class NewReportActivity extends AppCompatActivity {

    private static final int CAPTURE_IMAGE_REQUEST = 1;
    private static final String TAG ="NewReportActivity" ;
    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextVia;
    private ShapeableImageView photo_report;
    private MaterialButton add_photo_button;
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
        myToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setTitle("Nuova Segnalazione");

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        add_photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });
    }



    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_REQUEST) {

            Log.d(TAG, "OnActivityResult");
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            photo_report.setImageBitmap(bitmap);
            String reportPictures = "reportPictures/";
            String path = reportPictures + editTextTitle.getText();
            StorageReference reportPicturesRef = mStorageRef.child(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] dataFromBitmap = baos.toByteArray();
            UploadTask uploadTask = reportPicturesRef.putBytes(dataFromBitmap);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d(TAG, "Image uploaded");
                }
            });
        }
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

        if(title.trim().isEmpty() || description.trim().isEmpty() || via.trim().isEmpty() ){
            Toast.makeText(this, "Perfavore, inserire i campi mancanti", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = null;
        if (user != null){
            email = user.getEmail();
        }

       FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
       DocumentReference mFirestoreReports = mFirestore.collection("Reports").document(email + " " + title);
        ReportData reports = new ReportData(title,description,via,priority,email);
        mFirestoreReports.set(reports).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Segnalazione inserita con successo", Toast.LENGTH_SHORT).show();
            }
        });
        finish();

    }

}