package com.serveic_provider.service_provider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.serveic_provider.service_provider.fragments.DatePickerFragment;
import com.serveic_provider.service_provider.fragments.TimePickerFragment;
import com.serveic_provider.service_provider.serviceProvider.Job;
import com.serveic_provider.service_provider.serviceProvider.Service;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class CreateServiceActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {


    Intent listProviderIntent;

    //auth table reference
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    //user reference
    FirebaseUser FBuser = mAuth.getCurrentUser();
    final String userId = FBuser.getUid();

    String requsterID = userId;

    String serviceID;

    Service service = new Service();
    Job job;
    String jobTitle;

    String profession;

    String description;
    String date;
    String startTime;
    String endTime;
    String neighbor;
    String city;
    int building;

    String whichPicker;

    String PROMPT_FOR_JOB = "Select Job";

    ArrayList<String> JOBS = new ArrayList<String>();

    DatabaseReference requesterServicesRef;
    DatabaseReference providerServicesRef;
    DatabaseReference userProfileRef_serviceCounter;
    DatabaseReference professionJobsRef;


    @BindView(R.id.jobs_spinner)
    Spinner jobSpinner;
    @BindView(R.id.description_edit_text)
    EditText descriptionEditText;
    @BindView(R.id.date_edit_text)
    EditText dateEditText;
    @BindView(R.id.from_edit_text)
    EditText fromEditText;
    @BindView(R.id.to_edit_text)
    EditText toEditText;
    @BindView(R.id.city_spinner)
    Spinner citySpinner;
    @BindView(R.id.neighbor_spinner)
    Spinner neighborSpinner;
    @BindView(R.id.building_edit_text)
    EditText buildingEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_service);
        setTitle("Create Service");
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //getting the selected profession
            profession = extras.getString("profession");
            Log.v("extras1", profession);
        }//end of checking extras!=null
        //appends all jobs of the selected profession
        readJobsFromFBDB();
    }//end of create

    @OnClick(R.id.create_button)
    public void createService(View view) {
        if(validateCreateForm()) {
            // attributes from UI are in validate method

            // Service is  inserted into requster_service inside this method
            buildService();
            Intent intent = new Intent(this, ListProvidersActivity.class);
            final Bundle bundle = new Bundle();
            bundle.putSerializable("service",service);
            intent.putExtras(bundle);
            this.startActivity(intent);
            /*Toast.makeText(CreateServiceActivity.this, "Service has been created",
                    Toast.LENGTH_SHORT).show();*/
        }

    }
    @OnClick(R.id.date_edit_text)
    public void selectDate(View view) {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
    }
    @OnClick(R.id.from_edit_text)
    public void selectFromTime(View view) {
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "from time picker");
        whichPicker = "from time picker";
    }
    @OnClick(R.id.to_edit_text)
    public void selectToTime(View view) {
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "to time picker");
        whichPicker = "to time picker";
    }
    @OnItemSelected(R.id.city_spinner)
    public void selectCity(View view) {
        //temp array to hold all neighbors to the selected city
        String[] tempArray = new String[1];
        String city = citySpinner.getSelectedItem().toString();
        Log.e("dfs",city);
        if(!city.equals("")){
            if(city.equals("Dammam")){
                tempArray = getResources().getStringArray(R.array.dammam_neighbors);}
            else if(city.equals("Dhahran")){
                tempArray = getResources().getStringArray(R.array.dhahran_neighbors);}
            else if(city.equals("Riyadh")) {
                tempArray = getResources().getStringArray(R.array.riyadh_neighbors);
            }
            else{
                tempArray[0] = "not specified yet";
            }

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, tempArray );
            neighborSpinner.setAdapter(spinnerArrayAdapter);
        }
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDateString = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());
        dateEditText.setText(currentDateString);
    }
    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        if(whichPicker.equals("from time picker"))
            fromEditText.setText(hour + ":" + minute);
        if(whichPicker.equals("to time picker"))
            toEditText.setText(hour + ":" + minute);
    }



    public boolean validateCreateForm() {

        String jobSelected = jobSpinner.getSelectedItem().toString().toLowerCase();
        String descriptionText = descriptionEditText.getText().toString().trim().toLowerCase();
        String dateText = dateEditText.getText().toString().toLowerCase();
        String fromTimeText = fromEditText.getText().toString().toLowerCase();
        String toTimeText = toEditText.getText().toString().toLowerCase();
        String cityText = citySpinner.getSelectedItem().toString().toLowerCase();
        String neighborText = neighborSpinner.getSelectedItem().toString().toLowerCase();
        int buildingText = Integer.parseInt(buildingEditText.getText().toString());

        if (jobSelected.equals(PROMPT_FOR_JOB)) {
            Toast.makeText(CreateServiceActivity.this, "Please select a job",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (dateText.isEmpty()) {
            Toast.makeText(CreateServiceActivity.this, "Please enter a date",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (fromTimeText.isEmpty()) {
            Toast.makeText(CreateServiceActivity.this, "Please enter a start from time",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (toTimeText.isEmpty()) {
            Toast.makeText(CreateServiceActivity.this, "Please enter a to time",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (toTimeText.equals(fromTimeText)) {
            Toast.makeText(CreateServiceActivity.this, "The from and to time cannot be" +
                    "the same", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (cityText.isEmpty()) {
            Toast.makeText(CreateServiceActivity.this, "please enter a city", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (neighborText.isEmpty()) {
            Toast.makeText(CreateServiceActivity.this, "please enter a neighbor", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (buildingText==0) {
            Toast.makeText(CreateServiceActivity.this, "please enter a building number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (descriptionText.isEmpty()){
            descriptionEditText.setText("No description");
        }

        jobTitle = jobSelected.split(",")[0];
        description = descriptionText;
        date = dateText;
        startTime = fromTimeText;
        endTime = toTimeText;
        city = cityText;
        building = buildingText;
        neighbor = neighborText;

        return true;
    }

    public Service buildService(){
        //setting all values here
        Log.w("hello",profession);
        service.setJob(jobTitle);
        service.setProfession(profession);
        service.setDescription(description);
        service.setDate(date);
        service.setStartTime(startTime);
        service.setEndTime(endTime);
        service.setStatus("pending");
        service.setProvider_id("none");
        service.setRequester_id(requsterID);
        service.setCity(city);
        service.setBuilding(building);
        service.setNeighbor(neighbor);
        return service;
    }

    public void readJobsFromFBDB() {
        //add string to the jobs list
        JOBS.add(PROMPT_FOR_JOB);

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
                    Log.v("inJob", JOBS.get(i));
                    i++;
                }
            }
            @Override
            public void onCancelled (DatabaseError databaseError){
                // Getting Post failed, log a message
                Log.w("loadJobsError", "loadJobs:onCancelled", databaseError.toException());

                // ...
            }
        };
        // Adding the listener to the node so that it executes the above methods
        professionJobsRef.addListenerForSingleValueEvent(JobsListener);

        // Putting the job array in the string.xml into the spinner
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, JOBS);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobSpinner.setAdapter(adapter2);
    }



}