package com.example.mos3if;

public class User {
    public String firstName,lastName,email,photoUri;
    public Type type;


    public User(){}

    public User(String firstName, String lastName,Type type, String email ,String photoUri) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.type = type;
        this.email = email;
        this.photoUri = photoUri;
    }

    public User(String firstName, String lastName,Type type, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.type = type;
        this.email = email;
    }

    public User(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }
 public enum Type{
        BASIC, VOLUNTEER}

}
