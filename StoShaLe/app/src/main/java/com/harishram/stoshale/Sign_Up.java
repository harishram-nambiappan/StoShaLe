package com.harishram.stoshale;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Sign_Up extends AppCompatActivity {

    EditText name_text,email_text,username_text,password_text,re_password,address_text;
    String name,email,username,password,address;
    TextView register;
    DatabaseReference dbr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up);
        getSupportActionBar().hide();
        name_text = (EditText) findViewById(R.id.editTextTextPersonName2);
        email_text = (EditText) findViewById(R.id.editTextTextEmailAddress);
        username_text = (EditText) findViewById(R.id.editTextTextPersonName3);
        password_text = (EditText) findViewById(R.id.editTextTextPassword2);
        re_password = (EditText) findViewById(R.id.editTextTextPassword3);
        address_text = (EditText) findViewById(R.id.editTextTextPersonName4);
        register = (TextView) findViewById(R.id.textView15);
        register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                name = name_text.getText().toString();
                email = email_text.getText().toString();
                username = username_text.getText().toString();
                password = password_text.getText().toString();
                address = address_text.getText().toString();
                if(!password.equals(re_password.getText().toString())){
                    Toast no_match = Toast.makeText(getApplicationContext(),"Passwords do not match. Try Again",Toast.LENGTH_SHORT);
                    no_match.show();
                }
                else{
                    dbr = FirebaseDatabase.getInstance().getReference();
                    System.out.println(dbr);
                    User user = new User(name,email,username,password,address);
                    dbr.child("Users").child(username).setValue(user);
                    Toast success = Toast.makeText(getApplicationContext(),"Your registration was successful",Toast.LENGTH_SHORT);
                    success.show();
                    Intent login_intent = new Intent(getApplicationContext(),Login.class);
                    startActivity(login_intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent login_intent = new Intent(this, Login.class);
        startActivity(login_intent);
    }
}