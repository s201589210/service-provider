package com.serveic_provider.service_provider.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.serveic_provider.service_provider.MapsActivity;
import com.serveic_provider.service_provider.ProfileActivity;
import com.serveic_provider.service_provider.R;
import com.serveic_provider.service_provider.RateActivity;
import com.serveic_provider.service_provider.Report;
import com.serveic_provider.service_provider.serviceProvider.Service;
import com.serveic_provider.service_provider.serviceProvider.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServiceAdapter extends ArrayAdapter<Service> {

    private int colorid;
    private Context context;

    private Service service;
    private String senderId;
    private String recieverId;

    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference userRef;
    DatabaseReference userRates;

    String uid;
    double locationLat;
    double locationLng;

    TextView rateBtn;
    TextView confirmBtn;
    TextView reportBtn;
    TextView provNameTextView;
    TextView professionTextView;
    TextView raterNumTextView;
    TextView descriptionTextView;
    RatingBar ratingBar;
    ImageView imageBox;
    TextView profBtn;
    TextView locatBtn;
    public ServiceAdapter(Activity context, ArrayList<Service> words, int color) {
        super(context, 0, words);
        colorid=color;
        this.context = context;

        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
    }


    @Override

    public View getView(final int position, View convertView, ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.service_listview, parent, false);
        }
        //declare all view fields
        rateBtn = (TextView)listItemView.findViewById(R.id.rateBtn);
        profBtn = (TextView)listItemView.findViewById(R.id.profileBtn);
        locatBtn = (TextView)listItemView.findViewById(R.id.locationBtn);
        confirmBtn = (TextView)listItemView.findViewById(R.id.confirmBtn);
        reportBtn = (TextView)listItemView.findViewById(R.id.reportBtn);
        provNameTextView = (TextView) listItemView.findViewById(R.id.provider_name_text_view);
        professionTextView = (TextView) listItemView.findViewById(R.id.profession_text_view);
        raterNumTextView = (TextView) listItemView.findViewById(R.id.number_of_reviews);
        descriptionTextView = (TextView) listItemView.findViewById(R.id.serviceDesc);
        ratingBar = (RatingBar) listItemView.findViewById(R.id.rating_bar);
        imageBox = (ImageView) listItemView.findViewById(R.id.image);
        uid="-";
        // Get the {@link AndroidFlavor} object located at this position in the list
        final Service service = getItem(position);
        //if status is finished show the rate btn
        rateCheck(service);
        //if status is in progress show both confirm and report btns
        confirmCheck(service);
        //setting all fields
        setServiceFields( service,professionTextView, descriptionTextView);
        setProviderFields(service);
        //setting the profile btn listener
        setProfListener();
        //setting the location btn listener
        setLocationListener();

        View textcontainer = listItemView.findViewById(R.id.commentContainer_container);
        int color = ContextCompat.getColor(getContext(),colorid);
        textcontainer.setBackgroundColor(color);


        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;

    }
    public void setServiceFields(Service s,TextView professionTextView,TextView descriptionTextView){
        if(s.getProfession()!=null)
            professionTextView.setText(s.getProfession());
        if(s.getDescription()!=null)
            descriptionTextView.setText(s.getDescription());
        if(s.getImage()!=null){
            imageBox.setImageResource(service.getId());
            imageBox.setVisibility(View.VISIBLE);
        }
        else{
            imageBox.setVisibility(View.GONE);
        }
        if(s.getLocation()!=null){
            locationLat = Double.parseDouble(s.getLocation().split(",")[0]);
            locationLng = Double.parseDouble(s.getLocation().split(",")[1]);
        }
    }
    public void setProviderFields(final Service s){
        //check type of the user
        //current user id
        String currnetUserId ;
        //auth table reference
        FirebaseAuth mAuth = FirebaseAuth.getInstance();;
        //user reference
        FirebaseUser FBuser;
        //dRef
        DatabaseReference userProfileRef_type;
        //get user
        FBuser = mAuth.getCurrentUser();
        //get id
        currnetUserId = FBuser.getUid();
        userProfileRef_type = mDatabase.getReference("user_profiles").child(currnetUserId).child("type");
        userProfileRef_type.addListenerForSingleValueEvent(new ValueEventListener() { 
            @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String type = dataSnapshot.getValue(String.class);
                String userId;

                if(type.equals("provider")){
                    userId = s.getRequester_id();
                }
                else{
                    userId = s.getProvider_id();
                }
                if(!userId.equals("none")) {
                    uid = userId;
                    //get user profile
                    getUserProf(userId);
                }
             }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }
    public void setIds(final Service s, final String message){
        //check type of the user
        //current user id
        String currnetUserId ;
        //auth table reference
        FirebaseAuth mAuth = FirebaseAuth.getInstance();;
        //user reference
        FirebaseUser FBuser;
        //dRef
        DatabaseReference userProfileRef_type;
        //get user
        FBuser = mAuth.getCurrentUser();
        //get id
        currnetUserId = FBuser.getUid();
        userProfileRef_type = mDatabase.getReference("user_profiles").child(currnetUserId).child("type");
        userProfileRef_type.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String type = dataSnapshot.getValue(String.class);
                String recieverId;
                String senderId;
                if(type.equals("provider")){
                    recieverId = s.getRequester_id();
                    senderId = s.getProvider_id();
                }
                else{
                    recieverId = s.getProvider_id();
                    senderId = s.getRequester_id();
                }
                //send notification using the ids
                writeNotificaitonToFBDB(senderId, recieverId, message, s);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        

    }
    public void setProfListener(){
        final Intent intent1 = new Intent(this.getContext(), ProfileActivity.class);
        profBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Bundle bundle1 = new Bundle();
                bundle1.putString("userId",uid);
                intent1.putExtras(bundle1);
                getContext().startActivity(intent1);
            }
        });
    }
    public void setLocationListener(){
        String url = "http://maps.google.com/maps?daddr=" + locationLat + "," + locationLng;
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        locatBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getContext().startActivity(intent);
            }
        });

    }
    public void getUserProf(final String userId){
        userRef = mDatabase.getReference("user_profiles").child(userId);
        // user profile ref
        userRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                 User user = dataSnapshot.getValue(User.class);
                ratingBar.setRating(user.getRate());
                provNameTextView.setText(user.getName());

                userRates = mDatabase.getReference("rates").child(userId);
                userRates.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                        int rateNum = (int)dataSnapshot.getChildrenCount();
                        raterNumTextView.setText(rateNum+"");
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void rateCheck(final Service s){

        if(s.getStatus().equals("finished")){
            rateBtn.setVisibility(View.VISIBLE);
        }
        else{
            rateBtn.setVisibility(View.GONE);
        }
        //on click add service to intent and indicate user to be rated
        final Intent intent = new Intent(this.getContext(), RateActivity.class);
        rateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Bundle bundle = new Bundle();
                bundle.putSerializable("service",s);
                intent.putExtras(bundle);
                getContext().startActivity(intent);
            }
        });
    }
    public void confirmCheck(final Service s){
        if(s.getStatus().equals("in progress")){
            confirmBtn.setVisibility(View.VISIBLE);
            reportBtn.setVisibility(View.VISIBLE);
        }
        else{
            confirmBtn.setVisibility(View.GONE);
            reportBtn.setVisibility(View.GONE);
        }
        //on click add service to intent and indicate user to be report
        final Intent intent = new Intent(this.getContext(), Report.class);
        reportBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Bundle bundle = new Bundle();
                bundle.putSerializable("service",s);
                intent.putExtras(bundle);
                getContext().startActivity(intent);
            }
        });


        //on click set and send notification to other user in the service
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String message = "Please confirm that the service is finished";
                setIds(s, message);
            }
        });
    }

    //this used for sending notification while the receiving app is in background. It must
    // go to the database first. To reuse it take of the service element and redo the function
    public void writeNotificaitonToFBDB(String fromUserId, String toUserId,
                                        String messageText, final Service service) {
        String requesterServiceId = service.getRequester_id()+"_"+service.getService_id();
        Map<String, Object> notification = new HashMap<>();
        notification.put("message", messageText);
        notification.put("from", fromUserId);
        notification.put("serviceId", requesterServiceId);

        DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference()
                .child("user_notifications");
        notificationsRef.child(toUserId).push().setValue(notification)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Notifiaction Sent", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error in Sending Notification", Toast.LENGTH_SHORT).show();
                    }
                });
    }




}
