package com.harishram.stoshale;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Accept_Friend_Request extends AppCompatActivity {

    Bundle user_bun;
    String recipient,recipient_un,name,username;
    TextView req_conf,accept;
    DatabaseReference dbr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_friend_request);
        getSupportActionBar().hide();
        dbr = FirebaseDatabase.getInstance().getReference();
        name = getIntent().getBundleExtra("User").getString("Name");
        username = getIntent().getBundleExtra("User").getString("Username");
        recipient = getIntent().getBundleExtra("User").getString("Recipient");
        recipient_un = getIntent().getBundleExtra("User").getString("Recipient_un");
        req_conf = (TextView) findViewById(R.id.textView23);
        accept = (TextView) findViewById(R.id.textView24);
        req_conf.setText("Accept friend request from "+recipient+" ?");
        accept.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Friend friend = new Friend(recipient,recipient_un);
                Friend recipient_friend = new Friend(name,username);
                dbr.child("Users").child(username).child("Friends").child(recipient).setValue(friend);
                dbr.child("Users").child(recipient_un).child("Friends").child(name).setValue(recipient_friend);
                dbr.child("Users").child(username).child("Notifications").child("Friend Requests").child(recipient).setValue(null);
                dbr.child("Users").child(recipient_un).child("Requests").child(name).setValue(null);
                user_bun = new Bundle();
                user_bun.putString("Name",name);
                user_bun.putString("Username",username);
                Intent friend_request_intent = new Intent(getApplicationContext(),Friend_Request_List.class);
                friend_request_intent.putExtra("User",user_bun);
                startActivity(friend_request_intent);

            }
        });
    }

    @Override
    public void onBackPressed(){
        user_bun = new Bundle();
        user_bun.putString("Name",name);
        user_bun.putString("Username",username);
        Intent friend_request_intent = new Intent(this,Friend_Request_List.class);
        friend_request_intent.putExtra("User",user_bun);
        startActivity(friend_request_intent);
    }
}