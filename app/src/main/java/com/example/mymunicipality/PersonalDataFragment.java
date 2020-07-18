package com.example.mymunicipality;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class PersonalDataFragment extends Fragment {

    private static final String TAG = "PersonalDataFragment";
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int RESULT_OK = 2;
    static TextView name;
    static TextView email;
    ShapeableImageView photo;
    FloatingActionButton button_add_image;
    private Uri mImageUri;
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_data, container, false);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        photo = view.findViewById(R.id.photo);
        button_add_image = view.findViewById(R.id.button_add_image);

        button_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });


        Bundle bundle = getArguments();
        String name1 = bundle.getString("name");
        String email1 = bundle.getString("email");
        name.setText(name1);
        email.setText(email1);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //name.setText(Objects.requireNonNull(user.getDisplayName()));
            email.setText(user.getEmail());
        }

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

    public void putArguments(Bundle bundle) {

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

//Commit prova
}