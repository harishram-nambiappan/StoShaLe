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

public class Friend_List extends AppCompatActivity {

    LinearLayout friends_list;
    Bundle user_bun;
    String username;
    DatabaseReference dbr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        getSupportActionBar().hide();
        user_bun = getIntent().getBundleExtra("User");
        friends_list = (LinearLayout) findViewById(R.id.lof_ll);
        username = getIntent().getBundleExtra("User").getString("Username");
        dbr = FirebaseDatabase.getInstance().getReference();
        dbr.child("Users").child(username).child("Friends").addChildEventListener(new ChildEventListener(){

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    Friend friend = snapshot.getValue(Friend.class);
                    TextView friend_name = new TextView(getApplicationContext());
                    friend_name.setText(friend.name);
                    friend_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f);
                    friend_name.setTypeface(null, Typeface.BOLD);
                    friend_name.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent message_enter_intent = new Intent(getApplicationContext(), Message_Enter.class);
                            user_bun.putString("Recipient", friend.name);
                            user_bun.putString("Recipient_un", friend.username);
                            message_enter_intent.putExtra("User", user_bun);
                            startActivity(message_enter_intent);
                        }
                    });
                    friends_list.addView(friend_name);
                }
                catch(NullPointerException e){
                    TextView friend_name = new TextView(getApplicationContext());
                    friend_name.setText("No Friends Available");
                    friend_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f);
                    friend_name.setTypeface(null, Typeface.BOLD);
                    friends_list.addView(friend_name);
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
         Intent menu_intent = new Intent(this,Menu.class);
         menu_intent.putExtra("User",getIntent().getBundleExtra("User"));
         startActivity(menu_intent);
    }
}