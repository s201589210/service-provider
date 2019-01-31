package com.serveic_provider.service_provider.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
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

        // Get the {@link AndroidFlavor} object located at this position in the list
        final Service currentService = (Service) getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.item_job);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        nameTextView.setText(currentService.getJob());
        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView numberTextView = (TextView) listItemView.findViewById(R.id.item_requester_name);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        numberTextView.setText(currentService.getProvider_id());

        TextView PriceTextView = (TextView) listItemView.findViewById(R.id.item_date);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        PriceTextView.setText(currentService.getDate());

        TextView FromTextView = (TextView) listItemView.findViewById(R.id.item_location);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        FromTextView.setText(currentService.getLocation()+"");

        Button acceptButton = (Button) listItemView.findViewById(R.id.accept_service_button);
        final View finalListItemView = listItemView;
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //auth table reference
                FirebaseAuth mAuth = FirebaseAuth.getInstance();

                //user reference
                FirebaseUser FBuser;
                //get user
                FBuser = mAuth.getCurrentUser();
                final String userId = FBuser.getUid();
                //requster_location reference
                mDatabase = FirebaseDatabase.getInstance();
                typeRef = mDatabase.getReference().child("provider_services").child(userId).child(position+"");
                //location listner
                typeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //serviceId = requester ID + service number
                        String serviceId = dataSnapshot.getValue(String.class);
                        String requesterId = serviceId.substring(0,serviceId.indexOf("_"));
                        String serviceNumber = serviceId.substring(serviceId.indexOf("_")+1);
                        //assigns he provider to the service
                        assignServiceToProvider(requesterId, serviceNumber);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }

                    private void assignServiceToProvider(String requesterId, String serviceNumber) {
                        DatabaseReference serviceRef = mDatabase.getReference().child("requester_services").child(requesterId).child(serviceNumber);
                        serviceRef.child("provider_id").setValue(userId);
                    }
                });//end of getting location
                finalListItemView.setVisibility(View.GONE);

            }
        });

        Button declineButton = (Button) listItemView.findViewById(R.id.decline_service_button);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //auth table reference
                FirebaseAuth mAuth = FirebaseAuth.getInstance();

                //user reference
                FirebaseUser FBuser;
                //get user
                FBuser = mAuth.getCurrentUser();
                final String userId = FBuser.getUid();
                //remove value so it does not show again
                mDatabase.getReference().child("provider_services").child(userId).child(position+"").removeValue();
                finalListItemView.setVisibility(View.GONE);

            }
        });
        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;

    }


}
