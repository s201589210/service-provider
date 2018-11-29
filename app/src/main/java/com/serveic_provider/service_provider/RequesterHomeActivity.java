package com.serveic_provider.service_provider;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RequesterHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requester_home_page);
    }



    // Not allowing back button
    @Override
    public void onBackPressed() {

    }
}
