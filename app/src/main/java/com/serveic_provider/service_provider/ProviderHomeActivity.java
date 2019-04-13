package com.serveic_provider.service_provider;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
    String userId;
    //auth table reference
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ArrayList<Service> pendingServices = new ArrayList<Service>();
    ImageView profile_button;
    android.support.v4.widget.SwipeRefreshLayout pullToRefresh ;
    boolean isRefreshing = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_homepage);
        setTitle("Service Provider");
        pullToRefresh=(android.support.v4.widget.SwipeRefreshLayout) findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //update code
                Log.v("MyTag",isRefreshing+"");
                if(!isRefreshing) {
                    clearList();
                    buildList();
                }
                pullToRefresh.setRefreshing(false);
            }
        });

        Utils.updateServiceStatus();
        profile_button = (ImageView)findViewById(R.id.profile_button);
        final Intent intent1 = new Intent(ProviderHomeActivity.this, ProfileActivity.class);
        profile_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //check type of the user
                //current user id
                String currnetUserId ;
                //auth table reference
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                //user reference
                FirebaseUser FBuser;
                //dRef
                DatabaseReference userProfileRef_type;
                //get user
                FBuser = mAuth.getCurrentUser();
                //get id
                currnetUserId = FBuser.getUid();
                final Bundle bundle1 = new Bundle();
                bundle1.putString("userId",currnetUserId);
                intent1.putExtras(bundle1);
                Log.w("ClickedHH",currnetUserId);
                startActivity(intent1);
            }
        });
        buildList();

    }



    private void buildList() {

        isRefreshing = true;
        new CountDownTimer(3000,3000){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                isRefreshing = false;
            }
        }.start();
        //user reference
        FirebaseUser FBuser;
        //get user
        FBuser = mAuth.getCurrentUser();
        userId = FBuser.getUid();
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
        pullToRefresh.setRefreshing(false);
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
                //if the service has no provider assigned yet
                if(service.getProvider_id().equals("none") && service.getStatus().equals("pending")){
                    //if the provider has not decline the service before (provider's id is in the potential providers ids)
                    if(service.getPotentialProvidersIds().contains(userId)) {
                        //then display the service for hte provider
                        pendingServices.add(service);
                        setList();
                    }
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

    private void clearList() {
        pendingServices.clear();
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.logout){
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class)); //Go back to home page
            finish();
        }
        return true;
    }

}