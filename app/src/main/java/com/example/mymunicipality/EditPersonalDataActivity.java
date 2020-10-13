package com.example.mymunicipality;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

public class EditPersonalDataActivity extends AppCompatActivity {

    TextInputEditText email;
    TextInputEditText cellulare;
    TextInputEditText datanascita;
    TextInputEditText via;
    MaterialButton save_changes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_data);


        email = findViewById(R.id.change_email);
        cellulare = findViewById(R.id.change_cellulare);
        datanascita = findViewById(R.id.change_datanascita);
        via = findViewById(R.id.change_via);
        save_changes = findViewById(R.id.save_info_button);

        save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String save_email = email.getText().toString();
                String save_cellulare = cellulare.getText().toString();
                String save_datanascita = datanascita.getText().toString();
                String save_via = via.getText().toString();

                Intent return_intent = new Intent();
                return_intent.putExtra("email", save_email);
                return_intent.putExtra("cellulare", save_cellulare);
                return_intent.putExtra("datanascita", save_datanascita);
                return_intent.putExtra("via", save_via);
                setResult(Activity.RESULT_OK, return_intent);
                finish();
            }
        });


    }
}