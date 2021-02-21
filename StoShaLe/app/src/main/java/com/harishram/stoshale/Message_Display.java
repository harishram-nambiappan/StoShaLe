package com.harishram.stoshale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Message_Display extends AppCompatActivity {

    LinearLayout message_ll;
    String username;
    DatabaseReference dbr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_display);
        getSupportActionBar().hide();
        dbr = FirebaseDatabase.getInstance().getReference();
        message_ll = (LinearLayout) findViewById(R.id.msg_ll);
        username = getIntent().getBundleExtra("User").getString("Username");
        dbr.child("Users").child(username).child("Notifications").child("Messages").addChildEventListener(new ChildEventListener(){

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    Message message = snapshot.getValue(Message.class);
                    TextView msg_info = new TextView(getApplicationContext());
                    msg_info.setText(message.sender + " messaged you: " + message.message);
                    msg_info.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f);
                    msg_info.setTypeface(null, Typeface.BOLD);
                    message_ll.addView(msg_info);
                }
                catch(NullPointerException e){
                    TextView msg_info = new TextView(getApplicationContext());
                    msg_info.setText("No messages available");
                    msg_info.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f);
                    msg_info.setTypeface(null, Typeface.BOLD);
                    message_ll.addView(msg_info);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onBackPressed(){
        Intent notification_list_intent = new Intent(this,Notification_List.class);
        notification_list_intent.putExtra("User",getIntent().getBundleExtra("User"));
        startActivity(notification_list_intent);
    }
}