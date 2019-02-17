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
import java.util.Calendar;
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
        updateServicesStatus();
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

    private void updateServicesStatus() {


        //user reference
        FirebaseUser FBuser;
        //get user
        FBuser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = FBuser.getUid();

        final DatabaseReference requesterServicesRef = FirebaseDatabase.getInstance().getReference().child("requester_services").child(userId);

        //provider_services listener
        requesterServicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Service service = snapshot.getValue(Service.class);

                    // service is pending and has a provider
                    if(service.getStatus().equals("pending") && !service.getProvider_id().equals("none")) {
                        if(isTimePassed(service))
                            requesterServicesRef.child(service.getService_id()).child("status").setValue("in progress");
                        // service is pending and has no provider
                    }else if(service.getStatus().equals("pending") && service.getProvider_id().equals("none")){
                        if(isTimePassed(service))
                            requesterServicesRef.child(service.getService_id()).child("status").setValue("deleted");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });//end of updating services
    }

    //returns true if time has passed else return false
    private boolean isTimePassed(Service service) {
        // get current date info
        final int currentDay = Calendar.getInstance().getTime().getDate();
        final int currentMonth = Calendar.getInstance().getTime().getMonth()+1;
        final int currentYear = Calendar.getInstance().getTime().getYear()+1900;
        final int currentHour = Calendar.getInstance().getTime().getHours();
        final int currentMinuet = Calendar.getInstance().getTime().getMinutes();

        //get service date info
        String[] serviceDateString = service.getDate().split("/");
        int serviceDay = Integer.parseInt(serviceDateString[1]);
        int serviceMonth = Integer.parseInt(serviceDateString[0]);
        int serviceYear = Integer.parseInt("20" + serviceDateString[2]);
        String[] serviceStartTime = service.getStartTime().split(":");

        if (serviceYear < currentYear)
            return true;
        else if (serviceYear == currentYear)
            if (serviceMonth < currentMonth)
                return true;
            else if (serviceMonth == currentMonth)
                if (serviceDay < currentDay)
                    return true;
                else if (serviceDay == currentDay)
                    if (Integer.parseInt(serviceStartTime[0]) < currentHour)
                        return true;
                    else if (Integer.parseInt(serviceStartTime[0]) == currentHour)
                        if (Integer.parseInt(serviceStartTime[1]) <= currentMinuet)
                            return true;

        return false;
    }
}
