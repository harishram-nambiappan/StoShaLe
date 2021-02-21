package com.harishram.stoshale;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Friend_Request {
    public String name,username;
    public Friend_Request(){

    }
    public Friend_Request(String name,String username){
        this.name = name;
        this.username = username;
    }
}
