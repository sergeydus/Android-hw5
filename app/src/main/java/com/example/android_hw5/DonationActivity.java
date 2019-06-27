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
import android.widget.Toast;

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
    //float total,total_perU;
    public float total,total_perU;
    private StorageReference donationSumRef;
    FirebaseDatabase mdataBase = FirebaseDatabase.getInstance();
    DatabaseReference myRef = mdataBase.getReference("Donation/");

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);
        addtotal();
        TextView tv=findViewById(R.id.total_donate);
        tv.setVisibility(View.GONE);
    }

    private void addtotal() {
        final DatabaseReference mdatabase=FirebaseDatabase.getInstance().getReference();
        mdatabase.child("Donation/").addValueEventListener
                (new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(FirebaseAuth.getInstance().getUid()).exists()) {
                            total_perU += Float.valueOf(dataSnapshot.child(FirebaseAuth.getInstance().getUid()+"/amount").getValue().toString());
                            //MainActivity.sumTotal += Float.valueOf(myRef.child("/Total").toString());//.//setValue(total);
                            MainActivity.sumTotal=Float.valueOf(dataSnapshot.child("/Total").getValue().toString());
                            Log.e("TOTAL",""+MainActivity.sumTotal);
                            TextView tv1=findViewById(R.id.total_donate);
                            tv1.setText(String.valueOf(MainActivity.sumTotal)+"₪");

                        }
                        else{
                            MainActivity.sumTotal=Float.valueOf(dataSnapshot.child("/Total").getValue().toString());
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    public void donate(View view){
        FirebaseDatabase dataBase = FirebaseDatabase.getInstance();

        EditText ed1= findViewById(R.id.donate);
        TextView tv1=findViewById(R.id.total_donate);
        TextView tv3=findViewById(R.id.enteryourdonation);
        Button b1= findViewById(R.id.clickDonate);
        GifImageView imageView= findViewById(R.id.dancing);
        TextView tv2= findViewById(R.id.thanks);
        if(!ed1.getText().toString().equals("")){
            float num1= Float.valueOf(ed1.getText().toString());
            ed1.setVisibility(View.GONE);
            b1.setVisibility(View.GONE);
            tv3.setVisibility(View.GONE);

            float sum=num1+total_perU;
            //float sumTotal =+ sum;
            Toast.makeText(DonationActivity.this,"sumTotal is "+String.valueOf(MainActivity.sumTotal), Toast.LENGTH_SHORT).show();
            MainActivity.sumTotal = MainActivity.sumTotal + num1;
            Toast.makeText(DonationActivity.this,"sumTotal is "+String.valueOf(MainActivity.sumTotal), Toast.LENGTH_SHORT).show();
            DatabaseReference dataRef = dataBase.getReference("Donation/"+FirebaseAuth.getInstance().getUid());
           // myRef.push();
            total_perU=sum;
            total= total+MainActivity.sumTotal;
            //Toast.makeText(DonationActivity.this,"sumTotal is "+String.valueOf(MainActivity.sumTotal), Toast.LENGTH_SHORT).show();
            dataRef.child("amount").setValue(total_perU);
            myRef.child("Total").setValue(total);
            tv1.setVisibility(View.VISIBLE);
            tv1.setText(String.valueOf(total)+"₪");
            imageView.setVisibility(View.VISIBLE);
            tv2.setVisibility(View.VISIBLE);

        }
    }
    public void ret(View view){
        Intent intent= new Intent(this, NoteActivity.class);
        startActivity(intent);
    }
}



