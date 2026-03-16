package com.example.thurdaytest; // FIXED: Changed from quizzetest to thurdaytest

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// FIXED: All imports updated to match your actual package 'thurdaytest'
import com.example.thurdaytest.Adapter.StudentAdapter;
import com.example.thurdaytest.Interface.StudentInterface;
import com.example.thurdaytest.model.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentList extends AppCompatActivity implements StudentInterface {

    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private List<Student> studentList;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_list);

        studentList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);

        // Ensure RecyclerView exists in XML
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new StudentAdapter(studentList, this);
            recyclerView.setAdapter(adapter);

            ViewCompat.setOnApplyWindowInsetsListener(recyclerView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        addDummyData();

        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    filterStudent(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filterStudent(newText);
                    return true;
                }
            });
        }
    }

    private void addDummyData() {
        studentList.add(new Student("1", "John Doe", 20));
        studentList.add(new Student("2", "Jane Smith", 22));
        studentList.add(new Student("3", "Alex Johnson", 21));
        studentList.add(new Student("4", "Maria Garcia", 23));
        studentList.add(new Student("5", "Ahmed Khan", 19));
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void filterStudent(String query) {
        List<Student> filteredList = new ArrayList<>();

        for (Student student : studentList) {
            if (student.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(student);
            }
        }

        if (adapter != null) {
            adapter.setStudentList(filteredList);
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No student found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onclickItem(Student student) {
        // Ensure StudentDetails class exists in your project
        Intent intent = new Intent(this, StudentDetails.class);
        String[] information = {
                student.getId(),
                student.getName(),
                String.valueOf(student.getAge())
        };
        intent.putExtra("information", information);
        startActivity(intent);
    }
}