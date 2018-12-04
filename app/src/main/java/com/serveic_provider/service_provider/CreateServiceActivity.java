package com.serveic_provider.service_provider;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.serveic_provider.service_provider.serviceProvider.Job;
import com.serveic_provider.service_provider.serviceProvider.Service;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateServiceActivity extends AppCompatActivity {
    private static final String TAG = "CreateServiceActivity";

    String requsterID;
    String profession;
    String providerID;
    String PROMPT_FOR_JOB = "Select Job";
    Job job;
    String jobTitle;
    String description;
    String date;

    ArrayList<String> JOBS = new ArrayList<String>();

    DatabaseReference requesterServicesRef;
    DatabaseReference userProfileRef_serviceCounter;


    DatabaseReference professionJobsRef;


    @BindView(R.id.jobs_spinner)
    Spinner jobSpinner;
    @BindView(R.id.description_edit_text)
    EditText descriptionEditText;
    @BindView(R.id.date_edit_text)
    EditText dateEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_service);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String providerID_profession = extras.getString("providerID_profession");
            String[] providerID_profession_array = providerID_profession.split("_");
            providerID = providerID_profession_array[0];
            profession = providerID_profession_array[1];


            //The key argument here must match that used in the other activity


            //tv.setText(providerID);


        }//end of checking extras!=null

        JOBS.add(PROMPT_FOR_JOB);
        readJobsFromFBDB();

        // Putting the job array in the string.xml into the spinner
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, JOBS);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobSpinner.setAdapter(adapter2);


    }//end of create

    @OnClick(R.id.create_button)
    public void createService(View view) {
        if(validateCreateForm()) {
               // attributes from UI are in validate method

                // Service is not inserted into provider_service
                insertService(providerID);

                Toast.makeText(CreateServiceActivity.this, "Service has been created",
                        Toast.LENGTH_SHORT).show();
                updateUI();
        }

    }

    public boolean validateCreateForm() {
        String jobSelected = jobSpinner.getSelectedItem().toString();
        String dateText = dateEditText.getText().toString();
        String descriptionText = descriptionEditText.getText().toString().trim();

        if(jobSelected.equals(PROMPT_FOR_JOB)) {
            Toast.makeText(CreateServiceActivity.this, "Please select a job",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if(dateText.isEmpty()) {
            Toast.makeText(CreateServiceActivity.this, "Please enter a date",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if(dateText.isEmpty()) {
            Toast.makeText(CreateServiceActivity.this, "Please enter a date",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if(descriptionText.isEmpty())
            descriptionEditText.setText("No description");

        jobTitle = jobSelected.split(",")[0];
        date = dateText;
        description = descriptionText;

        return true;
    }

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
        //setting all values here
        service.setJob(jobTitle);
        service.setDescription(description);
        service.setDate(date);
        service.setStatus("pending");
        service.setProvider_id(providerID);

        return service;
    }

    public void readJobsFromFBDB() {
        // Getting the user_profiles node
        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        professionJobsRef = mDatabase.getReference("profession_jobs/" + profession);
        ValueEventListener JobsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {
                    //Loop 1 to go through all the child nodes of profession_jobs
                    job = jobSnapshot.getValue(Job.class);
                    String jobTitle_JobPrice = job.getTitle() + ", " + job.getPrice() + " SAR";
                    JOBS.add(jobTitle_JobPrice);
                    Log.v("Potato", JOBS.get(i));
                    Log.v("Potato", JOBS.get(i));
                    i++;
                }
            }
            @Override
            public void onCancelled (DatabaseError databaseError){
                // Getting Post failed, log a message
                Log.w(TAG, "loadJobs:onCancelled", databaseError.toException());

                // ...
            }
        };
        // Adding the listener to the node so that it executes the above methods
        professionJobsRef.addListenerForSingleValueEvent(JobsListener);
    }

    public void updateUI() {
        startActivity(new Intent(CreateServiceActivity.this, MyServicesActivity.class));
    }
}
