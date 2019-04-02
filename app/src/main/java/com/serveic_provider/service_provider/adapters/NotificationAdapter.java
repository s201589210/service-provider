package com.serveic_provider.service_provider.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.serveic_provider.service_provider.R;
import com.serveic_provider.service_provider.firebase_notification.Notification;

import java.util.ArrayList;

public class NotificationAdapter extends ArrayAdapter<Notification> {

    Context context;

    public NotificationAdapter(Activity context, ArrayList<Notification> words, int color) {
        super(context, 0, words);
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.notification_card, parent, false);
        };

        //get the notification at the current position
        Notification notification = getItem(position);

        //declare all view fields
        TextView serviceIDTextView = listItemView.findViewById(R.id.notification_service_id);
        TextView messageTextView = listItemView.findViewById(R.id.notification_message);

        //set all the fields
        setSenderName(notification.getFrom(),listItemView);
        serviceIDTextView.setText(notification.getServiceId());
        messageTextView.setText(notification.getMessage());

        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;

    }

    //getting the sender name from the sender id
    private void setSenderName(String requester_id, final View listItemView) {

        DatabaseReference userProfileRef = FirebaseDatabase.getInstance().getReference().child("user_profiles").child(requester_id).child("name");
        //name listener
        userProfileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String senderName = dataSnapshot.getValue(String.class);
                TextView senderTextView = listItemView.findViewById(R.id.notification_sender);
                senderTextView.setText(senderName+":");
                Log.v("MyTag",senderName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });//end of getting requester name
    }
}
