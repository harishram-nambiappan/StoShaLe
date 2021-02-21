package com.harishram.stoshale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class Add_Video_Display extends AppCompatActivity implements SurfaceHolder.Callback {

    Bundle user_bun;
    String name,username,video;
    SurfaceView video_display;
    SurfaceHolder video_holder;
    MediaPlayer media_player;
    TextView add_video;
    StorageReference fsr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video_display);
        getSupportActionBar().hide();
        user_bun = getIntent().getBundleExtra("User");
        name = getIntent().getBundleExtra("User").getString("Name");
        username = getIntent().getBundleExtra("User").getString("Username");
        video = getIntent().getBundleExtra("User").getString("Video");
        fsr = FirebaseStorage.getInstance().getReference();
        video_display = (SurfaceView) findViewById(R.id.surfaceView);
        add_video = (TextView) findViewById(R.id.textView30);
        add_video.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                media_player.stop();
                Intent add_topic_intent = new Intent(getApplicationContext(),Add_Topics_List.class);
                add_topic_intent.putExtra("User",user_bun);
                startActivity(add_topic_intent);

            }
        });
        media_player = new MediaPlayer();
        video_holder = video_display.getHolder();
        video_holder.addCallback(this);
    }

    @Override
    public void onBackPressed(){
        Intent video_list_intent = new Intent(getApplicationContext(),Add_Videos_List.class);
        user_bun = new Bundle();
        user_bun.putString("Name",name);
        user_bun.putString("Username",username);
        media_player.stop();
        startActivity(video_list_intent);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            media_player.setDataSource(System.getenv("EXTERNAL_STORAGE")+"/Videos/"+video);
            media_player.setDisplay(video_holder);
            media_player.prepare();
            media_player.start();
            media_player.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){

                @Override
                public void onCompletion(MediaPlayer mp) {
                    media_player.stop();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}