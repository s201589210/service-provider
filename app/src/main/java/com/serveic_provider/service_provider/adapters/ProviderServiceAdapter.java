package com.serveic_provider.service_provider.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.serveic_provider.service_provider.R;
import com.serveic_provider.service_provider.serviceProvider.Service;

import java.util.ArrayList;

public class ProviderServiceAdapter extends ArrayAdapter<Service> {
    Context context;
    DatabaseReference typeRef;
    FirebaseDatabase mDatabase;
    String requesterName = "";

    //auth table reference
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    //user reference
    FirebaseUser FBuser= mAuth.getCurrentUser();
    final String userId = FBuser.getUid();

    //requster_location reference

    TextView jobTextView;
    TextView descriptionTextView;
    TextView dateTextView;
    TextView timeTextView;
    TextView locationTextView;
    TextView profissionTextView;
    TextView requesterNameTextView;
    Button acceptButton;
    Button declineButton;
    Button undoButton;

    public ProviderServiceAdapter(Activity context, ArrayList<Service> serviceArrayList) {
        super(context,0, serviceArrayList);
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.listview_item_pending_service, parent, false);
        }

        //declare views
        undoButton = (Button) listItemView.findViewById(R.id.undo_button);
        jobTextView = (TextView) listItemView.findViewById(R.id.item_job);
        descriptionTextView = (TextView) listItemView.findViewById(R.id.item_description);
        dateTextView = (TextView) listItemView.findViewById(R.id.item_date);
        timeTextView = (TextView) listItemView.findViewById(R.id.item_time);
        locationTextView = (TextView) listItemView.findViewById(R.id.item_location);
        profissionTextView = (TextView) listItemView.findViewById(R.id.item_profission);
        acceptButton = (Button) listItemView.findViewById(R.id.accept_service_button);
        declineButton = (Button) listItemView.findViewById(R.id.decline_service_button);
        requesterNameTextView = (TextView) listItemView.findViewById(R.id.item_requester_name);

        final Service currentService = (Service) getItem(position);

        //set requester name
        setRequesterName(currentService.getRequester_id(), listItemView);

        //set TextViews values
        jobTextView.setText(currentService.getJob());
        descriptionTextView.setText(currentService.getDescription());
        dateTextView.setText(currentService.getDate());
        timeTextView.setText(currentService.getStartTime()+" - " + currentService.getEndTime());
        locationTextView.setText(currentService.getCity()+", "+ currentService.getNeighbor());
        profissionTextView.setText("("+currentService.getProfession()+")");

        //set accept button value
        final View finalListItemView = listItemView;
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignServiceToProvider(currentService.getRequester_id(), currentService.getService_id());
                finalListItemView.setVisibility(View.GONE);

            }
        });

        //set decline button value
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*final DatabaseReference potentialProvidersRef = FirebaseDatabase.getInstance().getReference().child("requester_services").child(currentService.getRequester_id()).child(currentService.getRequester_id()).child("potentialProvidersIds");
                potentialProvidersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int counter =0;
                        Log.v("MyTag",counter+"");
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            String potentialProviderId = snapshot.getValue(String.class);
                            Log.v("MyTag",counter+"");
                            if (potentialProviderId.equals(userId)){
                                removeProvider(counter,potentialProvidersRef);
                                return;
                            }
                            counter++;
                        }
                        Log.v("MyTag",counter+"");
                    }

                    private void removeProvider(int counter, DatabaseReference potentialProvidersRef) {
                        Log.v("MyTag",counter+"");
                        potentialProvidersRef.child(counter+"").removeValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }

                });//end of getting requester name*/
                makeItemsInvisible();

            }
        });

        //set undo button value
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //currentService.getPotentialProvidersIds().add(userId);
                makeItemsVisible();
            }
        });

        return listItemView;

    }

    private void assignServiceToProvider(String requesterId, String serviceNumber) {
        DatabaseReference serviceRef = FirebaseDatabase.getInstance().getReference().child("requester_services").child(requesterId).child(serviceNumber);
        serviceRef.child("provider_id").setValue(userId);
    }

    private void makeItemsVisible() {
        jobTextView.setVisibility(View.VISIBLE);
        descriptionTextView.setVisibility(View.VISIBLE);
        dateTextView.setVisibility(View.VISIBLE);
        timeTextView.setVisibility(View.VISIBLE);
        locationTextView.setVisibility(View.VISIBLE);
        profissionTextView.setVisibility(View.VISIBLE);
        requesterNameTextView.setVisibility(View.VISIBLE);
        acceptButton.setVisibility(View.VISIBLE);
        declineButton.setVisibility(View.VISIBLE);

        undoButton.setVisibility(View.INVISIBLE);
    }

    private void makeItemsInvisible() {
        jobTextView.setVisibility(View.INVISIBLE);
        descriptionTextView.setVisibility(View.INVISIBLE);
        dateTextView.setVisibility(View.INVISIBLE);
        timeTextView.setVisibility(View.INVISIBLE);
        locationTextView.setVisibility(View.INVISIBLE);
        profissionTextView.setVisibility(View.INVISIBLE);
        requesterNameTextView.setVisibility(View.INVISIBLE);
        acceptButton.setVisibility(View.INVISIBLE);
        declineButton.setVisibility(View.INVISIBLE);

        undoButton.setVisibility(View.VISIBLE);

    }


    //getting the requester name from the requester id
    private void setRequesterName(String requester_id, final View listItemView) {
        //auth table reference
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        //user reference
        FirebaseUser FBuser;
        //get user
        FBuser = mAuth.getCurrentUser();
        final String userId = FBuser.getUid();
        DatabaseReference userProfileRef = FirebaseDatabase.getInstance().getReference().child("user_profiles").child(requester_id).child("name");
        //name listener
        userProfileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requesterName = dataSnapshot.getValue(String.class);
                requesterNameTextView.setText(requesterName+":");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });//end of getting requester name
    }


}
