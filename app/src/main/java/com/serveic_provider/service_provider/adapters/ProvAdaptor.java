package com.serveic_provider.service_provider.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.media.audiofx.AudioEffect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.serveic_provider.service_provider.ListProvidersActivity;
import com.serveic_provider.service_provider.ProfileActivity;
import com.serveic_provider.service_provider.R;
import com.serveic_provider.service_provider.serviceProvider.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


public class ProvAdaptor extends BaseAdapter {
    FirebaseStorage storage;
    StorageReference storageReference;

    Activity activity;
    List<User> users;
    LayoutInflater inflater;

    ImageView image;
    TextView username;
    TextView profession;
    RatingBar rate;
    TextView raterNum;
    TextView description;
    ImageView ivCheckBox;
    TextView profBtn;

    public ProvAdaptor(Activity activity) {
        this.activity = activity;
    }

    public ProvAdaptor(Activity activity, List<User> users) {
        this.activity   = activity;
        this.users      = users;
        inflater        = activity.getLayoutInflater();
    }


    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null){

            view = inflater.inflate(R.layout.service_listview, viewGroup, false);

            username = (TextView)view.findViewById(R.id.provider_name_text_view);
            profession = (TextView) view.findViewById(R.id.profession_text_view);
            image = (ImageView) view.findViewById(R.id.imageProf);
            rate = (RatingBar) view.findViewById(R.id.rating_bar);
            raterNum = (TextView) view.findViewById(R.id.number_of_reviews);
            description = (TextView) view.findViewById(R.id.serviceDesc);
            profBtn = (TextView)view.findViewById(R.id.profileBtn);

            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();

        }


        User user = users.get(i);

        username.setText(user.getName());
        profession.setText(user.getProfession());
        //image
        rate.setRating(user.getRate());
        raterNum.setText("");
        description.setText("");

        displayImage(user.getUid());
        setProfListener(user.getUid());

        if (user.isSelected())
            view.setBackgroundColor(Color.GRAY);
        else
            view.setBackgroundColor(Color.WHITE);

        return view;

    }

    public void updateRecords(List<User> users){
        this.users = users;

        notifyDataSetChanged();
    }
    public void setProfListener(final String uid){
        final Intent intent1 = new Intent(activity, ProfileActivity.class);
        profBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Bundle bundle1 = new Bundle();
                bundle1.putString("userId",uid);
                intent1.putExtras(bundle1);
               activity.startActivity(intent1);
            }
        });
    }

    public void displayImage(String userId){
        Log.w("imageLink", "inside dispMethod"+userId);
        storageReference.child("userImages/"+userId+"/profileImage").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.w("imageLink", uri.toString());
                if(uri!=null) {

                    // Got the download URL for ''
                    Log.w("imageLink", uri.toString());
                    String imgUrl = uri.toString();
                    Picasso.get()
                            .load(imgUrl)
                            .into(image);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }


}
