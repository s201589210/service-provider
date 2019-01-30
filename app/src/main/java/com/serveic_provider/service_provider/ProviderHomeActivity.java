package com.serveic_provider.service_provider;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.serveic_provider.service_provider.R;
import com.serveic_provider.service_provider.adapters.ProviderServiceAdapter;
import com.serveic_provider.service_provider.adapters.ServiceAdapter;
import com.serveic_provider.service_provider.adapters.WordAdapter;
import com.serveic_provider.service_provider.serviceProvider.Service;
import com.serveic_provider.service_provider.word;

import java.util.ArrayList;

public class ProviderHomeActivity extends AppCompatActivity {

    DatabaseReference typeRef;
    FirebaseDatabase mDatabase;
    ArrayList<Service> pendingServices = new ArrayList<Service>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_homepage);

        setTitle("Service Provider");

        //auth table reference
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        //user reference
        FirebaseUser FBuser;
        //get user
        FBuser = mAuth.getCurrentUser();
        final String userId = FBuser.getUid();
        //requster_location reference
        mDatabase = FirebaseDatabase.getInstance();
        typeRef = mDatabase.getReference().child("provider_services").child(userId);
        //location listner
        typeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //serviceId = requester ID + service number
                    String serviceId = snapshot.getValue(String.class);
                    String requesterId = serviceId.substring(0,serviceId.indexOf("_"));
                    String serviceNumber = serviceId.substring(serviceId.indexOf("_")+1);
                    //getting the service using the service id
                    addServiceToPendingList(requesterId, serviceNumber);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });//end of getting location

        //display services loaded in pendingServices
        displayPendingServices();
    }

    private void addServiceToPendingList(String requesterId, String serviceNumber) {

        DatabaseReference requesterRef = mDatabase.getReference().child("requester_services").child(requesterId).child(serviceNumber);
        //location listner
        requesterRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //service object
                Service service = dataSnapshot.getValue(Service.class);
                //adding the sevice to the ArrayList
                if(service.getProvider_id() != "" || service.getProvider_id() != null)
                    pendingServices.add(service);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });//end of getting location

    }


    // Not allowing back button
    @Override
    public void onBackPressed() {

    }

    public void goToMyServicesPage(View view) {
        startActivity(new Intent(this,MyServicesActivity.class));
    }


    private void displayPendingServices() {
        ListView pendingServicesListView = findViewById(R.id.provider_pending_services_listview);
        ProviderServiceAdapter adapter = new ProviderServiceAdapter(this, pendingServices);
        pendingServicesListView.setAdapter(adapter);
        pendingServicesListView.setClickable(true);
    }
}