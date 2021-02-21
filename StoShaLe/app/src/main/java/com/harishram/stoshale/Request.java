package com.harishram.stoshale;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Request {
    public String name,username;
    public Request(){

    }
    public Request(String name,String username){
        this.name = name;
        this.username = username;
    }
}
