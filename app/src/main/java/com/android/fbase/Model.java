package com.android.fbase;

public class Model {
    String name, course, email, surl;

    public Model(String name, String email, String course, String surl) {
        this.name = name;
        this.course = course;
        this.email = email;
        this.surl = surl;
    }

    public Model() {}
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSurl() {
        return surl;
    }

    public void setSurl(String surl) {
        this.surl = surl;
    }
}
