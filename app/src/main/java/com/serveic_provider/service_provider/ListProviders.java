package com.serveic_provider.service_provider;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListProviders extends AppCompatActivity {
private static final String TAG ="ListProviders";
    String name ;
    private String company;
    private String location;
 //   private String profission;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_providers);




//const rootRef = firebase.database().ref();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ProfissionRef = rootRef.child("profession_location_provider");
        DatabaseReference ProfissionCityRef = ProfissionRef.child("Plumber").child("Dhahran");
        DatabaseReference nameRef = ProfissionCityRef.child("providerName");
        DatabaseReference locationRef = ProfissionCityRef.child("ProviderLocation");
        DatabaseReference CompanyRef = ProfissionCityRef.child("ProviderCompany");






        // Create a list of words
        final ArrayList<service> words = new ArrayList<service>();




         nameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 name = dataSnapshot.getValue(String.class);
             //   words.add(new service(name, "Cooker","11" ,"company name","DHAHRAN","KFUPM mall"));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                location = dataSnapshot.getValue(String.class);
          //      words.add(new service(name, profission,"11" ,"company name","DHAHRAN", location));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        CompanyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                company = dataSnapshot.getValue(String.class);
                words.add(new service(name, "Cooker","11" ,company,"DHAHRAN", location));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







       // words.add(new service(name, profission,"11" ,company,"DHAHRAN",location));
        words.add(new service(name, "Cooker","11" ,"company name","DHAHRAN","KFUPM mall"));
        words.add(new service("Hussain", "Cooker","11" ,"company name","DHAHRAN","KFUPM mall",R.drawable.electrician));
        words.add(new service("Hussain", "Cooker","11" ,"company name","DHAHRAN","KFUPM mall"));
        words.add(new service("Hussain", "Cooker","11" ,"company name","DHAHRAN","KFUPM mall"));
        words.add(new service("Hussain", "Cooker","11" ,"company name","DHAHRAN","KFUPM mall"));
        words.add(new service("Hussain", "Cooker","11" ,"company name","DHAHRAN","KFUPM mall"));
        words.add(new service("Hussain", "Cooker","11" ,"company name","DHAHRAN","KFUPM mall"));
        words.add(new service("Hussain", "Cooker","11" ,"company name","DHAHRAN","KFUPM mall"));
        words.add(new service("Hussain", "Cooker","11" ,"company name","DHAHRAN","KFUPM mall"));















        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        ServiceAdapter adapter = new ServiceAdapter(this,words, R.color.colorPrimary);
        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // activity_numbers.xml layout file.
        ListView listView = (ListView) findViewById(R.id.list);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                service Word = words.get(position);

            }
        });
        onButtonClickListener();

    }

    private static Button btn;


    public void onButtonClickListener() {
        btn = (Button)findViewById(R.id.slect_button);
        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder a_builder = new AlertDialog.Builder(ListProviders.this);
                        a_builder.setMessage("Do you want to Close this App !!!");

                        AlertDialog alert = a_builder.create();
                        alert.setTitle("Alert !!!");
                        alert.show();
                    }
                }
        );
    }




}