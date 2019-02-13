package com.serveic_provider.service_provider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.serveic_provider.service_provider.serviceProvider.User;
import com.squareup.picasso.Picasso;

public class EditProfileActivity extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "ViewDatabase";

    private Button chooseImageButton;
    private Button doneButton;
    private TextView userType;
    private EditText nameEditText;
    private EditText ageEditText;
    private EditText cityEditText;
    private ImageView myImageView;
    //private ProgressBar comProgressBar;
    private String userID;

    private Uri myImageUri;

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference userProfileRef;
    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        chooseImageButton = findViewById(R.id.chooseImageBtn);
        doneButton = findViewById(R.id.doneBtn);
        userType= findViewById(R.id.userType);
        nameEditText = findViewById(R.id.nameEditText);
        ageEditText = findViewById(R.id.ageEditText);
        cityEditText = findViewById(R.id.cityEditText);
        myImageView = findViewById(R.id.myImageView);
        //comProgressBar = findViewById(R.id.progressBarCom);

        Log.v("potato", "successfully viewed");

        getUserInfo();
        Log.v("potato", user.getName());
        userType.setText(user.getType());
        nameEditText.setText(user.getName());
        ageEditText.setText(user.getAge());
        cityEditText.setText(user.getLocation());




        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
       /*/ doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });/*/


    }


            private void getUserInfo() {
        Log.v("potato", "successfully entered get user info ");
        //auth table reference
        //FirebaseAuth mAuth = FirebaseAuth.getInstance();;
        //user reference
        //FirebaseUser FBuser;
        //get user
        //FBuser = mAuth.getCurrentUser();
        //get id
        //userID = FBuser.getUid();
          userID = "QacwWNo8N1NmdLYHn3G2crtAlSq2" ;


        //User_profile reference
                userProfileRef= rootRef.child("user_profiles").child(userID) ;
                Log.v("potato", "successfully accessed ");
        userProfileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //construct new user === firebase user
                showData(dataSnapshot);

                //user = dataSnapshot.getValue(User.class);
               // user.setUid(userID);

                Log.v("potato", "successfully read ");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()) {

            user.setName(ds.child(userID).getValue(User.class).getName()); //set the name
            user.setLocation(ds.child(userID).getValue(User.class).getLocation()); //set the location
            user.setAge(ds.child(userID).getValue(User.class).getAge()); //set the age
            user.setType(ds.child(userID).getValue(User.class).getType()); //set the type

            //display all the information
            Log.d(TAG, "showData: name: " + user.getName());
            Log.d(TAG, "showData: Location: " + user.getLocation());
            Log.d(TAG, "showData: age: " + user.getAge());
            Log.d(TAG, "showData: type: " + user.getType());
        }
    }


         private void openFileChooser() {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }

            @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);

                if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                        && data != null && data.getData() != null) {
                    myImageUri = data.getData();

                    Picasso.get().load(myImageUri).fit().into(myImageView);


                }
            }
}

