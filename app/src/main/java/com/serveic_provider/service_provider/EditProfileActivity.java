package com.serveic_provider.service_provider;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.serveic_provider.service_provider.serviceProvider.User;

public class EditProfileActivity extends AppCompatActivity {

    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    //user id
    String userId ;
    //declare all view element
    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText cityEditText;
    EditText phone;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setTitle("Edit Profile");


        //assign all view fields
        firstNameEditText = (EditText)findViewById(R.id.edit_profile_first_name_edit_text);
        lastNameEditText = (EditText)findViewById(R.id.edit_profile_last_name_edit_text);
        cityEditText = (EditText)findViewById(R.id.edit_profile_city_edit_text);
        phone =(EditText)findViewById(R.id.edit_profile_phone_edit_text);

        //auth table reference
        FirebaseAuth mAuth = FirebaseAuth.getInstance();;
        //user reference
        FirebaseUser FBuser;
        //get user
        FBuser = mAuth.getCurrentUser();
        //get id
        userId = FBuser.getUid();

        buildProfile(userId);
    }

    public void buildProfile(String userId){
        //build user obj from db
        DatabaseReference userProfileRef_type;
        userProfileRef_type = mDatabase.getReference("user_profiles").child(userId);
        userProfileRef_type.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                setFields(user);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public void setFields(User user){
        firstNameEditText.setText(user.getName());
        lastNameEditText.setText(user.getLastName());
        cityEditText.setText(user.getLocation());
        phone .setText(user.getPhone_number());
    }

    public void saveChanges() {
        user.setName(firstNameEditText.getText().toString());
        user.setLastName(lastNameEditText.getText().toString());
        user.setLocation(cityEditText.getText().toString());
        user.setPhone_number(phone.getText().toString());

        DatabaseReference userProfileRef = mDatabase.getReference("user_profiles");
        userProfileRef.child(userId).setValue(user);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_profile_changes,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.save_profile_button){
            saveChanges();
            finish();
        }

        return true;
    }
}
