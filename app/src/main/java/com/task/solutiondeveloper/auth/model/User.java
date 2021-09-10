package com.task.solutiondeveloper.auth.model;


public class User{
    private String birthday;
    private String name;
    private String age;
    private String email;

    public User(){

    }

    public User(String birthday, String name, String age, String email){
        this.birthday = birthday;
        this.name = name;
        this.age = age;
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}