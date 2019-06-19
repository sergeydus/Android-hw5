package com.example.android_hw5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public String username = "admin";
    public String password = "admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button SubmitButton = findViewById(R.id.SubmitButton);
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usernameView= findViewById(R.id.username_edittext);
                EditText passView= findViewById(R.id.password_edittext);
                String userpass = passView.getText().toString();
                String userid = usernameView.getText().toString();
                if(userid.equals(username)&&userpass.equals(password)) {
                    Log.d("notes", "LOGIN GOOD" + userid + userpass + username);
                    openNoteActivity();
                }
                else{
                    Toast.makeText(MainActivity.this, "try: admin, admin", Toast.LENGTH_SHORT).show();
                    Log.d("notes", "LOGIN BAD" + userid + userpass + username);
                }
            }
        });

    }
    public void openNoteActivity(){
        Intent intent = new Intent(this, NoteActivity.class);
        //Intent intent = new Intent(v.getContext(), EditNoteActivity.class);
        startActivity(intent);
    }
}
