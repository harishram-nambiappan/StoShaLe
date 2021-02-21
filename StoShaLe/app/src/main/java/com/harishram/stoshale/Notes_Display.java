package com.harishram.stoshale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
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

public class Notes_Display extends AppCompatActivity {

    String name,username,topic,video,file,result;
    Bundle user_bun;
    TextView notes;
    DatabaseReference dbr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_display);
        getSupportActionBar().hide();
        name = getIntent().getBundleExtra("User").getString("Name");
        username = getIntent().getBundleExtra("User").getString("Username");
        topic = getIntent().getBundleExtra("User").getString("Topic");
        video = getIntent().getBundleExtra("User").getString("Video");
        file = getIntent().getBundleExtra("User").getString("File");
        notes = (TextView) findViewById(R.id.textView53);
        dbr = FirebaseDatabase.getInstance().getReference();
        dbr.child("Users").child(username).child("address").addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String address = snapshot.getValue(String.class);
                Thread display = new Thread(new Runnable(){

                    @Override
                    public void run() {
                        try {
                            Socket disp_sock = new Socket(address,2800);
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(disp_sock.getOutputStream()));
                            bw.write(username+"/"+topic+"/"+video+"/"+file);
                            bw.flush();
                            BufferedReader br = new BufferedReader(new InputStreamReader(disp_sock.getInputStream()));
                            result = br.readLine();
                            disp_sock.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                display.start();
                try{
                    display.join();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                notes.setText(result);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed(){
         Intent notes_list_intent = new Intent(getApplicationContext(),Notes_List.class);
         user_bun = new Bundle();
         user_bun.putString("Name",name);
         user_bun.putString("Username",username);
         user_bun.putString("Topic",topic);
         user_bun.putString("Video",video);
         notes_list_intent.putExtra("User",user_bun);
         startActivity(notes_list_intent);

    }
}