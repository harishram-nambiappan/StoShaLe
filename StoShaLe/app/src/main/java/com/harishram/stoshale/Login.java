package com.harishram.stoshale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    EditText username_text,password_text;
    String username,password;
    TextView Register,Login;
    DatabaseReference dbr;
    Bundle user_bun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        username_text = (EditText) findViewById(R.id.editTextTextPersonName);
        password_text = (EditText) findViewById(R.id.editTextTextPassword);
        Login = (TextView) findViewById(R.id.textView12);
        Register = (TextView) findViewById(R.id.textView13);
        dbr = FirebaseDatabase.getInstance().getReference();
        user_bun = new Bundle();
        Login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                username = username_text.getText().toString();
                password = password_text.getText().toString();
                dbr.child("Users").child(username).addValueEventListener(new ValueEventListener(){

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            User data = snapshot.getValue(User.class);
                            if (data.password.equals(password)) {
                                Intent menu_intent = new Intent(getApplicationContext(), Menu.class);
                                user_bun.putString("Name", data.name);
                                user_bun.putString("Username", data.username);
                                menu_intent.putExtra("User", user_bun);
                                startActivity(menu_intent);
                            } else {
                                Toast error = Toast.makeText(getApplicationContext(), "Credentials are invalid. Try again", Toast.LENGTH_SHORT);
                                error.show();
                            }
                        }
                        catch(NullPointerException e){
                            Toast error = Toast.makeText(getApplicationContext(), "Credentials are invalid. Try again", Toast.LENGTH_SHORT);
                            error.show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        Register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent register_intent = new Intent(getApplicationContext(), Sign_Up.class);
                startActivity(register_intent);
            }
        });
    }
}