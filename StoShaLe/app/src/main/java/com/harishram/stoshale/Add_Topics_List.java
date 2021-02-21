package com.harishram.stoshale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Add_Topics_List extends AppCompatActivity {

    LinearLayout topic_list;
    Bundle user_bun;
    String name,username,video,res;
    StorageReference fsr;
    DatabaseReference dbr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topics_list);
        getSupportActionBar().hide();
        user_bun = getIntent().getBundleExtra("User");
        name = user_bun.getString("Name");
        username = user_bun.getString("Username");
        video = user_bun.getString("Video");
        topic_list = (LinearLayout) findViewById(R.id.add_topic_ll);
        dbr = FirebaseDatabase.getInstance().getReference();
        fsr = FirebaseStorage.getInstance().getReference();
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
                if(res.equals("None")) {
                    TextView no_topic = new TextView(getApplicationContext());
                    no_topic.setText("No Topics are available");
                    no_topic.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f);
                    no_topic.setTypeface(null, Typeface.BOLD);
                    topic_list.addView(no_topic);
                }
                else {
                    String[] res_split = res.split("/");
                    for (int i = 0; i < res_split.length; i++) {
                        TextView res_info = new TextView(getApplicationContext());
                        res_info.setText(res_split[i]);
                        res_info.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f);
                        res_info.setTypeface(null, Typeface.BOLD);
                        int finali = i;
                        res_info.setOnClickListener(new View.OnClickListener(){

                            @Override
                            public void onClick(View v) {
                                Uri video_uri = Uri.fromFile(new File(System.getenv("EXTERNAL_STORAGE")+"/Videos/"+video));
                                Toast start = Toast.makeText(getApplicationContext(),"Adding Video...",Toast.LENGTH_SHORT);
                                start.show();
                                UploadTask upload = fsr.child(username).child(res_split[finali]).child(video).putFile(video_uri);
                                upload.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        Toast success = Toast.makeText(getApplicationContext(),"Video Sucessfully Added as a Resource",Toast.LENGTH_SHORT);
                                        success.show();
                                    }
                                });
                                Thread add_vid = new Thread(new Runnable(){

                                    @Override
                                    public void run() {
                                        try {
                                            Socket topic_creation = new Socket(address,2500);
                                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(topic_creation.getOutputStream()));
                                            bw.write("Create_Resource/"+username+"_"+res_info.getText().toString()+"/"+video.split(".mp4")[0]);
                                            bw.flush();
                                            topic_creation.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                add_vid.start();
                                try {
                                    add_vid.join();
                                }catch(InterruptedException e){
                                    e.printStackTrace();
                                }
                                Intent video_list_intent = new Intent(getApplicationContext(),Add_Videos_List.class);
                                user_bun = new Bundle();
                                user_bun.putString("Name",name);
                                user_bun.putString("Username",username);
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

    }

    @Override
    public void onBackPressed(){
        Intent video_display_intent = new Intent(this,Add_Video_Display.class);
        video_display_intent.putExtra("User",user_bun);
        startActivity(video_display_intent);
    }
}