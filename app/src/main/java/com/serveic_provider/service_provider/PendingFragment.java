package com.serveic_provider.service_provider;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.serveic_provider.service_provider.adapters.ServiceAdapter;
import com.serveic_provider.service_provider.serviceProvider.Service;
import com.serveic_provider.service_provider.serviceProvider.User;

import java.util.ArrayList;

public class PendingFragment extends Fragment {
    View view;
    DatabaseReference typeRef;
    FirebaseDatabase mDatabase;
    ArrayList<Service> penddingServices = new ArrayList<Service>();

    public PendingFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.pending_fragment, container, false);


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
        typeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String type = dataSnapshot.getValue(String.class);

                if (type.equals("Requester")) {

                    displayRequesterServices(userId);
                    Log.v("MyTag", userId);
                } else if (type.equals("Provider")) {
                    //displayProviderServices(userId);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });//end of getting location
        return view;
    }//on create method

    private void displayRequesterServices(String userId) {
        DatabaseReference requesterServicesRef = mDatabase.getReference().child("requester_services").child(userId);
        requesterServicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Service service = snapshot.getValue(Service.class);
                    buildItem(service);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }//end of displaying services for requester

    public void buildItem(final Service service) {
        ListView listView = (ListView) getActivity().findViewById(R.id.pending_services_listview);
        penddingServices.add(service);
        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        ServiceAdapter adapter = new ServiceAdapter(this.getActivity(), penddingServices, R.color.colorWhite);
        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        Log.v("itemBuild", "buildItem: ");
        listView.setAdapter(adapter);
        listView.setClickable(true);
    }// end of building item
}
