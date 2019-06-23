package com.example.android_hw5;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NoteActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    double sum_donation=0;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();

        //
        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        Note newnote = new Note("Buy cheese","We need cheese",(new Date()).getTime());
        DatabaseReference myRef = database.getReference("Notes/"+mAuth.getUid());
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.e("Count " ,""+dataSnapshot.getChildrenCount());
                ArrayList<DataSnapshot> notes = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    //Note post = postSnapshot.getValue(Note.class);
                    Log.e("Get Data", postSnapshot.getValue(Note.class).toString());
                    notes.add(postSnapshot);
                    Log.e("Data",postSnapshot.getKey());
                }
                CustomListAdapter customAdapter = new CustomListAdapter(NoteActivity.this, notes);
                listView.setAdapter(customAdapter);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("onCancelled", "Failed to read value.", error.toException());
            }
        });
//        DatabaseReference newChildRef = myRef.push();
//        String key = newChildRef.getKey();
//        myRef.child(key).setValue(newnote);
        //
        Log.d("asd", "onCreate: creating");
        setContentView(R.layout.activity_note);
        sum_donation=getIntent().getDoubleExtra("donation",0);
        Button SignoutButton = findViewById(R.id.SignoutButton);
        SignoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(NoteActivity.this, MainActivity.class);
                //Intent intent = new Intent(v.getContext(), EditNoteActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
        //recreate();
        Log.d("onresune", "onResume: RESUMED");
        listView = findViewById(R.id.simpleListView);
        Button EditButton = findViewById(R.id.EditNoteButton);
        EditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditWindow();
            }
        });

    }
    public void OnDonationsClick(View view) {
        Intent intent = new Intent(this, DonationActivity.class);
        intent.putExtra("donation",sum_donation);
        startActivity(intent);
    }
    public void openEditWindow(){
        Intent intent = new Intent(this, EditNoteActivity.class);
        //Intent intent = new Intent(v.getContext(), EditNoteActivity.class);
        intent.putExtra("IsCreateNote",true);

        // intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
        startActivity(intent);
    }

//        EditText et = findViewById(R.id.textView);
//
//        final SharedPreferences prefs =
//                PreferenceManager
//                        .getDefaultSharedPreferences(this);
//
//
//        String s = prefs.getString("text", "");
//        et.setText(s);
//
//        et.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                prefs.edit().putString("text", editable.toString()).commit();
//            }
//        });
//
//    }
}
