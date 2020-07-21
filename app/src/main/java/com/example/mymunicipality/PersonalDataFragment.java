package com.example.mymunicipality;

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
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class PersonalDataFragment extends Fragment {

    private static final String TAG = "PersonalDataFragment";
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int RESULT_OK = 2;
    static TextView name;
    static TextView email;
    static TextView cellulare;
    static TextView viaoPiazza;
    static TextView datadinascita;
    ShapeableImageView photo;
    FloatingActionButton button_add_image;
    private Uri mImageUri;
    FirebaseFirestore db = FirebaseFirestore.getInstance();



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

        button_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });


        Bundle bundle = getArguments();
        if (bundle != null) {
            //Informazioni da GraphRequest per Facebook
            String name1 = bundle.getString("name");
            String email1 = bundle.getString("email");
            //Set informazioni di Facebook
            name.setText(name1);
            email.setText(email1);
        }

        //Dal Login per query su Database
        String emailDB1 = bundle.getString("emailDB1");
        String emailDB0 = bundle.getString("emailDB");
        String emailDB2 = bundle.getString("emailDB2");
        String emailDB = null;

        if (emailDB1 == null && emailDB0 == null){
            emailDB = emailDB2;
        }
        else if (emailDB2 == null && emailDB1 == null){
            emailDB = emailDB0;
        }
        else {
            emailDB = emailDB1;
        }

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
                                datadinascita.setText(datanascita);
                                viaoPiazza.setText(viapiazza);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });





        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //name.setText(Objects.requireNonNull(user.getDisplayName()));
            email.setText(user.getEmail());
        }

        //Piccolo errore dovuto a questa parte di codice
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(Objects.requireNonNull(getActivity()));
        if(signInAccount != null){
            name.setText(signInAccount.getDisplayName());
            email.setText(signInAccount.getEmail());
        }

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

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){

            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(photo);

            //photo.setImageURI(mImageUri);  //i can use this code instead of Picasso
        }
    }

}