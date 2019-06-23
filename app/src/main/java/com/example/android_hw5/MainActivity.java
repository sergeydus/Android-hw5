package com.example.android_hw5;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Date;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    public String username = "admin";
    public String password = "admin";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        //test
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        Note newnote = new Note(1,"Buy cheese","We need cheese",(new Date()).getTime());
//        DatabaseReference myRef = database.getReference("Notes/"+mAuth.getUid());
//        DatabaseReference newChildRef = myRef.push();
//        String key = newChildRef.getKey();
//        myRef.child(key).setValue(newnote);


        //
        Log.d("oncreate:",mAuth.toString());
        setContentView(R.layout.activity_main);
        Button SubmitButton = findViewById(R.id.SubmitButton);
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usernameView= findViewById(R.id.username_edittext);
                EditText passView= findViewById(R.id.password_edittext);
                String userpass = passView.getText().toString();
                String userid = usernameView.getText().toString();
                mAuth.signInWithEmailAndPassword(userid, userpass)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("Login:", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if(user!=null){
                                        openNoteActivity();
                                    }
                                    //updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("Login", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }

                                // ...
                            }
                        });
            }
        });
        Button RegisterButton = findViewById(R.id.RegisterButton);
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usernameView= findViewById(R.id.username_edittext);
                EditText passView= findViewById(R.id.password_edittext);
                String userpass = passView.getText().toString();
                String userid = usernameView.getText().toString();
                if(userpass.equals("")||userid.equals("")){
                    Toast.makeText(MainActivity.this, "Email or password missing.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(userid, userpass)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("Register:", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if(user!=null){
                                        openNoteActivity();
                                    }
                                    //updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("Register:", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(MainActivity.this, "Authentication failed.",
                                     //       Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }

                                // ...
                            }
                        });
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        if(currentUser!=null) {
            Log.d("onStart:", currentUser.toString());
            openNoteActivity();
        }
    }
    public void openNoteActivity(){
        Intent intent = new Intent(this, NoteActivity.class);
        //Intent intent = new Intent(v.getContext(), EditNoteActivity.class);
        startActivity(intent);
    }
}
