package com.example.mymunicipality;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import static android.content.Intent.getIntent;


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

        Bundle bundle = getArguments();
        String name1 = bundle.getString("name");
        String email1 = bundle.getString("email");
        name.setText(name1);
        email.setText(email1);


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

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void sendData(String data1, String data2) {
        if(data1 != null && data2 != null)
            name.setText(data1);
            email.setText(data2);
    }

}