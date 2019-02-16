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

public class DeletedFragment extends Fragment {
    View view;
    DatabaseReference typeRef;
    FirebaseDatabase mDatabase;
    ArrayList<Service> penddingServices = new ArrayList<Service>();
    ArrayList<String> requestersIDs = new ArrayList<String>();
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    String userType;

    public DeletedFragment() {

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
                                if (status.equals("deleted")) {
                                    penddingServices.add(service);
                                }

                                ListView listView = (ListView) view.findViewById(R.id.pending_services_listview);
                                // adapter knows how to create list items for each item in the list.
                                ServiceAdapter adapter = new ServiceAdapter(DeletedFragment.this.getActivity(), penddingServices, R.color.colorWhite);
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

                                String[] parts = service.split("_");
                                String requesterID = parts[0];
                                String serviceNumber = parts[1];
                                ////////////////////////////
                                DatabaseReference ServicesRef = rootRef.child("requester_services").child(requesterID).child(serviceNumber);
                                ServicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        Service service = dataSnapshot.getValue(Service.class);
                                        String job = service.getJob();
                                        String status = service.getStatus();

                                        if (status.equals("deleted") && service.getProvider_id().equals(userId)) {
                                            penddingServices.add(service);
                                        }

                                        ListView listView = (ListView) view.findViewById(R.id.pending_services_listview);
                                        // adapter knows how to create list items for each item in the list.
                                        ServiceAdapter adapter = new ServiceAdapter(DeletedFragment.this.getActivity(), penddingServices, R.color.colorWhite);
                                        // {@link ListView} will display list items for each {@link Word} in the list.
                                        listView.setAdapter(adapter);
                                        listView.setClickable(true);


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
