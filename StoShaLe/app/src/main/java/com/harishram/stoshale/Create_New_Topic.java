package com.harishram.stoshale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Create_New_Topic extends AppCompatActivity {

    EditText topic_text;
    TextView create;
    String topic,username;
    Bundle user_bun;
    DatabaseReference dbr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_topic);
        getSupportActionBar().hide();
        dbr = FirebaseDatabase.getInstance().getReference();
        user_bun = getIntent().getBundleExtra("User");
        username = user_bun.getString("Username");
        topic_text = (EditText) findViewById(R.id.editTextTextMultiLine2);
        create = (TextView) findViewById(R.id.textView34);

        create.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                topic = topic_text.getText().toString();
                dbr.child("Users").child(username).child("address").addValueEventListener(new ValueEventListener(){

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String address = snapshot.getValue(String.class);
                        System.out.println(address);
                        Create_Topic_Handler cth  = new Create_Topic_Handler(getApplicationContext(),user_bun);
                        cth.execute(address,username+"_"+topic);
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
        Intent topic_list_intent = new Intent(getApplicationContext(),My_Topics_List.class);
        topic_list_intent.putExtra("User",user_bun);
        startActivity(topic_list_intent);
    }
}