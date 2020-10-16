package com.example.mymunicipality;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class PersonalDataFragment extends Fragment {

    private static final String TAG = "PersonalDataFragment";
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int LAUNCH_ACTIVITY = 3;
    static TextView name;
    static TextView email;
    static TextView cellulare;
    static TextView viaoPiazza;
    static TextView datadinascita;
    CircleImageView photo;
    FloatingActionButton button_add_image;
    private Uri mImageUri;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference mStorageRef;
    String emailDB = null;
    MaterialButton changeButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_data, container, false);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        cellulare = view.findViewById(R.id.cellulare);
        viaoPiazza = view.findViewById(R.id.StreetPlatz);
        datadinascita = view.findViewById(R.id.birthdayDate);
        photo = view.findViewById(R.id.photo);
        button_add_image = view.findViewById(R.id.button_add_image);
        changeButton = view.findViewById(R.id.buttonChange);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditPersonalDataActivity.class);
                startActivityForResult(intent,LAUNCH_ACTIVITY);
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            emailDB = user.getEmail();
        }

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(Objects.requireNonNull(getActivity()));
        if(signInAccount != null){
            name.setText(signInAccount.getDisplayName());
            email.setText(signInAccount.getEmail());
        }

        button_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        // Create a reference to the cities collection
        CollectionReference usersRef = db.collection("users");

        // Create a query against the collection.
        Task<QuerySnapshot> query = usersRef.whereEqualTo("mail", emailDB)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String fname = document.getString("firstname");
                                String lname = document.getString("lastname");
                                String cell = document.getString("cell");
                                String datanascita = document.getString("datanascita");
                                String viapiazza = document.getString("viapiazza");
                                name.setText(fname + " " + lname);
                                cellulare.setText(cell);
                                email.setText(emailDB);
                                datadinascita.setText(datanascita);
                                viaoPiazza.setText(viapiazza);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        return  view;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String profilepictures = "profilepictures/";
        String path = profilepictures + emailDB;
        StorageReference riversRef = mStorageRef.child(path);
        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final File finalLocalFile = localFile;
        riversRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Successfully downloaded data to local file
                        Picasso.get().load(finalLocalFile).into(photo);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
            if(requestCode == PICK_IMAGE_REQUEST){
                //if (resultCode == RESULT_OK)
                    mImageUri = data.getData();
                    Picasso.get().load(mImageUri).into(photo);
                    String profilepictures = "profilepictures/";
                    String path = profilepictures + emailDB;
                    StorageReference riversRef = mStorageRef.child(path);

                    riversRef.putFile(mImageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Get a URL to the uploaded content

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads

                                }
                            });

            }

            if(requestCode == LAUNCH_ACTIVITY){
                if (resultCode == RESULT_OK) {
                    String new_email = data.getStringExtra("email");
                    String new_cellulare = data.getStringExtra("cellulare");
                    String new_datanascita = data.getStringExtra("datanascita");
                    String new_via = data.getStringExtra("via");
                    Log.d(TAG, new_email);
                }

            }
    }



}