package com.harishram.stoshale;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class Menu extends AppCompatActivity {

    TextView logout,Welcome,add_friend,notifications,ask_friend,add_resource,my_resource;
    Bundle user_bun;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().hide();
        user_bun = getIntent().getBundleExtra("User");
        name = getIntent().getBundleExtra("User").getString("Name");
        Welcome = (TextView) findViewById(R.id.textView);
        Welcome.setText("Welcome "+name);
        my_resource = (TextView) findViewById(R.id.textView2);
        add_resource = (TextView) findViewById(R.id.textView3);
        add_friend = (TextView) findViewById(R.id.textView4);
        ask_friend = (TextView) findViewById(R.id.textView5);
        notifications = (TextView) findViewById(R.id.textView6);
        logout = (TextView) findViewById(R.id.textView7);
        my_resource.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent topics_list_intent = new Intent(getApplicationContext(),My_Topics_List.class);
                topics_list_intent.putExtra("User",user_bun);
                startActivity(topics_list_intent);
            }
        });
        add_resource.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent add_resource_intent = new Intent(getApplicationContext(),Add_Videos_List.class);
                add_resource_intent.putExtra("User",user_bun);
                startActivity(add_resource_intent);
            }
        });
        add_friend.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent user_list_intent = new Intent(getApplicationContext(),User_List.class);
                user_list_intent.putExtra("User",user_bun);
                startActivity(user_list_intent);
            }
        });
        ask_friend.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent friend_list_intent = new Intent(getApplicationContext(),Friend_List.class);
                friend_list_intent.putExtra("User",user_bun);
                startActivity(friend_list_intent);
            }
        });
        notifications.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent notifications_intent = new Intent(getApplicationContext(),Notification_List.class);
                notifications_intent.putExtra("User",user_bun);
                startActivity(notifications_intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent logout_intent = new Intent(getApplicationContext(),Login.class);
                startActivity(logout_intent);

            }
        });
    }
}