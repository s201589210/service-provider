package com.serveic_provider.service_provider;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.serveic_provider.service_provider.adapters.ProvAdaptor;
import com.serveic_provider.service_provider.serviceProvider.Service;
import com.serveic_provider.service_provider.serviceProvider.User;

import java.util.ArrayList;

import butterknife.BindView;

public class ListProvidersActivity extends AppCompatActivity {


    private static final String TAG = "ListProvidersActivity";
    ArrayList<User> providerList = new ArrayList<User>();
    ArrayList<String> providerIdList = new ArrayList<String>();
    ArrayList<String> selectedProviders = new ArrayList<String>();
    ProvAdaptor adapter;

    String name;
    String company;
    String location;
    String profession;
    String city;
    Service service;
    String requsterID;
    String providerID;
    String serviceID;
    pl.droidsonroids.gif.GifImageView spinner;

    DatabaseReference requesterServicesRef;
    DatabaseReference providerServicesRef;
    DatabaseReference userProfileRef_serviceCounter;
    DatabaseReference locationRef;
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

    @BindView(R.id.create_button)
    Button createButton;

    //----------------------------------------------------------///
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_providers);
        setTitle("Select Providers");

        spinner = (pl.droidsonroids.gif.GifImageView)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);

        providerList = new ArrayList<User>();
        Log.v("onCreate","222");
        //get information based with last activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //getting the service of the intent(createService)
             service = (Service)extras.getSerializable("service");
             Log.v("extraPass","222");
            //build the providers list
            buildList(service.getCity());
        }//end of checking extras!=null

       final Button Btn = (Button) findViewById(R.id.create_button);
        Btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //insert the service to the requester node
                insertRequesterService();
                Toast.makeText(ListProvidersActivity.this, "created successfully ",
                        Toast.LENGTH_SHORT).show();

                startActivity(new Intent(ListProvidersActivity.this, RequesterHomeActivity.class));

                //set the provider id;
                //insertRequesterService();
            }
        });


        ListView listView = (ListView)findViewById(R.id.lists);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("itemClick", i+"");
                User model = providerList.get(i);
                if (model.isSelected())
                    model.setSelected(false);
                else
                    model.setSelected(true);

                providerList.set(i, model);
                providerIdList.add(model.getUid());


                //now update adapter
                adapter.updateRecords(providerList);

            }
        });

    }//on create method
    //insert service to requester
    public void insertRequesterService(){
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
                //setting the service fields
                service.setRequester_id(requsterID);
                service.setPotentialProvidersIds(providerIdList);
                service.setService_id(serviceCounter);
                //insert service to user id in the requester_services node
                requesterServicesRef.child(requsterID).child(serviceCounter).setValue(service)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            Integer intServiceCounter = Integer.parseInt(serviceCounter) + 1;
                            @Override
                            public void onSuccess(Void aVoid) {
                                userProfileRef_serviceCounter.setValue(intServiceCounter+"");

                                String tempCounter = intServiceCounter-1+"";
                                //loop for all selected providers
                                //if the user is selected insert service to his node
                                for(User u : providerList){
                                    if(u.isSelected())
                                    insertProviderService(u.getUid(), tempCounter);
                                }


                                Log.d("tag", "writeUserType:success");
                            }
                        });//end insertion refrence
            }//end of counter on data change listner
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });//end of counter listner
    }//end of inserting service
    //inserts the service to the selected providers
    public void insertProviderService(final String providerID, final String requester_serviceCounter){
        //auth table reference
        FirebaseAuth mAuth = FirebaseAuth.getInstance();;
        //user reference
        FirebaseUser FBuser;
        //requster_service reference
        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        providerServicesRef = mDatabase.getReference("provider_services");
        //get user
        FBuser = mAuth.getCurrentUser();
        //get id
        requsterID = FBuser.getUid();
        // set the serviceID
        serviceID = requsterID + "_" + requester_serviceCounter;
        //user profile reference
        userProfileRef_serviceCounter = mDatabase.getReference("user_profiles").child(providerID).child("serviceCounter");
        //service counter listener
        userProfileRef_serviceCounter.addListenerForSingleValueEvent(new ValueEventListener() {
            String serviceCounter;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                serviceCounter = dataSnapshot.getValue(String.class);
                //Service service = buildProviderService(providerID);

                //insert service to user id in the requster_services node
                providerServicesRef.child(providerID).child(serviceCounter).setValue(serviceID)
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



    //appends provider to view
    public void buildList(String location) {
        //refernce to the profession providers
        DatabaseReference ProfissionRef = rootRef.child("profession_location_provider");
        DatabaseReference professionLocationRef = ProfissionRef.child(service.getProfession()).child(location);
        //getting providers of  location_Profession
        professionLocationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //loop over all providers ids
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String providerID = snapshot.getValue(String.class);
                    Log.v("potato", providerID);
                    //add providers to providerList array
                    addProvInfoToList(providerID);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }// end of building list
    //get the provider profile information and insert them to the list
    public void addProvInfoToList( final String providerID){

        //refernce to the user profile
        DatabaseReference profilesRef = rootRef.child("user_profiles");
        DatabaseReference providerProfileRef = profilesRef.child(providerID);
        providerProfileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //construct new user === firebase user
                User user = dataSnapshot.getValue(User.class);
                user.setUid(providerID);
                providerList.add(user);
                spinner.setVisibility(View.GONE);
                Log.v("addingInfo",providerList.get(0).getName());
                //display the list
                setList();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }//end of adding item to list
    public void setList(){
        ListView listView = (ListView) findViewById(R.id.lists);
        adapter = new ProvAdaptor(ListProvidersActivity.this, providerList);
        listView.setAdapter(adapter);
        listView.setClickable(true);
    }



}
