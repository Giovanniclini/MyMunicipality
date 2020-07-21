package com.example.mymunicipality;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewReportActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextPriority;
    //private NumberPicker numberPickerPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextPriority =findViewById(R.id.priority_number);

        //numberPickerPriority = findViewById(R.id.number_picker_priority);

        //numberPickerPriority.setMinValue(1);
        //numberPickerPriority.setMaxValue(10);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
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

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        String priority = editTextPriority.getText().toString();
        //int priority = numberPickerPriority.getValue();

        if(title.trim().isEmpty() || description.trim().isEmpty()){
            Toast.makeText(this, "please insert a title amd description", Toast.LENGTH_SHORT).show();
            return;
        }

        CollectionReference reportsRef = FirebaseFirestore.getInstance().collection("Reports");
        reportsRef.add(new ReportData(title, description, priority));
        Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show();
        finish();

    }

}