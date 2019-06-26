package com.example.android_hw5;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import pl.droidsonroids.gif.GifImageView;

public class DonationActivity extends AppCompatActivity {
    float total,total_perU;
    private StorageReference donationSumRef;
    FirebaseDatabase mdataBase = FirebaseDatabase.getInstance();
    DatabaseReference myRef = mdataBase.getReference("Donation/");
    private  String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);
        donationSumRef = FirebaseStorage.getInstance().getReference();
        myRef.child("Total").setValue(total);
        Bundle bundle=getIntent().getExtras();
        this.key=bundle.getString("key","");
        this.total=bundle.getFloat("sum_donation",(float)0);
        final DatabaseReference mdatabase=FirebaseDatabase.getInstance().getReference();
        mdatabase.child("Donation/"+FirebaseAuth.getInstance().getUid()).addValueEventListener
                (new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            Log.e("tago",dataSnapshot.child("/amount").getValue().toString());
                            total_perU += Float.valueOf(dataSnapshot.child("/amount").getValue().toString());
                            total+=total_perU;
                        }
                        else
                            return;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        TextView tv=findViewById(R.id.total_donate);
        tv.setVisibility(View.GONE);
    }


    public void donate(View view){
        FirebaseDatabase dataBase = FirebaseDatabase.getInstance();

        EditText ed1= findViewById(R.id.donate);
        TextView tv1=findViewById(R.id.total_donate);
        TextView tv3=findViewById(R.id.enteryourdonation);
        Button b1= findViewById(R.id.clickDonate);
        GifImageView imageView= findViewById(R.id.dancing);
        TextView tv2= findViewById(R.id.thanks);
        float num1= Float.valueOf(ed1.getText().toString());

        ed1.setVisibility(View.GONE);
        b1.setVisibility(View.GONE);
        tv3.setVisibility(View.GONE);

        float sum=num1+total_perU;
        DatabaseReference dataRef = dataBase.getReference("Donation/"+FirebaseAuth.getInstance().getUid());
        DatabaseReference newChildRef = dataRef.push();
        this.key=newChildRef.getKey();
        total_perU=sum;
        dataRef.child("amount").setValue(total_perU);
        tv1.setVisibility(View.VISIBLE);
        tv1.setText(String.valueOf(sum)+"₪");
        imageView.setVisibility(View.VISIBLE);
        tv2.setVisibility(View.VISIBLE);

    }
    public void ret(View view){
        Intent intent= new Intent(this, NoteActivity.class);
        startActivity(intent);
    }
}
