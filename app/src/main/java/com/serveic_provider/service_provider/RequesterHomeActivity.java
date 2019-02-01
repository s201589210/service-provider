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




        ImageView prof_dyer =(ImageView)  findViewById(R.id.prof_dyer);

        prof_dyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* Intent myIntent = new Intent(RequesterHomeActivity.this, ListProvidersActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                RequesterHomeActivity.this.startActivity(myIntent);*/


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
        transit("Plumber");
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
        transit("Electrician");
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
     /*  public void  transit(String profession){
           Intent myIntent = new Intent(RequesterHomeActivity.this, ListProvidersActivity.class);
           myIntent.putExtra("profession", profession);
           RequesterHomeActivity.this.startActivity(myIntent);
        }*/


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
