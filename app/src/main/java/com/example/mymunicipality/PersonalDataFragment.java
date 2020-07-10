package com.example.mymunicipality;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import static android.content.Intent.getIntent;

/**
 * A simple {@link Fragment} subclass.
 */

public class PersonalDataFragment extends Fragment {

    static TextView name;
    static TextView email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_data, container, false);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);

        Intent intent= getActivity().getIntent();
        String name1 = intent.getStringExtra("name");
        String email1 = intent.getStringExtra("email");
        name.setText(name1);
        email.setText(email1);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(Objects.requireNonNull(getActivity()));
        if(signInAccount != null){
            name.setText(signInAccount.getDisplayName());
            email.setText(signInAccount.getEmail());
        }

        return  view;
    }

}