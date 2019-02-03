package com.serveic_provider.service_provider;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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
