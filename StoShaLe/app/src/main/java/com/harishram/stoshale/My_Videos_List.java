package com.harishram.stoshale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
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

public class My_Videos_List extends AppCompatActivity {

    String name,username,topic;
    Bundle user_bun;
    DatabaseReference dbr;
    LinearLayout video_list;
    String vid_exp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_videos_list);
        getSupportActionBar().hide();
        user_bun = getIntent().getBundleExtra("User");
        name = getIntent().getBundleExtra("User").getString("Name");
        username = getIntent().getBundleExtra("User").getString("Username");
        topic = getIntent().getBundleExtra("User").getString("Topic");
        dbr = FirebaseDatabase.getInstance().getReference();
        video_list = (LinearLayout) findViewById(R.id.my_video_ll);
        dbr.child("Users").child(username).child("address").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String address = snapshot.getValue(String.class);
                Thread get_videos = new Thread(new Runnable(){

                    @Override
                    public void run() {
                        try {
                            Socket gv_sock = new Socket("192.168.43.100",2600);
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(gv_sock.getOutputStream()));
                            bw.write("Get_Videos/"+username+"_"+topic);
                            bw.flush();
                            BufferedReader br = new BufferedReader(new InputStreamReader(gv_sock.getInputStream()));
                            vid_exp = br.readLine();
                            gv_sock.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                get_videos.start();
                try{
                    get_videos.join();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                String[] vid_list = vid_exp.split("/");
                for(int i=0;i<vid_list.length;i++){
                    TextView vid_name = new TextView(getApplicationContext());
                    vid_name.setText(vid_list[i]);
                    vid_name.setTextSize(TypedValue.COMPLEX_UNIT_SP,22f);
                    vid_name.setTypeface(null, Typeface.BOLD);
                    int finali = i;
                    vid_name.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            Intent display_intent = new Intent(getApplicationContext(), Video_Display.class);
                            user_bun.putString("Video",vid_list[finali]);
                            display_intent.putExtra("User",user_bun);
                            startActivity(display_intent);
                        }
                    });
                    video_list.addView(vid_name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    @Override
    public void onBackPressed(){
        Intent topic_intent = new Intent(getApplicationContext(),My_Topics_List.class);
        topic_intent.putExtra("User",user_bun);
        startActivity(topic_intent);
    }
}