package com.harishram.stoshale;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Resource_share {
    public String time,sender,filename;
    public Resource_share(){

    }
    public Resource_share(String time,String sender,String filename){
        this.time = time;
        this.sender = sender;
        this.filename = filename;
    }
}
