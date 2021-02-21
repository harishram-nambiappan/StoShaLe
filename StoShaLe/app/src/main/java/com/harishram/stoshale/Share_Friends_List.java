package com.harishram.stoshale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Share_Friends_List extends AppCompatActivity {

    String name,username,topic,video;
    Bundle user_bun;
    LinearLayout share_friend;
    DatabaseReference dbr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_friends_list);
        getSupportActionBar().hide();
        user_bun = getIntent().getBundleExtra("User");
        name = getIntent().getBundleExtra("User").getString("Name");
        username = getIntent().getBundleExtra("User").getString("Username");
        topic = getIntent().getBundleExtra("User").getString("Topic");
        video = getIntent().getBundleExtra("User").getString("Video");
        share_friend = (LinearLayout) findViewById(R.id.share_friend_ll);
        dbr = FirebaseDatabase.getInstance().getReference();
        dbr.child("Users").child(username).child("Friends").addChildEventListener(new ChildEventListener(){

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                 try{
                     Friend friend = snapshot.getValue(Friend.class);
                     TextView friend_text = new TextView(getApplicationContext());
                     friend_text.setText(friend.name);
                     friend_text.setTextSize(TypedValue.COMPLEX_UNIT_SP,22f);
                     friend_text.setTypeface(null, Typeface.BOLD);
                     friend_text.setOnClickListener(new View.OnClickListener(){

                         @Override
                         public void onClick(View v) {
                             Intent share_confirm_intent = new Intent(getApplicationContext(),Share_Confirmation.class);
                             user_bun.putString("Recipient",friend.name);
                             user_bun.putString("Recipient_un",friend.username);
                             share_confirm_intent.putExtra("User",user_bun);
                             startActivity(share_confirm_intent);
                         }
                     });
                     share_friend.addView(friend_text);
                 }catch(NullPointerException e){
                     TextView friend_text = new TextView(getApplicationContext());
                     friend_text.setText("No friends available");
                     friend_text.setTextSize(TypedValue.COMPLEX_UNIT_SP,22f);
                     friend_text.setTypeface(null, Typeface.BOLD);
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
        Intent display_intent = new Intent(getApplicationContext(),Video_Display.class);
        display_intent.putExtra("User",user_bun);
        startActivity(display_intent);
    }
}