package com.example.thurdaytest;

import android.content.Intent; // 1. ADDED THIS IMPORT
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class StudentForm extends AppCompatActivity {

    private EditText etName, etIndexNumber;
    private Button btnAddStudent;
    private FloatingActionButton fabViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_form);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etName = findViewById(R.id.etName);
        etIndexNumber = findViewById(R.id.etIndexNumber);
        btnAddStudent = findViewById(R.id.btnAddStudent);
        fabViewList = findViewById(R.id.fabViewList);

        btnAddStudent.setOnClickListener(v -> {
            addStudent();
        });

        // 2. Navigation logic is now fixed with the import above
        fabViewList.setOnClickListener(v -> {
            Intent intent = new Intent(this, StudentList.class);
            startActivity(intent);
        });
    }

    private void addStudent() {
        String name = etName.getText().toString().trim();
        String index = etIndexNumber.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Name is required");
            return;
        }

        if (index.isEmpty()) {
            etIndexNumber.setError("Index number is required");
            return;
        }

        String message = "Student Added: " + name + " (" + index + ")";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        etName.setText("");
        etIndexNumber.setText("");
    }
}