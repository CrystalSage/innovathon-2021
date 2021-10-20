package com.example.innovathon21;

public class studentsmodel {
    public studentsmodel() {
    }
    String name_student,email,img_url;

    public studentsmodel(String name_student, String email, String img_url) {
        this.name_student = name_student;
        this.email = email;
        this.img_url = img_url;
    }

    public String getName_student() {
        return name_student;
    }

    public void setName_student(String name_student) {
        this.name_student = name_student;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
