package com.harishram.stoshale;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Message {
    public String time,sender,message;
    public Message(){

    }
    public Message(String time,String sender,String message){
        this.time = time;
        this.sender = sender;
        this.message = message;
    }
}
