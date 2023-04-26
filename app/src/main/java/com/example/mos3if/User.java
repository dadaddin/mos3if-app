package com.example.mos3if;

public class User {
    public String firstName,lastName,email,photoUri;



    public User(){}

    public User(String firstName, String lastName, String email ,String photoUri) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.photoUri = photoUri;
    }

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
