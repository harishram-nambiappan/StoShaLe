package com.harishram.stoshale;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class Video_Display extends AppCompatActivity implements SurfaceHolder.Callback {

    StorageReference fsr;
    String name,username,topic,video;
    Bundle user_bun;
    SurfaceView video_display;
    TextView share,add,view;
    File temp_file;
    SurfaceHolder vsh;
    MediaPlayer mep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_display);
        getSupportActionBar().hide();
        user_bun = getIntent().getBundleExtra("User");
        name = getIntent().getBundleExtra("User").getString("Name");
        username = getIntent().getBundleExtra("User").getString("Username");
        topic = getIntent().getBundleExtra("User").getString("Topic");
        video = getIntent().getBundleExtra("User").getString("Video");
        video_display = (SurfaceView) findViewById(R.id.surfaceView2);
        vsh = video_display.getHolder();
        mep = new MediaPlayer();
        vsh.addCallback(this);
        share = (TextView) findViewById(R.id.textView39);
        add = (TextView) findViewById(R.id.textView37);
        view = (TextView) findViewById(R.id.textView38);
        fsr = FirebaseStorage.getInstance().getReference();
        add.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                   mep.stop();
                   Intent add_intent = new Intent(getApplicationContext(),Camera_Preview.class);
                   add_intent.putExtra("User",user_bun);
                   startActivity(add_intent);
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mep.stop();
                Intent view_intent = new Intent(getApplicationContext(),Notes_List.class);
                view_intent.putExtra("User",user_bun);
                startActivity(view_intent);
            }
        });
        share.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mep.stop();
                Intent share_intent = new Intent(getApplicationContext(),Share_Friends_List.class);
                share_intent.putExtra("User",user_bun);
                startActivity(share_intent);
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            temp_file = File.createTempFile("temp_video",".mp4");
        } catch (IOException e) {
            e.printStackTrace();
        }
        fsr.child(username).child(topic).child(video+".mp4").getFile(temp_file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                try {
                    mep.setDataSource(temp_file.toString());
                    mep.setDisplay(vsh);
                    mep.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mep.stop();
                        }
                    });
                    mep.prepare();
                    mep.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
        Intent video_list_intent = new Intent(this,My_Videos_List.class);
        mep.stop();
        user_bun = new Bundle();
        user_bun.putString("Name",name);
        user_bun.putString("Username",username);
        user_bun.putString("Topic",topic);
        video_list_intent.putExtra("User",user_bun);
        startActivity(video_list_intent);
    }
}