package com.example.android_hw5;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

class CustomListAdapter implements ListAdapter {
    ArrayList<DataSnapshot> arrayList;
    Context context;
    private StorageReference mStorageRef;
    public CustomListAdapter(Context context, ArrayList<DataSnapshot> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
    @Override
    public boolean isEnabled(int position) {
        return true;
    }
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Note subjectData=arrayList.get(position).getValue(Note.class);
        if(convertView==null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView=layoutInflater.inflate(R.layout.adapter_view_layout, null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            final ImageView myimage = convertView.findViewById(R.id.ImageView);
            TextView tittle=convertView.findViewById(R.id.textView2);
            TextView contenttext=convertView.findViewById(R.id.textView3);
            TextView datetext=convertView.findViewById(R.id.textView4);
            TextView statustext=convertView.findViewById(R.id.textView5);
            Button EditButton =convertView.findViewById(R.id.SmallEditButton);
            Date date= new Date(subjectData.date);
            Date curr= new Date();

            float days = (float) ((curr.getTime()-subjectData.date)) / (1000*60*60*24);
            if(days>2)
            {
                EditButton.setVisibility(View.GONE);
                statustext.setText("Sent");
            }
            else{
                statustext.setText("Recieved");
            }
            datetext.setText(date.toString());
            Log.d("List", "Tried showing:"+subjectData.toString()+" Days are: "+days);
            tittle.setText(subjectData.title);
//            Uri imageuri = mStorageRef.child(FirebaseAuth.getInstance().getUid()+"/"+arrayList.get(position).getKey())
//                    .getDownloadUrl().getResult();
            Log.e("IMAGEDOWNLOAD:","TRYING TO GET: images/"+FirebaseAuth.getInstance().getUid()+"/"+arrayList.get(position).getKey());
            mStorageRef.child("images/"+FirebaseAuth.getInstance().getUid()+"/"+arrayList.get(position).getKey())
                    .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    Picasso.get().load(uri).into(myimage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Log.e("IMAGE URI:","ERROR GET REKT:");
                }
            });

            contenttext.setText(subjectData.getContent());
            EditButton.setOnClickListener(new NewListener(subjectData.title,subjectData.getContent()
            ,arrayList.get(position).getKey(),subjectData.date));
        }
        return convertView;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getViewTypeCount() {
        return 1;
    }
    @Override
    public boolean isEmpty() {
        return false;
    }

}
class NewListener implements View.OnClickListener
{
    String title;
    String content;
    String key;
    long date;
    public NewListener(String title,String content,String key,long date) {
        this.content=content;
        this.title=title;
        this.key=key;
        this.date = date;
    }

    @Override
    public void onClick(View v)
    {
        Intent intent = new Intent(v.getContext(), EditNoteActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("content",content);
        intent.putExtra("key",key);
        intent.putExtra("date",date);
        // intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
        v.getContext().startActivity(intent);
    }

}