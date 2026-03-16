package com.example.quizzetest.model;

import androidx.annotation.NonNull;import java.io.Serializable;


public class Student implements Serializable {

    private String id;
    private String name;
    private int age;
    private int profile;


    // Default constructor (often required for Firebase or libraries like Room/GSON)
    public Student() {
    }

    // Full constructor
    public Student(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.profile = profile;
    }

    // --- Getters and Setters ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getProfile() {
        return profile;
    }

    // toString method for debugging purposes
    @NonNull
    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", profile=" + profile +
                '}';
    }
}