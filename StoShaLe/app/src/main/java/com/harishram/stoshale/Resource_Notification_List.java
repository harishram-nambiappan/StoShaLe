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

public class Resource_Notification_List extends AppCompatActivity {

    String name,username;
    Bundle user_bun;
    DatabaseReference dbr;
    LinearLayout rs_ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_notification_list);
        getSupportActionBar().hide();
        user_bun = getIntent().getBundleExtra("User");
        name = getIntent().getBundleExtra("User").getString("Name");
        username = getIntent().getBundleExtra("User").getString("Username");
        rs_ll = (LinearLayout) findViewById(R.id.rs_ll);
        dbr = FirebaseDatabase.getInstance().getReference();
        dbr.child("Users").child(username).child("Notifications").child("Resource Share").addChildEventListener(new ChildEventListener(){

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                   Resource_share rs = snapshot.getValue(Resource_share.class);
                   TextView res_not = new TextView(getApplicationContext());
                   res_not.setText(rs.sender+" has shared a resource with you: "+rs.filename.split("/")[1]);
                   res_not.setTextSize(TypedValue.COMPLEX_UNIT_SP,22f);
                   res_not.setTypeface(null, Typeface.BOLD);
                   res_not.setOnClickListener(new View.OnClickListener(){

                       @Override
                       public void onClick(View v) {
                           Intent share_display_intent = new Intent(getApplicationContext(),Share_Video_Display.class);
                           user_bun.putString("Sender",rs.sender);
                           user_bun.putString("Filename",rs.filename);
                           share_display_intent.putExtra("User",user_bun);
                           startActivity(share_display_intent);
                       }
                   });
                   rs_ll.addView(res_not);
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
        Intent notification_intent = new Intent(getApplicationContext(),Notification_List.class);
        notification_intent.putExtra("User",user_bun);
        startActivity(notification_intent);
    }
}