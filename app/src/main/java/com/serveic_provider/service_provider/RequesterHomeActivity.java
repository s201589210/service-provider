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




        ImageView prof_dyer = findViewById(R.id.prof_dyer);

        prof_dyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(RequesterHomeActivity.this, ListProviders.class);
                //myIntent.putExtra("key", value); //Optional parameters
                RequesterHomeActivity.this.startActivity(myIntent);


            }

        });





    }


    // Not allowing back button
    @Override
    public void onBackPressed() {

    }
}
