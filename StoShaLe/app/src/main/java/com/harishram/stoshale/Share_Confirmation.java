package com.harishram.stoshale;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Share_Confirmation extends AppCompatActivity {

    String name,username,topic,video,recipient,recipient_un;
    Bundle user_bun;
    DatabaseReference dbr;
    TextView confirm_text,confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_confirmation);
        getSupportActionBar().hide();
        name = getIntent().getBundleExtra("User").getString("Name");
        username = getIntent().getBundleExtra("User").getString("Username");
        topic = getIntent().getBundleExtra("User").getString("Topic");
        video = getIntent().getBundleExtra("User").getString("Video");
        recipient = getIntent().getBundleExtra("User").getString("Recipient");
        recipient_un = getIntent().getBundleExtra("User").getString("Recipient_un");
        confirm_text = (TextView) findViewById(R.id.textView42);
        confirm_text.setText("Confirm sharing the video file with "+recipient+" ?");
        confirm = (TextView) findViewById(R.id.textView43);
        dbr = FirebaseDatabase.getInstance().getReference();
        confirm.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-YYYY_HH:mm:ss");
                String time_entry = sdf.format(new Date());
                Resource_share rs = new Resource_share(time_entry,name,topic+"/"+video+".mp4");
                dbr.child("Users").child(recipient_un).child("Notifications").child("Resource Share").child(time_entry).setValue(rs);
                Intent share_friends = new Intent(getApplicationContext(),Share_Friends_List.class);
                user_bun = new Bundle();
                user_bun.putString("Name",name);
                user_bun.putString("Username",username);
                user_bun.putString("Topic",topic);
                user_bun.putString("Video",video);
                share_friends.putExtra("User",user_bun);
                startActivity(share_friends);
            }
        });

    }
    @Override
    public void onBackPressed(){
        Intent share_friends_intent = new Intent(getApplicationContext(),Share_Friends_List.class);
        user_bun = new Bundle();
        user_bun.putString("Name",name);
        user_bun.putString("Username",username);
        user_bun.putString("Topic",topic);
        user_bun.putString("Video",video);
        share_friends_intent.putExtra("User",user_bun);
        startActivity(share_friends_intent);
    }
}