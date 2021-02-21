package com.harishram.stoshale;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Send_Request extends AppCompatActivity {

    TextView confirm_text,send_request;
    Bundle user_bun;
    String recipient_name,recipient_un;
    String username,name;
    DatabaseReference dbr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request);
        getSupportActionBar().hide();
        recipient_name = getIntent().getBundleExtra("User").getString("Recipient");
        recipient_un = getIntent().getBundleExtra("User").getString("Recipient_un");
        name = getIntent().getBundleExtra("User").getString("Name");
        username = getIntent().getBundleExtra("User").getString("Username");
        confirm_text = (TextView) findViewById(R.id.textView19);
        confirm_text.setText("Send Friend Request to "+recipient_name+" ?");
        send_request = (TextView) findViewById(R.id.textView20);
        dbr = FirebaseDatabase.getInstance().getReference();
        send_request.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Request request = new Request(recipient_name,recipient_un);
                dbr.child("Users").child(username).child("Requests").child(recipient_name).setValue(request);
                Friend_Request friend_request = new Friend_Request(name,username);
                dbr.child("Users").child(recipient_un).child("Notifications").child("Friend Requests").child(name).setValue(friend_request);
                Toast req_sent = Toast.makeText(getApplicationContext(),"Friend Request Sent",Toast.LENGTH_SHORT);
                req_sent.show();
                user_bun = new Bundle();
                user_bun.putString("Name",getIntent().getBundleExtra("User").getString("Name"));
                user_bun.putString("Username",getIntent().getBundleExtra("User").getString("Username"));
                Intent user_list_intent = new Intent(getApplicationContext(),User_List.class);
                user_list_intent.putExtra("User",user_bun);
                startActivity(user_list_intent);
            }
        });

    }
    @Override
    public void onBackPressed(){
        user_bun = new Bundle();
        user_bun.putString("Name",getIntent().getBundleExtra("User").getString("Name"));
        user_bun.putString("Username",getIntent().getBundleExtra("User").getString("Username"));
        Intent user_list_intent = new Intent(this,User_List.class);
        user_list_intent.putExtra("User",user_bun);
        startActivity(user_list_intent);
    }
}