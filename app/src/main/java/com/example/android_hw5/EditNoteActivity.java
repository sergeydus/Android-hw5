package com.example.android_hw5;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.view.View;

public class EditNoteActivity extends AppCompatActivity {
    private String Title;
    private String Description;
    private boolean isCreateNote;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        findViewById(R.id.EditTextTitle).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        findViewById(R.id.DescriptionEditText).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        this.isCreateNote = getIntent().getBooleanExtra("IsCreateNote",false);
        if(!isCreateNote) {
            this.id = getIntent().getIntExtra("id", 10);
            Bundle bundle = getIntent().getExtras();
            this.Title = bundle.getString("title", "");
            this.Description = bundle.getString("content", "");
            Log.d("test", "id is " + id + " title is +" + Title + " content is " + Description);
            EditText EditTitleView = findViewById(R.id.EditTextTitle);
            EditText EditDescriptionView = findViewById(R.id.DescriptionEditText);
            EditTitleView.setText(Title);
            EditDescriptionView.setText(Description);
        }

    }
    public void SaveButtonClick(View view){
        EditText EditTitleView = findViewById(R.id.EditTextTitle);
        EditText EditDescriptionView = findViewById(R.id.DescriptionEditText);
        this.Title=EditTitleView.getText().toString();
        this.Description=EditDescriptionView.getText().toString();
        Log.d("SaveButtonClick", "Title="+this.Title);
        Log.d("SaveButtonClick", "Description="+this.Description);
        DBHelper db = new DBHelper(this);
        if(isCreateNote) {
            //db.add
            db.AddData(this.Title,this.Description,"");
            finish();
        }
        else{
            db.updateData(String.valueOf(this.id),this.Title,this.Description,"");
            finish();
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
