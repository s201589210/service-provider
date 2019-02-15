package com.serveic_provider.service_provider.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.serveic_provider.service_provider.R;
import com.serveic_provider.service_provider.adapters.ServiceAdapter;
import com.serveic_provider.service_provider.serviceProvider.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PendingFragment extends Fragment {
    View view;
    DatabaseReference typeRef;
    FirebaseDatabase mDatabase;
    ArrayList<Service> penddingServices = new ArrayList<Service>();
    ArrayList<String> requestersIDs = new ArrayList<String>();
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    String userType;

    public PendingFragment() {

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.services_fragment, container, false);
        penddingServices = new ArrayList<Service>();
        //auth table reference
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        //user reference
        FirebaseUser FBuser;
        //get user
        FBuser = mAuth.getCurrentUser();
        final String userId = FBuser.getUid();
        //requster_location reference
        mDatabase = FirebaseDatabase.getInstance();
        typeRef = mDatabase.getReference().child("user_profiles").child(userId).child("type");


        //find the type of the user
        typeRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userType = dataSnapshot.getValue(String.class);





                //if statment for requester type
                if (userType.equals("requester")){
                    DatabaseReference ServicesRef = rootRef.child("requester_services").child(userId);
                    ServicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //loop over all providers ids
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Service service = snapshot.getValue(Service.class);
                                String job = service.getJob();
                                String status = service.getStatus();
                                // Log.v("potato", "test");
                                if (status.equals("pending")) {
                                    penddingServices.add(service);
                                }

                                ListView listView = (ListView) view.findViewById(R.id.pending_services_listview);
                                // adapter knows how to create list items for each item in the list.
                                ServiceAdapter adapter = new ServiceAdapter(PendingFragment.this.getActivity(), penddingServices, R.color.colorWhite);
                                // {@link ListView} will display list items for each {@link Word} in the list.
                                listView.setAdapter(adapter);
                                listView.setClickable(true);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    }); }

                    //if statment for provider type
                else  if (userType.equals("provider")){
                    //refrence to get all requesters id for the provider
                    DatabaseReference ServicesRef = rootRef.child("provider_services").child(userId);

                    ServicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String service = snapshot.getValue(String.class);
                     //store requesters id in arraylist
                   // for some reason , there is extra 2 characters at the end of each id ,i use substring to remove them
                                requestersIDs.add(service.substring(0,28));
                            }
                    // to remove dublicate id send it to set then return it back
                            Set<String> set = new HashSet<>(requestersIDs);
                            requestersIDs.clear();
                            requestersIDs.addAll(set);

                        //loop for the id list to add to find all services in requester side
                            for(int i=0;i<requestersIDs.size();i++){

                           // Toast.makeText(getActivity(), servicesIDs.get(0), Toast.LENGTH_LONG).show();
                  DatabaseReference ServicesRef = rootRef.child("requester_services").child(requestersIDs.get(i));
                        ServicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        //loop over all providers ids
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Service service = snapshot.getValue(Service.class);
                                            String job = service.getJob();
                                            String status = service.getStatus();
                                 // if statment for service if its in pending status and made by the spcific requester
                                            if (status.equals("pending") && service.getProvider_id().equals(userId)) {
                                                penddingServices.add(service);
                                            }

                                            ListView listView = (ListView) view.findViewById(R.id.pending_services_listview);
                                            // adapter knows how to create list items for each item in the list.
                                            ServiceAdapter adapter = new ServiceAdapter(PendingFragment.this.getActivity(), penddingServices, R.color.colorWhite);
                                            // {@link ListView} will display list items for each {@link Word} in the list.
                                            listView.setAdapter(adapter);
                                            listView.setClickable(true);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });




                           }

                       }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    }); }






            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


//*//




        return view;
    }//on create method
}
