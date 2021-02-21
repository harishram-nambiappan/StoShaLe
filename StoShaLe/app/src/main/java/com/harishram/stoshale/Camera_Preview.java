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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Camera_Preview extends AppCompatActivity {

    File notes_file;
    String notes_path;
    String[] notes_list;
    LinearLayout notes_ll;
    Bundle user_bun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);
        getSupportActionBar().hide();
        user_bun = getIntent().getBundleExtra("User");
        notes_ll = (LinearLayout) findViewById(R.id.notes_file_ll);
        notes_path = System.getenv("EXTERNAL_STORAGE")+"/Notes";
        notes_file = new File(notes_path);
        notes_list = notes_file.list();
        for(int i=0;i<notes_list.length;i++){
            TextView notes_text = new TextView(this);
            notes_text.setText(notes_list[i]);
            notes_text.setTextSize(TypedValue.COMPLEX_UNIT_SP,22f);
            notes_text.setTypeface(null, Typeface.BOLD);
            int finali = i;
            notes_text.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Intent preview_intent = new Intent(getApplicationContext(),Photo_Preview.class);
                    user_bun.putString("Notes",notes_list[finali]);
                    preview_intent.putExtra("User",user_bun);
                    startActivity(preview_intent);
                }
            });
            notes_ll.addView(notes_text);
        }

    }
    @Override
    public void onBackPressed(){
        Intent display_intent = new Intent(getApplicationContext(),Video_Display.class);
        display_intent.putExtra("User",user_bun);
        startActivity(display_intent);
    }
}