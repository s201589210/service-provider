package com.serveic_provider.service_provider.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

public class FinishedFragment extends Fragment {
    View view;
    DatabaseReference typeRef;
    FirebaseDatabase mDatabase;
    ArrayList<Service> finishedServices = new ArrayList<Service>();
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    public FinishedFragment() {
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.services_fragment, container, false);
        finishedServices = new ArrayList<Service>();
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
        //location listner
//*//
        DatabaseReference ServicesRef = rootRef.child("requester_services").child(userId);
        ServicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //loop over all providers ids
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Service servic = snapshot.getValue(Service.class);
                    String job = servic.getJob();
                    String status = servic.getStatus();
                    // Log.v("potato", "test");
                    if (status.equals("finished")) {
                        finishedServices.add(servic);
                    }
                    ///////////////////
                    ///////////////////
                    ListView listView = (ListView) view.findViewById(R.id.pending_services_listview);
                    // adapter knows how to create list items for each item in the list.
                    ServiceAdapter adapter = new ServiceAdapter(FinishedFragment.this.getActivity(), finishedServices, R.color.colorWhite);
                    // {@link ListView} will display list items for each {@link Word} in the list.
                    listView.setAdapter(adapter);
                    listView.setClickable(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return view;
    }//on create method
}