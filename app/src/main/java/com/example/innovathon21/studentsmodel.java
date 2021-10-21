package com.example.innovathon21;

public class studentsmodel {
    public studentsmodel() {
    }
    String name,Email,imageUrl;

    public studentsmodel(String name, String email, String imageUrl) {
        this.name = name;
        Email = email;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
