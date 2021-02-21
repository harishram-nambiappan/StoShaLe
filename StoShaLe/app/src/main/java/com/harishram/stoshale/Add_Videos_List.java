package com.harishram.stoshale;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

public class Add_Videos_List extends AppCompatActivity {

    LinearLayout vid_list;
    String video_dir_path;
    File video_dir_name;
    String[] video_files;
    Bundle user_bun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_resource_list);
        getSupportActionBar().hide();
        user_bun = getIntent().getBundleExtra("User");
        vid_list = (LinearLayout) findViewById(R.id.av_vid_ll);
        video_dir_path = System.getenv("EXTERNAL_STORAGE")+"/Videos";
        video_dir_name = new File(video_dir_path);
        video_files = video_dir_name.list();
        for(int i=0;i<video_files.length;i++){
            String video_name = video_files[i];
            TextView video_text = new TextView(this);
            video_text.setText(video_name.split(".mp4")[0]);
            video_text.setTextSize(TypedValue.COMPLEX_UNIT_SP,24f);
            video_text.setTypeface(null, Typeface.BOLD);
            video_text.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Intent video_display_intent = new Intent(getApplicationContext(),Add_Video_Display.class);
                    user_bun.putString("Video",video_name);
                    video_display_intent.putExtra("User",user_bun);
                    startActivity(video_display_intent);
                }
            });
            vid_list.addView(video_text);
        }

    }

    @Override
    public void onBackPressed(){
        Intent menu_intent = new Intent(getApplicationContext(),Menu.class);
        menu_intent.putExtra("User",user_bun);
        startActivity(menu_intent);
    }
}