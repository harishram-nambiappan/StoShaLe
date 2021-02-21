package com.harishram.stoshale;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class Notification_List extends AppCompatActivity {

    TextView friend_requests,messages,resource_sharing;
    Bundle user_bun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        getSupportActionBar().hide();
        user_bun = getIntent().getBundleExtra("User");
        friend_requests = (TextView) findViewById(R.id.textView9);
        messages = (TextView) findViewById(R.id.textView10);
        resource_sharing = (TextView) findViewById(R.id.textView11);
        friend_requests.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent friend_request_list = new Intent(getApplicationContext(),Friend_Request_List.class);
                friend_request_list.putExtra("User",user_bun);
                startActivity(friend_request_list);
            }
        });
        messages.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent message_display_intent = new Intent(getApplicationContext(),Message_Display.class);
                message_display_intent.putExtra("User",user_bun);
                startActivity(message_display_intent);
            }
        });
        resource_sharing.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent resource_share_intent = new Intent(getApplicationContext(),Resource_Notification_List.class);
                resource_share_intent.putExtra("User",user_bun);
                startActivity(resource_share_intent);
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