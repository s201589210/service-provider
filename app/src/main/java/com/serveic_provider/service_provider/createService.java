package com.serveic_provider.service_provider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.serveic_provider.service_provider.serviceProvider.Service;

import java.util.ArrayList;

public class createService extends AppCompatActivity {
    private static final String TAG ="createService";
    String jobTitle ;
    int price;
    String description;
    String date;
    String providerID;

    EditText descriptionEditText, dateEditText;
    ListView jobsListView ;
    Button createButton;

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_service);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            providerID = extras.getString("key");
            //The key argument here must match that used in the other activity
        }

        descriptionEditText = findViewById(R.id.descriptionEditText);
        dateEditText = findViewById(R.id.dateEditText);

        description= descriptionEditText.getText().toString();
        date = dateEditText.getText().toString();





        DatabaseReference professionRef =rootRef.child("user_profiles").child(providerID).child("ProviderProffission");
        DatabaseReference professionJobRef =rootRef.child("profession_jobs").child(String.valueOf(professionRef));

        final ArrayList<Job> jobs = new ArrayList<Job>();

        final ValueEventListener jobLisn = professionJobRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();
                ArrayList<DataSnapshot>  data= (ArrayList<DataSnapshot>) dataSnapshot.getChildren();
                for(int i=0 ;i<count; i++){
                    jobTitle=data.get(i).toString();
                    price= 0;
                    jobs.add(new Job(jobTitle,price));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        JobAdapter jobAdapter = new JobAdapter(createService.this,jobs,R.color.colorPrimary);
        jobsListView = findViewById(R.id.jobListView);
        jobsListView.setAdapter(jobAdapter);

        createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Service service = new Service(date,description,1,providerID,jobs);
                // TODO Auto-generated method stub
                Intent i = new Intent(createService.this,RequesterHomePage.class);
                startActivity(i);
            }
        });

    }








}
