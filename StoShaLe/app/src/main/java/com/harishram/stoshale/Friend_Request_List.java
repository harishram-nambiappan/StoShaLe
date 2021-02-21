package com.harishram.stoshale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Friend_Request_List extends AppCompatActivity {

    Bundle user_bun;
    String username;
    DatabaseReference dbr;
    LinearLayout fr_ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request_list);
        getSupportActionBar().hide();
        user_bun = getIntent().getBundleExtra("User");
        username = getIntent().getBundleExtra("User").getString("Username");
        dbr = FirebaseDatabase.getInstance().getReference();
        fr_ll = (LinearLayout) findViewById(R.id.friend_request_ll);
        dbr.child("Users").child(username).child("Notifications").child("Friend Requests").addChildEventListener(new ChildEventListener(){

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    Friend_Request friend_request = snapshot.getValue(Friend_Request.class);
                    TextView request = new TextView(getApplicationContext());
                    request.setText(friend_request.name + " has sent you a friend request");
                    request.setTypeface(null, Typeface.BOLD);
                    request.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f);
                    request.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            user_bun.putString("Recipient", friend_request.name);
                            user_bun.putString("Recipient_un", friend_request.username);
                            Intent accept_friend_request_intent = new Intent(getApplicationContext(), Accept_Friend_Request.class);
                            accept_friend_request_intent.putExtra("User", user_bun);
                            startActivity(accept_friend_request_intent);
                        }
                    });
                    fr_ll.addView(request);
                }
                catch(NullPointerException e){
                    TextView request = new TextView(getApplicationContext());
                    request.setText("No Friend Request Available");
                    request.setTypeface(null, Typeface.BOLD);
                    request.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f);
                    fr_ll.addView(request);
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
        user_bun = getIntent().getBundleExtra("User");
        Intent notification_intent = new Intent(this,Notification_List.class);
        notification_intent.putExtra("User",user_bun);
        startActivity(notification_intent);
    }
}