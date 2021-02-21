package com.harishram.stoshale;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Friend {
    public String name,username;
    public Friend(){

    }
    public Friend(String name,String username){
        this.name = name;
        this.username = username;
    }
}
