package com.example.mymunicipality;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.facebook.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;



public class PersonalDataFragment extends Fragment {

    private static final String TAG = "PersonalDataFragment";
    static TextView name;
    static TextView email;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_data, container, false);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);

        /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //name.setText(Objects.requireNonNull(user.getDisplayName()));
            email.setText(user.getEmail());
        }
        else{
            Toast.makeText(getContext(), "You're not logged in", Toast.LENGTH_SHORT).show();
        }

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(Objects.requireNonNull(getActivity()));
        if(signInAccount != null){
            name.setText(signInAccount.getDisplayName());
            email.setText(signInAccount.getEmail());
        }

         */

        return  view;
    }

    public void putArguments(Bundle args){
        String value1 = args.getString("name");
        String value2 = args.getString("email");
        name.setText(value1);
        email.setText(value2);
    }

}