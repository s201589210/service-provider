package com.serveic_provider.service_provider;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.serveic_provider.service_provider.serviceProvider.Service;

import java.util.Calendar;

public class Utils {

    //updates services' statuses
    public static void updateServiceStatus() {
        //auth table reference
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        //user reference
        FirebaseUser FBuser;
        //get user
        FBuser = mAuth.getCurrentUser();
        final String userId = FBuser.getUid();
        DatabaseReference userTypeRef = FirebaseDatabase.getInstance().getReference().child("user_profiles").child(userId).child("type");

        //find the type of the user
        userTypeRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userType = dataSnapshot.getValue(String.class);
                if (userType.equals("requester")){
                    updateServicesForRequester(userId);
                }else{
                    updateServicesForProvider(userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private static void updateServicesForRequester(String userId) {
        final DatabaseReference requesterServicesRef = FirebaseDatabase.getInstance().getReference().child("requester_services").child(userId);

        //provider_services listener
        requesterServicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Service service = snapshot.getValue(Service.class);
                    updateService(service, requesterServicesRef);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private static void updateServicesForProvider(String userId) {
        DatabaseReference providerServicesRef = FirebaseDatabase.getInstance().getReference().child("provider_services").child(userId);

        //provider_services listener
        providerServicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //serviceId = requester ID + service number
                    String serviceId = snapshot.getValue(String.class);
                    String requesterId = serviceId.substring(0,serviceId.indexOf("_"));
                    String serviceNumber = serviceId.substring(serviceId.indexOf("_")+1);
                    //getting the service using the service id
                    updateServicesForRequester(requesterId);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private static void updateServiceStatus(String requesterId, String serviceNumber) {

        final DatabaseReference requesterServicesRef = FirebaseDatabase.getInstance().getReference().child("requester_services").child(requesterId).child(serviceNumber);
        //provider_services listener
        requesterServicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Service service = dataSnapshot.getValue(Service.class);
                updateService(service, requesterServicesRef);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });//end of updating services
    }

    private static void updateService(Service service, DatabaseReference requesterServicesRef) {
        //if a service is in "pending" check for its time
        if(service.getStatus().equals("pending")) {
            //if time has past check if it has a provider
            if (isTimePassed(service)) {
                //if it does have a provider then it becomes "in progress"
                if (!service.getProvider_id().equals("none")){
                    requesterServicesRef.child("status").setValue("in progress");
                }
                //it it does not have a provider then it becomes "deleted"
                else{
                    requesterServicesRef.child(service.getService_id()).child("status").setValue("deleted");
                }
            }
        }
    }

    private static boolean isTimePassed(Service service) {
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
