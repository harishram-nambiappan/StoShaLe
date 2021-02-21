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

public class Notes_List extends AppCompatActivity {

    Bundle user_bun;
    LinearLayout notes_list;
    String name,username,topic,video,files;
    DatabaseReference dbr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        getSupportActionBar().hide();
        user_bun = getIntent().getBundleExtra("User");
        name = getIntent().getBundleExtra("User").getString("Name");
        username = getIntent().getBundleExtra("User").getString("Username");
        topic = getIntent().getBundleExtra("User").getString("Topic");
        video = getIntent().getBundleExtra("User").getString("Video");
        notes_list = (LinearLayout) findViewById(R.id.res_notes_ll);
        dbr = FirebaseDatabase.getInstance().getReference();
        dbr.child("Users").child(username).child("address").addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String address = snapshot.getValue(String.class);
                Thread get_list = new Thread(new Runnable(){

                    @Override
                    public void run() {
                        try {
                            Socket get_sock = new Socket(address,2800);
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(get_sock.getOutputStream()));
                            bw.write(username+"/"+topic+"/"+video);
                            bw.flush();
                            BufferedReader br = new BufferedReader(new InputStreamReader(get_sock.getInputStream()));
                            files = br.readLine();
                            get_sock.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                get_list.start();
                try{
                    get_list.join();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                if(files.equals("None")){
                    TextView no_file = new TextView(getApplicationContext());
                    no_file.setText("No notes available");
                    no_file.setTextSize(TypedValue.COMPLEX_UNIT_SP,22f);
                    no_file.setTypeface(null, Typeface.BOLD);
                    notes_list.addView(no_file);
                }
                else{
                    String[] file_list = files.split("/");
                    for(int i=0;i<file_list.length;i++){
                        TextView file_info = new TextView(getApplicationContext());
                        file_info.setText(file_list[i]);
                        file_info.setTextSize(TypedValue.COMPLEX_UNIT_SP,22f);
                        file_info.setTypeface(null,Typeface.BOLD);
                        int finali = i;
                        file_info.setOnClickListener(new View.OnClickListener(){

                            @Override
                            public void onClick(View v) {
                                Intent file_content_intent = new Intent(getApplicationContext(),Notes_Display.class);
                                user_bun.putString("File",file_list[finali]);
                                file_content_intent.putExtra("User",user_bun);
                                startActivity(file_content_intent);
                            }
                        });
                        notes_list.addView(file_info);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onBackPressed(){
        Intent video_intent = new Intent(this,Video_Display.class);
        video_intent.putExtra("User",user_bun);
        startActivity(video_intent);
    }
}