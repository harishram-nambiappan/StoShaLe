package com.harishram.stoshale;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String name,email,username,password,address;
    public Friend friend;
    public Request request;
    public User(){

    }
    public User(String name,String email,String username,String password,String address){
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.address = address;
        this.friend = null;
        this.request = null;

    }
}
