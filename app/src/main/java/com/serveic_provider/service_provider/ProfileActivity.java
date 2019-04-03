package com.serveic_provider.service_provider;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.serveic_provider.service_provider.adapters.ProvAdaptor;
import com.serveic_provider.service_provider.adapters.RateAdaptor;
import com.serveic_provider.service_provider.adapters.ServiceAdapter;
import com.serveic_provider.service_provider.fragments.InProgressFragment;
import com.serveic_provider.service_provider.serviceProvider.Comment;
import com.serveic_provider.service_provider.serviceProvider.Rate;
import com.serveic_provider.service_provider.serviceProvider.Service;
import com.serveic_provider.service_provider.serviceProvider.User;

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
    ArrayList<String> favouriteProviderIdList = new ArrayList<String>();
    pl.droidsonroids.gif.GifImageView spinner;
    de.hdodenhof.circleimageview.CircleImageView profilepic;
    android.support.v4.widget.SwipeRefreshLayout pullToRefresh ;
    LikeButton heart_botton;
    boolean isFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.material_design_profile_screen_xml_ui_design);
        setTitle("Profile");
        rateList = new ArrayList<Rate>();

        pullToRefresh=(android.support.v4.widget.SwipeRefreshLayout) findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                buildProfile(userId);
                pullToRefresh.setRefreshing(false);
            }
        });


        //assign all view fields
        firstName = (TextView)findViewById(R.id.firstNameTextView);
        lastName = (TextView)findViewById(R.id.lastNameTextView);
        city = (TextView)findViewById(R.id.cityTextView);
        phone =(TextView)findViewById(R.id.phoneTextView);
        ratingBar =(RatingBar)findViewById(R.id.ratingBar2);
        type =(TextView)findViewById(R.id.typeTextView);
        profilepic = (de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.profile_image);
        spinner = (pl.droidsonroids.gif.GifImageView)findViewById(R.id.progressBar1);
        heart_botton = findViewById(R.id.heart_button);

        spinner.setVisibility(View.VISIBLE);

        //this is themethod to set an imge
        //profilepic.setImageResource(R.drawable.bakground);

        
        //get user id from last activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getString("userId");
            Log.e("sddsa",userId);
        }//end of checking extras!=null
        if(!userId.equals("none")){
            buildProfile(userId);
        }
        //remove the favorite botton "heart_botton" from the original user
        if (checkUser(userId)){
            heart_botton.setVisibility(View.GONE);
        }

        // if the provider is a favourite


        heart_botton.setLiked(isFavourite(userId));


        heart_botton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                addToFavourite(userId);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                deleteFromFavourite(userId);

            }
        });

    }

    private boolean isFavourite(final String userId) {
        //auth table reference
        FirebaseAuth mAuth = FirebaseAuth.getInstance();;
        //user reference
        FirebaseUser FBuser;
        //user_profiles reference
        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        //get user
        FBuser = mAuth.getCurrentUser();
        //get id
        final String requsterID = FBuser.getUid();

        DatabaseReference userProfileRef;
        userProfileRef = mDatabase.getReference().child("user_profiles").child(requsterID);
        userProfileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //construct new user === firebase user
                User user = dataSnapshot.getValue(User.class);
                user.setUid(requsterID);
                favouriteProviderIdList = user.getFavourite_provider_ids();
                isFav = favouriteProviderIdList.contains(userId);
                Log.d("tag", String.valueOf(isFav));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return isFav;
    }

    private void deleteFromFavourite(final String userId) {
        //auth table reference
        FirebaseAuth mAuth = FirebaseAuth.getInstance();;
        //user reference
        FirebaseUser FBuser;
        //user_profiles reference
        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        //get user
        FBuser = mAuth.getCurrentUser();
        //get id
        final String requsterID = FBuser.getUid();

        DatabaseReference userProfileRef;
        userProfileRef = mDatabase.getReference("user_profiles").child(userId);
        userProfileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //construct new user === firebase user
                User user = dataSnapshot.getValue(User.class);
                user.setUid(requsterID);
                favouriteProviderIdList = user.getFavourite_provider_ids();
                String provId;
                for(int i =1;i <favouriteProviderIdList.size();i++){
                    provId = favouriteProviderIdList.get(i);
                    if(provId.equals(userId)) {
                        favouriteProviderIdList.remove(provId);
                        mDatabase.getReference().child("user_profiles").child(requsterID).child("favourite_provider_ids").setValue(favouriteProviderIdList);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addToFavourite(final String userId) {
        //auth table reference
        FirebaseAuth mAuth = FirebaseAuth.getInstance();;
        //user reference
        FirebaseUser FBuser;
        //user_profiles reference
        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        //get user
        FBuser = mAuth.getCurrentUser();
        //get id
        final String requsterID = FBuser.getUid();

        DatabaseReference userProfileRef;
        userProfileRef = mDatabase.getReference().child("user_profiles").child(requsterID);
        userProfileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //construct new user === firebase user
                User user = dataSnapshot.getValue(User.class);
                user.setUid(requsterID);
                favouriteProviderIdList = user.getFavourite_provider_ids();
                favouriteProviderIdList.add(userId);
                mDatabase.getReference().child("user_profiles").child(requsterID).child("favourite_provider_ids").setValue(favouriteProviderIdList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public void buildProfile(String userId){

        //build user obj from db
        DatabaseReference userProfileRef_type;
        userProfileRef_type = mDatabase.getReference("user_profiles").child(userId);
        userProfileRef_type.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                setFields(user);
                buildCommentList();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
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
                    raterRef = mDatabase.getReference("user_profiles").child(userId);
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
