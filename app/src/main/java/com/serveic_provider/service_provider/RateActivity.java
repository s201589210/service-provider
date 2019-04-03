package com.serveic_provider.service_provider;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.serveic_provider.service_provider.serviceProvider.Rate;
import com.serveic_provider.service_provider.serviceProvider.Service;

public class RateActivity extends AppCompatActivity {
    DatabaseReference userRateRef;
    DatabaseReference userRates;
    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    String userId;
    Rate rate;
    String serviceId;
    int entrdRate;
    String comment;
    Service service;

    Button rateButton ;
    TextView rateHintsTextView ;
    TextView commentView;
    RatingBar ratingBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        setTitle("Rate");


        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        rateButton = (Button)findViewById(R.id.rateBtn);
        rateHintsTextView = (TextView)findViewById(R.id.ratingHint);
        commentView = (TextView)findViewById(R.id.commentView);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //getting the service from extra
            service = (Service)extras.getSerializable("service");
        }//end of checking extras!=null

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rateHintsTextView.setText(String.valueOf(v));
                switch ((int) ratingBar.getRating()) {
                    case 1:
                        rateHintsTextView.setText("Very bad");
                        break;
                    case 2:
                        rateHintsTextView.setText("Need some improvement");
                        break;
                    case 3:
                        rateHintsTextView.setText("Good");
                        break;
                    case 4:
                        rateHintsTextView.setText("Great");
                        break;
                    case 5:
                        rateHintsTextView.setText("Amazing");
                        break;
                    default:
                        rateHintsTextView.setText("");
                }
            }

        });

        rateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                //get entered comment
                comment = commentView.getText().toString();
                //get selected rate
                entrdRate = (int)ratingBar.getRating();

                //create rate object
                rate = buildRate();
                //set user to be rated based on the current user type
                processRate();

            }
        });

    }
    public void processRate(){
        //current user id
        String currnetUserId ;
        //auth table reference
        FirebaseAuth mAuth = FirebaseAuth.getInstance();;
        //user reference
        FirebaseUser FBuser;
        //dRef
        DatabaseReference userProfileRef_type;
        //get user
        FBuser = mAuth.getCurrentUser();
        //get id
        currnetUserId = FBuser.getUid();
        rate.setRaterId(currnetUserId);
        userProfileRef_type = mDatabase.getReference("user_profiles").child(currnetUserId).child("type");
        userProfileRef_type.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String type = dataSnapshot.getValue(String.class);
                String serviceIdDb;
                if(type.equals("provider")){
                    userId = service.getRequester_id();
                    serviceIdDb = service.getProvider_id()+"_"+service.getService_id();
                }
                else{
                    userId = service.getProvider_id();
                    serviceIdDb = service.getRequester_id()+"_"+service.getService_id();
                }
                //rate contains requester_id,provider_id,service_id,profession,job,rate,comment
                //get reference
                userRateRef = mDatabase.getReference("rates").child(userId);
                //push rate to db
                userRateRef.child(serviceIdDb).setValue(rate);
                //update userProfile rate
                updateRate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
    public void updateRate(){
        userRateRef = mDatabase.getReference("user_profiles").child(userId).child("rate");
        // user rate ref
        userRateRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                final Double currRate = dataSnapshot.getValue(Double.class);
                final Double weightedRate = calcRate((double)entrdRate,currRate);
                //set new rate
                userRates = mDatabase.getReference("rates").child(userId);
                userRates.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                        int rateNum = (int)dataSnapshot.getChildrenCount();
                        Double calcRate = weightedRate + currRate * rateNum;
                        calcRate = calcRate /(rateNum + 1);
                        Log.w("lookAtMe","calcRate "+calcRate+"rateNum "+rateNum+"weightedRate "+weightedRate+"currRate "+currRate);
                        userRateRef.setValue(calcRate);
                        transit();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public Double calcRate(Double entrdRate, Double currRate){
        if(entrdRate > (currRate + 1.5) || entrdRate < (currRate - 1.5) )
            return(entrdRate/2);
        else
            return(entrdRate);
    }
    public Rate buildRate(){
        Rate r = new Rate();
        r.setRequesterId(service.getRequester_id());
        r.setProviderId(service.getProvider_id());
        r.setServiceId(service.getService_id());
        r.setRate(entrdRate);
        r.setComment(comment);
        r.setProfession(service.getProfession());
        r.setJob(service.getJob());
        return r;
    }
    public void transit(){
        Toast.makeText(RateActivity.this, "Thank you for your time", Toast.LENGTH_SHORT).show();
        //startActivity(new Intent(RateActivity.this, ListProvidersActivity.class));
        this.finish();
    }
}
