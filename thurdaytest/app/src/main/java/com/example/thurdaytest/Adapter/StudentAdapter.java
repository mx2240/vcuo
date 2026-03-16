package com.example.thurdaytest.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thurdaytest.Interface.StudentInterface;
import com.example.thurdaytest.R;
import com.example.thurdaytest.model.Student;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Student> studentList;
    private final StudentInterface studentInterface;

    // Constructor
    public StudentAdapter(List<Student> studentList, StudentInterface studentInterface) {
        this.studentList = studentList;
        this.studentInterface = studentInterface;
    }

    // Method to update the list (used for Search Filtering)
    public void setStudentList(List<Student> newList) {
        this.studentList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This inflates the individual row layout (item_student.xml)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_layout, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);

        holder.tvName.setText(student.getName());
        holder.tvAge.setText("Age: " + student.getAge());
        holder.tvId.setText("ID: " + student.getId());

        // Use a default icon or a specific profile image if available
        holder.imgProfile.setImageResource(R.drawable.ic_launcher_foreground);

        // Set the click listener for the item
        holder.itemView.setOnClickListener(v -> {
            if (studentInterface != null) {
                studentInterface.onclickItem(student);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList != null ? studentList.size() : 0;
    }

    // ViewHolder class to hold the views for each row
    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAge, tvId;
        ImageView imgProfile;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvStudentName);;
            tvId = itemView.findViewById(R.id.tvStudentId);
            imgProfile = itemView.findViewById(R.id.profileImage);
        }
    }
}