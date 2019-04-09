package com.serveic_provider.service_provider;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
        finish();
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
