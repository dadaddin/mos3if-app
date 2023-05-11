package com.example.mos3if;

public class User {
    public String firstName,lastName,email,phone,photoUri;
    public Status status;



    public User(){}

    public User(String firstName, String lastName, String email ,String phone,String photoUri) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.photoUri = photoUri;
    }

    public User(String firstName, String lastName, String email, String phone, Status status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.status = status;
    }

    public User(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public enum Status{
        AVAILABLE,
        UNAVAILABLE
    }
}
