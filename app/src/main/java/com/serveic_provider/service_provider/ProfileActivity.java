package com.serveic_provider.service_provider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.serveic_provider.service_provider.adapters.RateAdaptor;
import com.serveic_provider.service_provider.serviceProvider.Rate;
import com.serveic_provider.service_provider.serviceProvider.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    //declare all view element
    TextView firstName;
    TextView lastName;
    TextView city;
    TextView phone;
    TextView type;
    RatingBar ratingBar;
    String userId = "";
    ArrayList<Rate> rateList = new ArrayList<Rate>();
    RateAdaptor adapter;
    pl.droidsonroids.gif.GifImageView spinner;
    ImageView profilepic;
    FirebaseStorage storage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.material_design_profile_screen_xml_ui_design);
        setTitle("Profile");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        rateList = new ArrayList<Rate>();



        //assign all view fields
        firstName = (TextView)findViewById(R.id.firstNameTextView);
        lastName = (TextView)findViewById(R.id.lastNameTextView);
        city = (TextView)findViewById(R.id.cityTextView);
        phone =(TextView)findViewById(R.id.phoneTextView);
        ratingBar =(RatingBar)findViewById(R.id.ratingBar2);
        type =(TextView)findViewById(R.id.typeTextView);
        profilepic = (ImageView)findViewById(R.id.profile_image);
        spinner = (pl.droidsonroids.gif.GifImageView)findViewById(R.id.progressBar1);

        spinner.setVisibility(View.VISIBLE);

        //this is the method to set an imge
        //profilepic.setImageResource(R.drawable.bakground);

        //get user id from last activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getString("userId");
        }//end of checking extras!=null
        if(!userId.equals("none")){
            buildProfile(userId);
        }
    }
    public void buildProfile(final String userId){

        //build user obj from db
        DatabaseReference userProfileRef_type;
        userProfileRef_type = mDatabase.getReference("user_profiles").child(userId);
        userProfileRef_type.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                displayImage(userId);
                setFields(user);
                buildCommentList();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public void displayImage(String userId){
        storageReference.child("userImages/"+userId+"/profileImage").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if(uri!=null) {

                    // Got the download URL for ''
                    Log.w("imageLink", uri.toString());
                    String imgUrl = uri.toString();
                    Picasso.get()
                            .load(imgUrl)
                            .into(profilepic);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }
    public boolean checkUser(String userId){
        //current user id
        String currnetUserId ;
        //auth table reference
        FirebaseAuth mAuth = FirebaseAuth.getInstance();;
        //user reference
        FirebaseUser FBuser;
        //get user
        FBuser = mAuth.getCurrentUser();
        //get id
        currnetUserId = FBuser.getUid();

        if(currnetUserId.equals(userId)){
            return true;
        }
        else{
            return false;
        }

    }
    public void setFields(User user){
        if(user.getName()!=null)
            firstName.setText(user.getName());
        if(user.getLastName()!=null)
            lastName.setText(user.getLastName());
        if(user.getLocation()!=null)
            city.setText(user.getLocation());
        if(user.getPhone_number()!=null)
            phone .setText(user.getPhone_number());
        if(user.getToken_id()!=null)
            type .setText(user.getType());

    ratingBar.setRating(user.getRate());
        spinner.setVisibility(View.GONE);

    }

    public void buildCommentList(){
        //get comments from db
        DatabaseReference userCommentRef;
        userCommentRef = mDatabase.getReference("rates").child(userId);
        userCommentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //getting the rate object
                    final Rate rate  = snapshot.getValue(Rate.class);
                    //gt the rater obj


                    DatabaseReference raterRef;
                    raterRef = mDatabase.getReference("user_profiles").child(rate.getRaterId());
                    raterRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                            User raterObj = dataSnapshot1.getValue(User.class);
                            rate.setRaterName(raterObj.getName());
                            rateList.add(rate);

                            ListView listView = (ListView) findViewById(R.id.rateList);
                            adapter = new RateAdaptor(ProfileActivity.this, rateList);
                            listView.setAdapter(adapter);
                            listView.setClickable(true);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });


                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        //check if current user opening his profile
        if(checkUser(userId)){
            getMenuInflater().inflate(R.menu.edit_profile,menu);
            return true;
        }
        else{
            return false;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.edit_profile_button){
            startActivity(new Intent(this,EditProfileActivity.class));
            finish();
        }

        return true;
    }
}