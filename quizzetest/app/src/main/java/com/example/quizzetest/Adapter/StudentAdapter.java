package com.example.quizzetest.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizzetest.Interface.StudentInterface;
import com.example.quizzetest.R;
import com.example.quizzetest.model.Student;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    // 1. Remove 'final' so we can update the list during filtering
    private List<Student> studentList;
    private StudentInterface studentInterface;

    // 2. Updated Constructor to include the Interface
    public StudentAdapter(List<Student> studentList, StudentInterface studentInterface) {
        this.studentList = studentList;
        this.studentInterface = studentInterface;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);

        holder.tvName.setText(student.getName());
        holder.tvAge.setText("Age: " + student.getAge());
        holder.tvId.setText("ID: " + student.getId());

        // Ensure student.getProfile() returns a drawable resource ID (int)
        holder.profile.setImageResource(student.getProfile());

        holder.profile.setOnClickListener(v -> {
            if (studentInterface != null) {
                studentInterface.onclickItem(student);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList != null ? studentList.size() : 0;
    }

    // 3. Cleaned up setStudentList (Removed the duplicate)
    public void setStudentList(List<Student> newList) {
        this.studentList = newList;
        notifyDataSetChanged(); // Important: Refresh the UI after filtering
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAge, tvId;
        ImageView profile;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvStudentName);
            tvAge = itemView.findViewById(R.id.tvStudentAge);
            tvId = itemView.findViewById(R.id.tvStudentId);
            profile = itemView.findViewById(R.id.profile);
        }
    }
}