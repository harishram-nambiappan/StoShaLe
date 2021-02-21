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
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class User_List extends AppCompatActivity {

    ScrollView user_sv;
    LinearLayout user_ll;
    Bundle user_bun;
    String username;
    DatabaseReference dbr;
    ArrayList<TextView> user_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        getSupportActionBar().hide();
        user_bun = getIntent().getBundleExtra("User");
        username = user_bun.getString("Username");
        user_sv = (ScrollView) findViewById(R.id.sv_user);
        user_ll = (LinearLayout) findViewById(R.id.user_ll);
        user_list = new ArrayList();
        dbr = FirebaseDatabase.getInstance().getReference();
        dbr.child("Users").addChildEventListener(new ChildEventListener(){

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                if(!user.username.equals(username)){
                    TextView user_data = new TextView(getApplicationContext());
                    user_data.setText(user.name);
                    user_data.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f);
                    user_data.setTypeface(null, Typeface.BOLD);
                    user_data.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            user_bun.putString("Recipient",user.name);
                            user_bun.putString("Recipient_un",user.username);
                            Intent send_request_intent = new Intent(getApplicationContext(),Send_Request.class);
                            send_request_intent.putExtra("User",user_bun);
                            startActivity(send_request_intent);
                        }
                    });
                    user_list.add(user_data);
                    user_ll.addView(user_data);
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
        Intent menu_intent = new Intent(this,Menu.class);
        menu_intent.putExtra("User",user_bun);
        startActivity(menu_intent);
    }
}