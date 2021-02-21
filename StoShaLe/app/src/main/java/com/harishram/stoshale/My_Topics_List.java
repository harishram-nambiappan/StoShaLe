package com.harishram.stoshale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class My_Topics_List extends AppCompatActivity {

    LinearLayout topic_list;
    TextView create_topic;
    String username;
    Bundle user_bun;
    DatabaseReference dbr;
    String address,res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_resources_list);
        getSupportActionBar().hide();
        user_bun = getIntent().getBundleExtra("User");
        username = user_bun.getString("Username");
        dbr = FirebaseDatabase.getInstance().getReference();
        topic_list = (LinearLayout) findViewById(R.id.topic_ll);
        dbr.child("Users").child(username).child("address").addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String address = snapshot.getValue(String.class);
                Thread sock_conn = new Thread(new Runnable(){

                    @Override
                    public void run() {
                        try {
                            Socket get_topic = new Socket(address,2600);
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(get_topic.getOutputStream()));
                            bw.write("Get_Topic/"+username);
                            bw.flush();
                            BufferedReader br = new BufferedReader(new InputStreamReader(get_topic.getInputStream()));
                            res = br.readLine();
                            get_topic.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                sock_conn.start();
                try {
                    sock_conn.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(res.equals("None")){
                    TextView no_topic = new TextView(getApplicationContext());
                    no_topic.setText("No Topics are available");
                    no_topic.setTextSize(TypedValue.COMPLEX_UNIT_SP,22f);
                    no_topic.setTypeface(null, Typeface.BOLD);
                    topic_list.addView(no_topic);
                }
                else{
                    String[] res_split = res.split("/");
                    for(int i=0;i<res_split.length;i++){
                        TextView res_info = new TextView(getApplicationContext());
                        res_info.setText(res_split[i]);
                        res_info.setTextSize(TypedValue.COMPLEX_UNIT_SP,22f);
                        res_info.setTypeface(null,Typeface.BOLD);
                        int finalI = i;
                        res_info.setOnClickListener(new View.OnClickListener(){

                            @Override
                            public void onClick(View v) {
                               Intent video_list_intent = new Intent(getApplicationContext(),My_Videos_List.class);
                               user_bun.putString("Topic",res_split[finalI]);
                               video_list_intent.putExtra("User",user_bun);
                               startActivity(video_list_intent);
                            }
                        });
                        topic_list.addView(res_info);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        create_topic = (TextView) findViewById(R.id.textView32);
        create_topic.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent create_topic_intent = new Intent(getApplicationContext(),Create_New_Topic.class);
                create_topic_intent.putExtra("User",user_bun);
                startActivity(create_topic_intent);
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent menu_intent = new Intent(getApplicationContext(),Menu.class);
        menu_intent.putExtra("User",user_bun);
        startActivity(menu_intent);
    }
}