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

import java.util.ArrayList;
import java.util.Date;

public class NoteActivity extends AppCompatActivity {

    double sum_donation=0;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("asd", "onCreate: creating");
        setContentView(R.layout.activity_note);
        sum_donation=getIntent().getDoubleExtra("donation",0);
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
        DBHelper db = new DBHelper(this);
        /*for(int i=0;i<5;i++){
            Note p = new Note(i,
                    "title"+i,
                    "content"+i);
            db.getWritableDatabase().execSQL(p.getSQLInsertString());
        }*/
        //db.updateData("1","nigger","wapon","");
        ArrayList<Note> notes = new ArrayList<>();
        Cursor c = db.getReadableDatabase().rawQuery(Note.SELECT_ALL,
                null);

        c.moveToFirst();
        while(!c.isAfterLast()){
            Note p = new Note(c);
            notes.add(p);
            c.moveToNext();
        }

        Log.i("notes", notes.size() + "");

        CustomListAdapter customAdapter = new CustomListAdapter(this, notes);
        listView.setAdapter(customAdapter);
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
