package com.serveic_provider.service_provider.firebase_notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.serveic_provider.service_provider.ConfirmationActivity;
import com.serveic_provider.service_provider.R;
import com.serveic_provider.service_provider.SignUpActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessagingServic";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle(); //get title
            String message = remoteMessage.getNotification().getBody(); //get message
            String click_action = remoteMessage.getNotification().getClickAction(); //get click_action

            Log.d(TAG, "Message Notification Title: " + title);
            Log.d(TAG, "Message Notification Body: " + message);
            Log.d(TAG, "Message Notification click_action: " + click_action);

            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());
                Map data = remoteMessage.getData();
                sendNotification(title, message, click_action, data);
            }
        }
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
    }

    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
    }

    public void onNewToken(String token) {
        // Used to get the userId
        FirebaseAuth mAuth = FirebaseAuth.getInstance();;
        FirebaseUser FBuser;

        // The userId of the authenticated user, the unique id for all of the users used in the Firebase Database
        // user.getEmail() return the email in String
        FBuser = mAuth.getCurrentUser();
        String userId = FBuser.getUid();

        // Getting the user_profiles node
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userProfileRef = mDatabase.getReference("user_profiles");
        userProfileRef.child(userId).child("token_id").setValue(token);

        Log.d("newToken", token);
    }
    //this used for sending notification while the receiving app is in foreground. It must
    // go to the database first
    private void sendNotification(String title,String messageBody,
                                  String click_action, Map data) {
        Intent intent = new Intent(click_action);
        Bundle mBundle = new Bundle();
        mBundle.putString("service",  (String) data.get("service"));
        intent.putExtras(mBundle);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent
                .getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat
                .Builder(this, getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        int notificationId = (int) System.currentTimeMillis();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        notificationManager.notify(notificationId/* ID of notification */, notificationBuilder.build());
    }

}
