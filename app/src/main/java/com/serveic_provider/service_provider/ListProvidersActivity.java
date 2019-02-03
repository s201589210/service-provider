package com.serveic_provider.service_provider;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.serveic_provider.service_provider.adapters.ProviderAdaptor;
import com.serveic_provider.service_provider.serviceProvider.User;

import java.util.ArrayList;

public class ListProvidersActivity extends AppCompatActivity {
    private static final String TAG = "ListProvidersActivity";
    final ArrayList<User> professionProviderList = new ArrayList<User>();
    String name;
    String company;
    String location;
    String profession;
    String city;
    DatabaseReference locationRef;
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    //----------------------------------------------------------///
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_providers);
        //get information based with last activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            profession = extras.getString("profession");
            //The key argument here must match that used in the other activity
            //auth table reference
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            ;
            //user reference
            FirebaseUser FBuser;
            //get user
            FBuser = mAuth.getCurrentUser();
            String requsterID = FBuser.getUid();
            //requster_location reference
            final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
            locationRef = mDatabase.getReference().child("user_profiles").child(requsterID).child("location");
            //location listner
            locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    location = dataSnapshot.getValue(String.class);
                    buildList(location);
                    Log.v("potato","we are in the location listner");
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });//end of getting location
        }//end of checking extras!=null

       final Button Btn = (Button) findViewById(R.id.slect_button);

        Btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(ListProvidersActivity.this, MyServicesActivity.class));
            }
        });




    }//on create method
    //appends provider to view
    public void buildList(String location) {

        //refernce to the profession providers
        DatabaseReference ProfissionRef = rootRef.child("profession_location_provider");
        DatabaseReference professionLocationRef = ProfissionRef.child(profession).child(location);
        //getting providers of  location_Profession
        professionLocationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //loop over all providers ids
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String providerID = snapshot.getValue(String.class);
                    Log.v("potato", providerID);
                        addProvInfoToList(providerID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }// end of building list
    //get the provider profile information and insert them to the list
    public void addProvInfoToList(final String providerID){
        //refernce to the user profile
        DatabaseReference profilesRef = rootRef.child("user_profiles");
        DatabaseReference providerProfileRef = profilesRef.child(providerID);

        providerProfileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //construct new user === firebase user
                User user = dataSnapshot.getValue(User.class);
                Log.v("addingInfo","");
                buildItem(user,providerID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }//end of adding item to list
    //add the provider(user) to an item
    public void buildItem(final User user,final String providerID){
        ListView listView = (ListView) findViewById(R.id.lists);
        professionProviderList.add(user);
        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        ProviderAdaptor adapter = new ProviderAdaptor(ListProvidersActivity.this,professionProviderList, R.color.colorPrimary);
        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // activity_numbers.xml layout file.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("helloVetnam", ": ");
                Intent createService = new Intent(ListProvidersActivity.this,CreateServiceActivity.class);
                createService.putExtra("providerID_profession", providerID+"_"+ profession);
                ListProvidersActivity.this.startActivity(createService);
            }
        });
        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
          Log.v("itemBuild", "buildItem: ");
        listView.setAdapter(adapter);
        listView.setClickable(true);
    }// end of building item

}