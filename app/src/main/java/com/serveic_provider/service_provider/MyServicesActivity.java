package com.serveic_provider.service_provider;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.serveic_provider.service_provider.adapters.ViewPagerAdapter;
import com.serveic_provider.service_provider.fragments.DeletedFragment;
import com.serveic_provider.service_provider.fragments.FinishedFragment;
import com.serveic_provider.service_provider.fragments.InProgressFragment;
import com.serveic_provider.service_provider.fragments.PendingFragment;

public class MyServicesActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    PendingFragment pendingFragment = new PendingFragment();
    InProgressFragment inProgressFragment = new InProgressFragment();
    FinishedFragment finishedFragment = new FinishedFragment();
    DeletedFragment deletedFragment = new DeletedFragment();
    FirebaseDatabase mDatabase  = FirebaseDatabase.getInstance();;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_services_page);
        setTitle("My Services");


        tabLayout = (TabLayout) findViewById(R.id.myServicesTab);
        appBarLayout = (AppBarLayout) findViewById(R.id.myServicesBar);
        viewPager = (ViewPager) findViewById(R.id.myServicesPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //Adding Fragments
        adapter.addFragment(pendingFragment, "Pending");
        adapter.addFragment(inProgressFragment, "In Progress");
        adapter.addFragment(finishedFragment, "Finished");
        adapter.addFragment(deletedFragment, "Deleted");
        //adapter Setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onBackPressed() {

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

                if(type.equals("provider")){

                    Intent i = new Intent(MyServicesActivity.this,WaitingScreen.class);
                    startActivity(i);

                }
                else{

                finish();
                }

            }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

            });







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_services_page, menu);

        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
       /*// searchView.setQueryHint(getString(R.string.searchServices));
       // searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
*/
        return true;
    }
}
