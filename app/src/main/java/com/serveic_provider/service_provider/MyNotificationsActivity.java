package com.serveic_provider.service_provider;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.serveic_provider.service_provider.adapters.NotificationAdapter;
import com.serveic_provider.service_provider.adapters.ServiceAdapter;
import com.serveic_provider.service_provider.firebase_notification.Notification;
import com.serveic_provider.service_provider.fragments.InProgressFragment;
import com.serveic_provider.service_provider.serviceProvider.Service;

import java.util.ArrayList;

public class MyNotificationsActivity extends AppCompatActivity {
    ArrayList<Notification> notifications = new ArrayList<>();
    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notifications);

        getNotifications();
    }

    private void getNotifications() {
        //auth table reference
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        //user reference
        FirebaseUser FBuser;
        //get user
        FBuser = mAuth.getCurrentUser();
        final String userId = FBuser.getUid();
        //requster_location reference
        DatabaseReference userNotifications = FirebaseDatabase.getInstance().getReference().child("user_notifications").child(userId);

        //find the type of the user
        userNotifications.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //loop over all notifications
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Notification notification = snapshot.getValue(Notification.class);
                    notifications.add(notification);
                }

                ListView listView = findViewById(R.id.notification_list_view);
                // adapter knows how to create list items for each item in the list.
                NotificationAdapter adapter = new NotificationAdapter(activity, notifications, R.color.colorWhite);
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
