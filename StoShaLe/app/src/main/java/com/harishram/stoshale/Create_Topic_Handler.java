package com.harishram.stoshale;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

public class Create_Topic_Handler extends AsyncTask<String,Void,Void> {
    Context context;
    Bundle bundle;
    public Create_Topic_Handler(Context context, Bundle bundle){
        this.context = context;
        this.bundle = bundle;
    }
    @Override
    protected Void doInBackground(String... strings) {
        String address = strings[0];
        String path = strings[1];
        try {
            Socket topic_creation = new Socket(address,2500);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(topic_creation.getOutputStream()));
            bw.write("Create_Topic/"+path);
            bw.flush();
            topic_creation.close();
            Intent topics_list_intent = new Intent(context,My_Topics_List.class);
            topics_list_intent.putExtra("User",bundle);
            context.startActivity(topics_list_intent);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
