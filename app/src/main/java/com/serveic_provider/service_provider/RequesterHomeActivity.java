package com.serveic_provider.service_provider;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.serveic_provider.service_provider.serviceProvider.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class RequesterHomeActivity extends AppCompatActivity {
    ImageView profile_button;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requester_home_page);
        updateServicesStatus();
        profile_button = (ImageView)findViewById(R.id.profile_button);

        final Intent intent1 = new Intent(RequesterHomeActivity.this, ProfileActivity.class);
        profile_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //check type of the user
                //current user id
                String currnetUserId ;
                //user reference
                FirebaseUser FBuser;
                //dRef
                DatabaseReference userProfileRef_type;
                //get user
                FBuser = mAuth.getCurrentUser();
                //get id
                currnetUserId = FBuser.getUid();
                final Bundle bundle1 = new Bundle();
                bundle1.putString("userId",currnetUserId);
                intent1.putExtras(bundle1);
                Log.w("ClickedHH",currnetUserId);
                startActivity(intent1);
            }
        });
    }

    // Not allowing back button
    @Override
    public void onBackPressed() {

    }

    public void onDyerClick(View view) {
        transit("dyer");
    }

    public void onPlumberClick(View view) {
        transit("plumber");
    }

    public void onWelderClick(View view) {
        transit("welder");
    }

    public void onGardenerClick(View view) {
        transit("gardener");
    }

    public void onMaidClick(View view) {
        transit("maid");
    }

    public void onElectricianClick(View view) {
        transit("electrician");
    }

    public void onCourierClick(View view) {
        transit("courier");
    }

    public void onCarpenterClick(View view) {
        transit("carpenter");
    }

    public void onBuilderClick(View view) {
        transit("builder");
    }


    //intent transtion based on the clicked view
    public void  transit(String profession){
        Intent myIntent = new Intent(RequesterHomeActivity.this, CreateServiceActivity.class);
        myIntent.putExtra("profession", profession);
        RequesterHomeActivity.this.startActivity(myIntent);
    }

    public void goToMyServicesPage(View view) {
        startActivity(new Intent(this,MyServicesActivity.class));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.logout){
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class)); //Go back to home page
            finish();
        }
        return true;
    }

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
                        if(isTimePassed(service)) {
                            requesterServicesRef.child(service.getService_id()).child("status").setValue("in progress");
                        }
                        // service is pending and has no provider
                    }else if(service.getStatus().equals("pending") && service.getProvider_id().equals("none")){
                        if(isTimePassed(service)) {
                            requesterServicesRef.child(service.getService_id()).child("status").setValue("deleted");
                        }
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
