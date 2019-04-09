package com.serveic_provider.service_provider;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.serveic_provider.service_provider.serviceProvider.Service;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmationActivity extends AppCompatActivity {

    private static final String TAG = "ConfirmationActivty";
    JSONObject serviceJSON;
    String requesterId = "";
    String serviceId = "";

    // Initializing the UI elements to get them ready for binding
    @BindView(R.id.data_profession_text_view)
    TextView professionText;
    @BindView(R.id.data_job_text_view)
    TextView jobText;
    @BindView(R.id.data_description_text_view)
    TextView descripitonText;
    @BindView(R.id.data_location_text_view)
    TextView locationText;
    @BindView(R.id.data_date_text_view)
    TextView dateText;
    @BindView(R.id.data_starttime_text_view)
    TextView startTimeText;
    @BindView(R.id.data_endtime_text_view)
    TextView endTimeText;
    @BindView(R.id.confirm_button)
    Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        setTitle("Confirm");

        // Binding the UI elements
        ButterKnife.bind(this);

        // getting the information
        Bundle extras = getIntent().getExtras();
        if(extras != null) {

            // if the information coming from the pushed notification
            if(extras.getString("service") != null) {
                String service = extras.getString("service");
                try {

                    serviceJSON = new JSONObject(service);
                    Log.d("serviceJSON", serviceJSON.toString());

                    // setting the data into the UI
                    professionText.setText(serviceJSON.getString("profession"));
                    jobText.setText(serviceJSON.getString("job"));
                    descripitonText.setText(serviceJSON.getString("description"));
                    locationText.setText(serviceJSON.getString("city"));
                    dateText.setText(serviceJSON.getString("date"));
                    startTimeText.setText(serviceJSON.getString("startTime"));
                    endTimeText.setText(serviceJSON.getString("endTime"));

                    // getting the values to update the status of the service
                    requesterId = serviceJSON.getString("requester_id");
                    serviceId = serviceJSON.getString("service_id");
                } catch (JSONException e) {
                    Log.e("serviceJson", "Could not parse malformed JSON");
                }
            }

            // if the information coming from the notification adapter
            if(extras.getString("serviceID") != null) {

                String requesterId_serviceId = extras.getString("serviceID");
                Log.d("serviceID", serviceId);

                // getting the values to update the status of the service
                requesterId = requesterId_serviceId.split("_")[0];
                serviceId = requesterId_serviceId.split("_")[1];

                // read and update the UI texts
                readServiceFromFDBD(requesterId, serviceId);


            }
        }
    }// end of create method

    @OnClick(R.id.confirm_button)
    public void confirmService(View view) {
        writeServiceFinishedToFDBD(requesterId, serviceId);
    }
    private void writeServiceFinishedToFDBD(String user_id, String serviceId) {

        DatabaseReference serviceRef = FirebaseDatabase.getInstance().getReference().child("requester_services")
                .child(user_id).child(serviceId);

        serviceRef.child("status").setValue("finished").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ConfirmationActivity.this, "The service is confirmed", Toast.LENGTH_SHORT).show();
                updateUI();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ConfirmationActivity.this, "Error in Confirming the service", Toast.LENGTH_SHORT).show();
            }
          });
    }
    private void readServiceFromFDBD(String user_id, String serviceId) {
        DatabaseReference serviceRef = FirebaseDatabase.getInstance().getReference().child("requester_services")
                .child(user_id).child(serviceId);

        ValueEventListener serviceListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get service object and use the values to update the UI
                Service service = dataSnapshot.getValue(Service.class);

                // setting the data into the UI
                professionText.setText(service.getProfession());
                jobText.setText(service.getJob());
                descripitonText.setText(service.getDescription());
                locationText.setText(service.getCity());
                dateText.setText(service.getDate());
                startTimeText.setText(service.getStartTime());
                endTimeText.setText(service.getEndTime());
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadService:onCancelled", databaseError.toException());
                // ...
            }
        };
        serviceRef.addValueEventListener(serviceListener);

    }


    // Changing the UI
    private void updateUI() {
            startActivity(new Intent(this, MyServicesActivity.class));
    }
}
