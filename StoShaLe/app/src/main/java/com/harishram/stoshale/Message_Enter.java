package com.harishram.stoshale;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message_Enter extends AppCompatActivity {

    TextView instruction,send_msg;
    EditText msg;
    String recipient,recipient_un,name,username;
    DatabaseReference dbr;
    Bundle user_bun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_enter);
        getSupportActionBar().hide();
        dbr = FirebaseDatabase.getInstance().getReference();
        recipient = getIntent().getBundleExtra("User").getString("Recipient");
        recipient_un = getIntent().getBundleExtra("User").getString("Recipient_un");
        name = getIntent().getBundleExtra("User").getString("Name");
        username = getIntent().getBundleExtra("User").getString("Username");
        instruction = (TextView) findViewById(R.id.textView26);
        send_msg = (TextView) findViewById(R.id.textView27);
        msg = (EditText) findViewById(R.id.editTextTextMultiLine);
        instruction.setText("Type the message below to send to "+recipient);
        send_msg.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String mess = msg.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-YYYY_HH:mm:ss");
                String time_entry = sdf.format(new Date());
                Message message = new Message(time_entry,name,mess);
                dbr.child("Users").child(recipient_un).child("Notifications").child("Messages").child(time_entry).setValue(message);
                Intent friend_list_intent = new Intent(getApplicationContext(),Friend_List.class);
                user_bun = new Bundle();
                user_bun.putString("Name",name);
                user_bun.putString("Username",username);
                friend_list_intent.putExtra("User",user_bun);
                startActivity(friend_list_intent);
            }
        });
    }
    @Override
    public void onBackPressed(){
        Intent friend_list_intent = new Intent(getApplicationContext(),Friend_List.class);
        user_bun = new Bundle();
        user_bun.putString("Name",name);
        user_bun.putString("Username",username);
        friend_list_intent.putExtra("User",user_bun);
        startActivity(friend_list_intent);
    }
}