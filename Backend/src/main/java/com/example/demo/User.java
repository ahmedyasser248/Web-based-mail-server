package com.example.demo;

import com.fasterxml.jackson.annotation.JsonSetter;

public class User {
    /**
     * Data Fields of each user
     * setter with @JsonSetter annotation for http converters
     */
    private String Email;
    private String password;
    private String firstName;
    private String lastName;
    //  getters and setters to get each field
    @JsonSetter("firstName")
    public void setFirstName(String firstName){
        this.firstName=firstName;
    }
    @JsonSetter("lastName")
    public void setLastName(String lastName){
        this.lastName=lastName;
    }
    @JsonSetter("Email")
    public void setEmail(String email){
        this.Email=email;
    }
    @JsonSetter("password")
    public void setPassword(String password){
        this.password=password;
    }
    public String getEmail( ){
        return this.Email;
    }
    public String getPassword(){
        return this.password;
    }
    public String getLastName(){
        return this.lastName;
    }
    public String getFirstName(){
        return this.firstName;
    }

}