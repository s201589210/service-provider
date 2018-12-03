package com.serveic_provider.service_provider;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.serveic_provider.service_provider.serviceProvider.Service;

public class CreateServiceActivity extends AppCompatActivity {
    String requsterID;
    DatabaseReference requesterServicesRef;
    DatabaseReference userProfileRef_serviceCounter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String providderID = extras.getString("key");
            //The key argument here must match that used in the other activity

            TextView tv = (TextView) findViewById(R.id.textfield);


            ////insertService(providderID);

            tv.setText(providderID);


        }//end of checking extras!=null
    }//end of create
    public void insertService(String providderID){
        //auth table reference
        FirebaseAuth mAuth = FirebaseAuth.getInstance();;
        //user reference
        FirebaseUser FBuser;
        //requster_service reference
        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
         requesterServicesRef = mDatabase.getReference("requester_services");
        //get user
        FBuser = mAuth.getCurrentUser();
        //get id
        requsterID = FBuser.getUid();
        //user profile reference
        userProfileRef_serviceCounter = mDatabase.getReference("user_profiles").child(requsterID).child("serviceCounter");
        //service counter listener
        userProfileRef_serviceCounter.addListenerForSingleValueEvent(new ValueEventListener() {
            String serviceCounter;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                serviceCounter = dataSnapshot.getValue(String.class);
                //initiate new service object
                Service service = buildService(requsterID);
                //insert service to user id in the requster_services node
                requesterServicesRef.child(requsterID).child(serviceCounter).setValue(service)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            Integer intServiceCounter = Integer.parseInt(serviceCounter) + 1;
                            @Override
                            public void onSuccess(Void aVoid) {userProfileRef_serviceCounter.setValue(intServiceCounter+"");
                                Log.d("tag", "writeUserType:success");
                            }
                        });//end insertion refrence
            }//end of counter on data change listner

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });//end of counter listner


    }//end of inserting service

    public Service buildService(String userId){
        //service no values
        Service service = new Service();
        //setting all values
        service.setDescription("this is new service");
        return service;
    }


}
