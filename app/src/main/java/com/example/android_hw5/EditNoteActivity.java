package com.example.android_hw5;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.URI;
import java.util.Date;

public class EditNoteActivity extends AppCompatActivity {
    private String Title;
    private String Description;
    private boolean isCreateNote;
    private long date;
    private String key;
    public static final int PICK_IMAGE = 1;
    private Uri imageuri;
    private StorageReference mStorageRef;
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
        mStorageRef = FirebaseStorage.getInstance().getReference();
        this.isCreateNote = getIntent().getBooleanExtra("IsCreateNote",false);
        Bundle bundle = getIntent().getExtras();
        this.date = bundle.getLong("date", (new Date()).getTime());
        this.Title = bundle.getString("title", "");
        this.Description = bundle.getString("content", "");
        if(!isCreateNote) {
            this.key = bundle.getString("key", "");
//            this.date = bundle.getLong("date", (new Date()).getTime());
            Log.d("test", " title is +" + Title + " content is " + Description);
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
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Note newnote = new Note(this.Title,this.Description,this.date);
        if(!isCreateNote){
            DatabaseReference myRef = database.getReference("Notes/"+ FirebaseAuth.getInstance().getUid());
            myRef.child(this.key).setValue(newnote);
    //        DatabaseReference newChildRef = myRef.push();
    //        String key = newChildRef.getKey();
    //        myRef.child(key).setValue(newnote);

        }
        else{
            DatabaseReference myRef = database.getReference("Notes/"+ FirebaseAuth.getInstance().getUid());
            DatabaseReference newChildRef = myRef.push();
            this.key = newChildRef.getKey();
            myRef.child(this.key).setValue(newnote);
        }
            if(imageuri !=null){
                StorageReference riversRef = mStorageRef.child(
                        "images/"+ FirebaseAuth.getInstance().getUid()+"/"+this.key);

                riversRef.putFile(imageuri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
//                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                Log.e("ImageUpload","Success");
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                                Log.e("ImageUpload","FAIL:"+exception.getMessage());
                                finish();
                            }
                        });
            }
            else{
                finish();
            }
    }
    public void uploadImage(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.e("UPLOAD IMAGE:","ON ACTIVITY RESULT");
        if (requestCode == PICK_IMAGE && data!= null) {
            Log.e("UPLOAD IMAGE:",data.getData().toString());
            ImageView myimage = findViewById(R.id.UploadImageView);
            myimage.setImageURI(data.getData());
            this.imageuri=data.getData();
        }
        else{
            Log.e("UPLOAD IMAGE:","NULL@@@@@@!@!@@!!@");
            this.imageuri=null;
        }

    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
