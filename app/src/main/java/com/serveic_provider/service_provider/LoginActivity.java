package com.serveic_provider.service_provider;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
//import com.serveic_provider.service_provider.serviceProvider.FontsOverride;
import com.serveic_provider.service_provider.serviceProvider.User;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    pl.droidsonroids.gif.GifImageView spinner;
    LinearLayout loginElements;


    // For debugging
    private static final String TAG = "LoginActivity";

    // Used to get the userId
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    FirebaseUser FBuser;

    // Getting the user_profiles node
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference userProfileRef = mDatabase.getReference("user_profiles");

    //Very important to understand
    // User object to bind the return node in teh Firebase to the User class. So the user.type will
    // be connected to user_profiles/$userId/type/value of type. The same for the others
    // (but not in this activity) so user.age
    // will be connected to user_profiles/$userId/age/value of age
    User user;

    // Initializing the UI elements to get them ready for binding
    @BindView(R.id.email_edit_text)
    EditText emailText;
    @BindView(R.id.password_edit_text)
    EditText passwordText;
    @BindView(R.id.sign_up_button)
    Button signUpButton;
    @BindView(R.id.sign_in_button)
    Button getSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        runFadeOutAnimation();

        // Binding the UI elements
        ButterKnife.bind(this);

        spinner = (pl.droidsonroids.gif.GifImageView)findViewById(R.id.progressBar1);
        loginElements= (LinearLayout) findViewById(R.id.login_Elements);
        spinner.setVisibility(View.GONE);



    }

    // Assigning onClick for the sign IN using Butterknife instead of the onClick on the XML file
    @OnClick(R.id.sign_in_button)
    public void login() {
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        showSpenner();

        // Checking for an empty fields
        if(!isEmailEmpty(email) && !isPasswordEmpty(password))
            //FirebaseAuth method
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                // The userId of the authenticated user, the unique id for all of the users used in the Firebase Database
                                // user.getEmail() return the email in String
                                FBuser = mAuth.getCurrentUser();
                                final String userId = FBuser.getUid();

                                // setting the token which used for notification
                                setTokenId(userId);

                                // Notifying the user
                                Toast.makeText(LoginActivity.this, "Authentication successed",
                                        Toast.LENGTH_SHORT).show();
                                // reading the data
                                readUserTypeFromFBDB(userId);

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                                hideSpenner();

                                updateUI(null);
                            }
                        }
                    });
        // Empty fields
        else{
            Toast.makeText(LoginActivity.this, "One of the fields is empty",
                    Toast.LENGTH_SHORT).show();
            hideSpenner();

        }
    }

    // User will go to the suited interface
    private void updateUI(FirebaseUser FBuser) {
        // Checking if the user is authenticated
        if(FBuser != null) {
            String userType = user.getType();
            if(userType.equals("provider")){
                startActivity(new Intent(this ,ProviderHomeActivity.class));
            } else if (userType.equals("requester")){
                startActivity(new Intent(this ,RequesterHomeActivity.class));
            }
        }

    }

    // reading the profile of the user from the node user_profiles/$userId
    // This is not real time reading since it uses addListenerForSingleValueEvent
    // to do realtime user the other type of listener
    private void readUserTypeFromFBDB(String userId){
        ValueEventListener typeListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get User object and use the values to update the UI
                // The value of user_profiles/$userId/object is bounded with the user object's
                // attributes meaning that user.types is now, user_profiles/$userId/tpe/value
                user = dataSnapshot.getValue(User.class);
                // Changing the UI here to solve the problem of the program continuing execution
                // before the Firebase get the data. This solves the problem Othman stating
                if(user.getIs_baned()==1){
                    startActivity(new Intent(LoginActivity.this, InactiveAccount.class));
                    hideSpenner();
                }
                else if(user.getIs_active()==0){
                    startActivity(new Intent(LoginActivity.this, InactiveAccount.class));
                    hideSpenner();
                }
                else
                    updateUI(FBuser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadUserType:onCancelled", databaseError.toException());

                // ...
            }
        };
        userProfileRef.child(userId).addListenerForSingleValueEvent(typeListener);
        // Adding the listener to the node so that it executes the above methods
    }

    // Assigning onClick for the sign up using Butterknife instead of the onClick on the XML file
    @OnClick(R.id.sign_up_button)
    public void newUser(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
    }
    // Checks for empty Email
    private Boolean isEmailEmpty(String email) {
        return email.equals("");
    }
    // Checks for empty Password
    private Boolean isPasswordEmpty(String password) {
        return password.equals("");
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

    private void setTokenId(final String userId) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Stored under user id in user_profiles
                        String token = task.getResult().getToken();
                        Log.d(TAG, "current token: " + token);
                        userProfileRef.child(userId).child("token_id").setValue(token);
                    }
                });
    }


    public void showSpenner(){
        spinner = (pl.droidsonroids.gif.GifImageView)findViewById(R.id.progressBar1);
        loginElements= (LinearLayout) findViewById(R.id.login_Elements);

        spinner.setVisibility(View.VISIBLE);
        loginElements.setVisibility(View.GONE);

    }
    public void hideSpenner(){
        spinner = (pl.droidsonroids.gif.GifImageView)findViewById(R.id.progressBar1);
        loginElements= (LinearLayout) findViewById(R.id.login_Elements);

        spinner.setVisibility(View.GONE);
        loginElements.setVisibility(View.VISIBLE);
    }

    private void runFadeOutAnimation(){
        Animation a = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        a.reset();
        RelativeLayout ll = (RelativeLayout) findViewById(R.id.login_layout);
        ll.clearAnimation();
        ll.startAnimation(a);
    }

}
