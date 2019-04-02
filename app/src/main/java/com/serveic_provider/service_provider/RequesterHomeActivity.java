package com.serveic_provider.service_provider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.serveic_provider.service_provider.serviceProvider.User;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.widget.TextView;


public class RequesterHomeActivity extends Activity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DrawerLayout drawer;
    Context context = this;
    boolean drawerIsSet = false;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requester_home_page);
        setUpToolBar();

        Utils.updateServiceStatus();
    }

    private void setUpToolBar() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ImageView drawerButton = findViewById(R.id.drawer_button);
        drawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!drawerIsSet){
                    setUpDrawer();
                    drawerIsSet = true;
                }
                drawer.openDrawer(GravityCompat.START);
            }
        });

        TextView logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(context, LoginActivity.class)); //Go back to home page
                finish();
            }
        });
    }

    private void setUpDrawer() {
        final Intent intent1 = new Intent(this, ProfileActivity.class);
        findViewById(R.id.drawer_profile_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check type of the user
                //current user id
                String currnetUserId ;
                //auth table reference
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
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
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        //auth table reference
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        //user reference
        FirebaseUser FBuser;
        //get user
        FBuser = mAuth.getCurrentUser();
        final String userId = FBuser.getUid();
        DatabaseReference userProfileRef_type;
        userProfileRef_type = FirebaseDatabase.getInstance().getReference("user_profiles").child(userId);
        userProfileRef_type.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                ((TextView)findViewById(R.id.drawer_username)).setText(user.getName()+ " " + user.getLastName());
                ((TextView)findViewById(R.id.drawer_user_type)).setText(user.getType());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    // Not allowing back button
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
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

    public void goToMyServices(MenuItem item) {
        startActivity(new Intent(this, MyServicesActivity.class));
        drawer.closeDrawer(GravityCompat.START);
    }

    public void goToMyNotifications(MenuItem item) {
        startActivity(new Intent(this, MyNotificationsActivity.class));
        drawer.closeDrawer(GravityCompat.START);
    }
}
