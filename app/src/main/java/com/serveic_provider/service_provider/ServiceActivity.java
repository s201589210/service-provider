package com.serveic_provider.service_provider;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.serveic_provider.service_provider.serviceProvider.Service;


public class ServiceActivity extends AppCompatActivity {

    TextView professionTextView;
    TextView jobTextView;
    TextView descriptionTextView;
    TextView dateTextView;
    TextView startTimeTextView;
    TextView endTimeTextView;
    Button locationButton;


    double locationLat;
    double locationLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        professionTextView = findViewById(R.id.service_activity_data_profession_text_view);
        jobTextView = findViewById(R.id.service_activity_data_job_text_view);
        descriptionTextView = findViewById(R.id.service_activity_data_description_text_view);
        dateTextView = findViewById(R.id.service_activity_data_date_text_view);
        startTimeTextView = findViewById(R.id.service_activity_data_starttime_text_view);
        endTimeTextView = findViewById(R.id.service_activity_data_endtime_text_view);

        locationButton = findViewById(R.id.service_activity_data_location_button);

        // getting the information from MyServices Activity
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            Service service = (Service) extras.getSerializable("service");

            professionTextView.setText(service.getProfession());
            jobTextView.setText(service.getJob());
            descriptionTextView.setText(service.getDescription());
            dateTextView.setText(service.getDate());
            startTimeTextView.setText(service.getStartTime());
            endTimeTextView.setText(service.getEndTime());

            if(service.getLocation()!=null){
                locationLat = Double.parseDouble(service.getLocation().split(",")[0]);
                locationLng = Double.parseDouble(service.getLocation().split(",")[1]);
            }

            setLocationListener();
        }

    }

    public void setLocationListener(){
        String url = "http://maps.google.com/maps?daddr=" + locationLat + "," + locationLng;
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        locationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(intent);
            }
        });

    }
}
