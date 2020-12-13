package com.example.mymunicipality;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class EditPersonalDataActivity extends AppCompatActivity {

    private TextInputEditText cellulare;
    private EditText datanascita;
    private TextInputEditText via;
    private MaterialButton save_changes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_data);

        cellulare = findViewById(R.id.change_cellulare);
        datanascita = findViewById(R.id.change_datanascita);

        Toolbar myToolbar = findViewById(R.id.new_report_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Modifica Dati Personali");

        datanascita.setInputType(InputType.TYPE_NULL);
        datanascita.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                DatePickerDialog picker = new DatePickerDialog(EditPersonalDataActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker View, int year, int monthOfYear, int dayOfMonth) {
                                datanascita.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        via = findViewById(R.id.change_via);
        save_changes = findViewById(R.id.save_info_button);

        save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String save_cellulare = cellulare.getText().toString();
                String save_datanascita = datanascita.getText().toString();
                String save_via = via.getText().toString();

                Intent return_intent = new Intent();
                return_intent.putExtra("cellulare", save_cellulare);
                return_intent.putExtra("datanascita", save_datanascita);
                return_intent.putExtra("via", save_via);
                setResult(RESULT_OK, return_intent);
                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}