package com.serveic_provider.service_provider;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.serveic_provider.service_provider.serviceProvider.User;

public class ProfileActivity extends AppCompatActivity {
    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    //declare all view element
    TextView firstName;
    TextView lastName;
    TextView city;
    TextView phone;
    String userId = "";

    Button editBtn;
    Button updateBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        //assign all view fields
        firstName = (TextView)findViewById(R.id.firstNameTextView);
        lastName = (TextView)findViewById(R.id.lastNameTextView);
        city = (TextView)findViewById(R.id.cityTextView);
        phone =(TextView)findViewById(R.id.phoneTextView);

        //get user id from last activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getString("userId");
        }//end of checking extras!=null
        if(!userId.equals("none")){
            buildProfile(userId);
        }
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
        firstName.setText(user.getName());
        lastName.setText(user.getLastName());
        city.setText(user.getLocation());
        phone .setText(user.getPhone_number());
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
        }

        return true;
    }
}
