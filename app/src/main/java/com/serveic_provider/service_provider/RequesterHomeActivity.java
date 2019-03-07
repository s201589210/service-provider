package com.serveic_provider.service_provider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toolbar;


public class RequesterHomeActivity extends Activity {
    ImageView profile_button;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    ShareActionProvider mShareActionProvider;
    String[] listArray;
    ListView drawerListView;
    ActionBarDrawerToggle mActionBarDrawerToggle;
    DrawerLayout mDrawerLayout;
    DrawerLayout drawer;
    Context context = this;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requester_home_page);
        setUpToolBar();
        Utils.updateServiceStatus();
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

    private void setUpToolBar() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ImageView drawerButton = findViewById(R.id.drawer_button);
        drawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    public void goToMyServicesPage(View view) {
        startActivity(new Intent(this,MyServicesActivity.class));
    }
}
