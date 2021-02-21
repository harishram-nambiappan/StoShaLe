package com.harishram.stoshale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Photo_Preview extends AppCompatActivity {

    File notes_path;
    TextView notes_text,save_file;
    String content,notes_file;
    String name,username,topic,video;
    DatabaseReference dbr;
    Bundle user_bun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);
        getSupportActionBar().hide();
        content = "";
        notes_file = getIntent().getBundleExtra("User").getString("Notes");
        notes_path = new File(System.getenv("EXTERNAL_STORAGE")+"/Notes/"+notes_file);
        name = getIntent().getBundleExtra("User").getString("Name");
        username = getIntent().getBundleExtra("User").getString("Username");
        video = getIntent().getBundleExtra("User").getString("Video");
        topic = getIntent().getBundleExtra("User").getString("Topic");
        notes_text = (TextView) findViewById(R.id.textView49);
        save_file = (TextView) findViewById(R.id.textView50);
        dbr = FirebaseDatabase.getInstance().getReference();
        try {
            BufferedReader br = new BufferedReader(new FileReader(notes_path));
            String temp = br.readLine();
            while(temp!=null){
                content += temp;
                temp = br.readLine();
            }
            notes_text.setText(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        save_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbr.child("Users").child(username).child("address").addValueEventListener(new ValueEventListener(){

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String address = snapshot.getValue(String.class);
                        Thread sock_save = new Thread(new Runnable(){

                            @Override
                            public void run() {
                                try {
                                    Socket sock = new Socket(address,2700);
                                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
                                    bw.write(username+"/"+topic+"/"+video+"/"+notes_file+"/"+content);
                                    bw.flush();
                                    sock.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        sock_save.start();
                        try {
                            sock_save.join();
                        }catch(InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent notes_list_intent = new Intent(getApplicationContext(),Camera_Preview.class);
                        user_bun = new Bundle();
                        user_bun.putString("Name",name);
                        user_bun.putString("Username",username);
                        user_bun.putString("Topic",topic);
                        user_bun.putString("Video",video);
                        notes_list_intent.putExtra("User",user_bun);
                        startActivity(notes_list_intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });



    }
    @Override
    public void onBackPressed(){
        Intent camera_intent = new Intent(getApplicationContext(),Camera_Preview.class);
        user_bun = new Bundle();
        user_bun.putString("Name",name);
        user_bun.putString("Username",username);
        user_bun.putString("Topic",topic);
        user_bun.putString("Video",video);
        camera_intent.putExtra("User",user_bun);
        startActivity(camera_intent);
    }
}