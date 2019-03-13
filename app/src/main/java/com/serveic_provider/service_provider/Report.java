package com.serveic_provider.service_provider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.serveic_provider.service_provider.serviceProvider.Service;

import org.w3c.dom.Text;

public class Report extends AppCompatActivity {
    CheckBox penalty1 ,penalty2,penalty3,penalty4,penalty5;
    Button reportButton;
    FirebaseDatabase mDatabase;
    Service service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_page);
        setTitle("Report");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //getting the service from extra
            service = (Service)extras.getSerializable("service");
        }//end of checking extras!=null


        reportButton = (Button) findViewById(R.id.report_button);




        reportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                mDatabase = FirebaseDatabase.getInstance();
                DatabaseReference penaltypoints = mDatabase.getReference().child("user_profiles").child("HFPdxSNW9bcE2D8oupLIU1KcKiq2").child("penalty_points");

                penaltypoints.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int oldPoints  = dataSnapshot.getValue(int.class);
                        calculatePoint(oldPoints);
                    }

                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });


            }

        });




    }
    public void calculatePoint(int oldPoints){
        int penaltyPoint=oldPoints;

        penalty1 =(CheckBox) findViewById(R.id.report1_checkbox);
        penalty2 =(CheckBox) findViewById(R.id.report2_checkbox);
        penalty3 =(CheckBox) findViewById(R.id.report3_checkbox);
        penalty4 =(CheckBox) findViewById(R.id.report4_checkbox);
        penalty5 =(CheckBox) findViewById(R.id.report5_checkbox);

        if (penalty1.isChecked()){
            penaltyPoint=penaltyPoint+1;}

        if (penalty2.isChecked()){
            penaltyPoint=penaltyPoint+2;}

        if (penalty3.isChecked()){
            penaltyPoint=penaltyPoint+3;}

        if (penalty4.isChecked()){
            penaltyPoint=penaltyPoint+4;}

        if (penalty5.isChecked()){
            penaltyPoint=penaltyPoint+5;}

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("user_profiles").child("HFPdxSNW9bcE2D8oupLIU1KcKiq2");
        mDatabase.child("penalty_points").setValue(penaltyPoint);


    }


}