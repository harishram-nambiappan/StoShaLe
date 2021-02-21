package com.harishram.stoshale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class Share_Video_Display extends AppCompatActivity implements SurfaceHolder.Callback {

    String name,username,sender,filename;
    Bundle user_bun;
    SurfaceView res_display;
    SurfaceHolder res_sh;
    MediaPlayer res_mp;
    DatabaseReference dbr;
    File temp_file;
    StorageReference fsr;
    TextView add_resource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_video_display);
        getSupportActionBar().hide();
        name = getIntent().getBundleExtra("User").getString("Name");
        username = getIntent().getBundleExtra("User").getString("Username");
        sender = getIntent().getBundleExtra("User").getString("Sender");
        filename = getIntent().getBundleExtra("User").getString("Filename");
        dbr = FirebaseDatabase.getInstance().getReference();
        fsr = FirebaseStorage.getInstance().getReference();
        res_mp = new MediaPlayer();
        res_display = (SurfaceView) findViewById(R.id.surfaceView3);
        res_sh = res_display.getHolder();
        res_sh.addCallback(this);
        add_resource = (TextView) findViewById(R.id.textView44);
        add_resource.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                res_mp.stop();
                Intent share_topic_intent = new Intent(getApplicationContext(),Resource_Share_Topics_List.class);
                user_bun = new Bundle();
                user_bun.putString("Name",name);
                user_bun.putString("Username",username);
                user_bun.putString("Sender",sender);
                user_bun.putString("Filename",filename);
                user_bun.putString("Temp_file",temp_file.toString());
                share_topic_intent.putExtra("User",user_bun);
                startActivity(share_topic_intent);

            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        dbr.child("Users").child(username).child("Friends").child(sender).addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Friend friend = snapshot.getValue(Friend.class);
                try {
                    temp_file = File.createTempFile("temp_video",".mp4");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fsr.child(friend.username).child(filename).getFile(temp_file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        try {
                            res_mp.setDataSource(temp_file.toString());
                            res_mp.setDisplay(res_sh);
                            res_mp.prepare();
                            res_mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    res_mp.stop();
                                }
                            });
                            res_mp.start();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onBackPressed(){
        Intent res_not_intent = new Intent(getApplicationContext(),Resource_Notification_List.class);
        user_bun = new Bundle();
        user_bun.putString("Name",name);
        user_bun.putString("Username",username);
        res_not_intent.putExtra("User",user_bun);
        startActivity(res_not_intent);
    }
}