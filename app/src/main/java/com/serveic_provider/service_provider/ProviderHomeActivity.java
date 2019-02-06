package com.serveic_provider.service_provider;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.serveic_provider.service_provider.adapters.ProviderServiceAdapter;
import com.serveic_provider.service_provider.serviceProvider.Service;

import java.util.ArrayList;

public class ProviderHomeActivity extends AppCompatActivity {

    DatabaseReference providerServicesRef;
    FirebaseDatabase mDatabase;
    ArrayList<Service> pendingServices = new ArrayList<Service>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_homepage);

        setTitle("Service Provider");

        buildList();

    }

    private void buildList() {
        //auth table reference
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        //user reference
        FirebaseUser FBuser;
        //get user
        FBuser = mAuth.getCurrentUser();
        final String userId = FBuser.getUid();
        //provider_services reference
        mDatabase = FirebaseDatabase.getInstance();
        providerServicesRef = mDatabase.getReference().child("provider_services").child(userId);

        //provider_services listener
        providerServicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
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

    }

    private void addServiceToPendingList(String requesterId, String serviceNumber) {

        DatabaseReference requesterServiceRef = mDatabase.getReference().child("requester_services").child(requesterId).child(serviceNumber);
        //requester's service listener
        requesterServiceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //service object
                Service service = dataSnapshot.getValue(Service.class);
                //adding the service to the ArrayList
                if(service.getProvider_id().equals("none")){
                    pendingServices.add(service);
                    //display service
                    setList();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });//end of adding service to pending list

    }

    private void setList() {
        ListView pendingServicesListView = findViewById(R.id.provider_pending_services_listview);
        ProviderServiceAdapter adapter = new ProviderServiceAdapter(this, pendingServices);
        pendingServicesListView.setAdapter(adapter);
        pendingServicesListView.setClickable(true);
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