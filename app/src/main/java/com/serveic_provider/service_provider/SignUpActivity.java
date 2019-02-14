package com.serveic_provider.service_provider;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.serveic_provider.service_provider.serviceProvider.User;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {
    // For debugging
    private static final String TAG = "SignUpActivity";

    // Used to get the userId
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    // Getting the user_profiles node
    DatabaseReference userProfileRef = mDatabase.getReference("user_profiles");

    //Getting the profession_location_provider
    DatabaseReference professionLocationRef = mDatabase.getReference("profession_location_provider");

    // The userId of the authenticated user
    private String userId;

    //Very important to understand
    // User object to bind the return node in teh Firebase to the User class. So the user.type will
    // be connected to user_profiles/$userId/type/value of type. The same for the others
    //  so user.location
    // will be connected to user_profiles/$userId/location/value of location
    User user = new User();

    // Password pattern
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$");

    // Initializing the UI elements to get them ready for binding
    @BindView(R.id.email_edit_text)
    EditText emailText;
    @BindView(R.id.first_name_edit_text)
    EditText firstNametext;
    @BindView(R.id.last_name_edit_text)
    EditText lastNametext;
    @BindView(R.id.password_edit_text)
    EditText passwordText;
    @BindView(R.id.password_confirm_edit_text)
    EditText passwordConfirmText;
    @BindView(R.id.location_spinner)
    Spinner locationSpinner;
    @BindView(R.id.profession_text_view)
    TextView professionTextView;
    @BindView(R.id.profession_spinner)
    Spinner professionSpinner;
    @BindView(R.id.group_radio_button)
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Binding the UI elements
        ButterKnife.bind(this);

        // Putting the locations array in the string.xml into the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(this, R.array.locations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);

        makeProfessionVisible();
        // Putting the professions array in the string.xml into the spinner
        ArrayAdapter<CharSequence> professionadapter = ArrayAdapter
                .createFromResource(this, R.array.professions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        professionSpinner.setAdapter(professionadapter);

    }
    // Assigning onClick for the sign up using Butterknife instead of the onClick on the XML file
    @OnClick(R.id.sign_up_button)
    public void signUp(View view) {
        // Validate information. this can be commented in case of testing to ease the sign up
        if (validateAll())
            mAuth.createUserWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser FBuser = mAuth.getCurrentUser();
                                // The userId of the authenticated user, the unique id for all of the users used in the Firebase Database
                                // user.getEmail() return the email in String
                                userId = FBuser.getUid();

                                // Getting the location form the spinner and assigning to user.location
                                String location = locationSpinner.getSelectedItem().toString().toLowerCase();
                                user.setLocation(location);
                                //user.setLocation(location);

                                // Getting the type from the radio buttons and assigning to user,type
                                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                                RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonId);
                                String type = selectedRadioButton.getText().toString().toLowerCase();
                                user.setType(type);
                                // Setting the name of the user
                                String name = firstNametext.getText() + " " + lastNametext.getText();
                                user.setName(name);
                                // Using both the unique userId and user object to write the type and location under user_profiles/$userId
                                writeUserToFBDB(userId, user);



                                if(type.equals("requester")) {
                                    updateUI(FBuser);

                                    // Notifying the user
                                    Toast.makeText(SignUpActivity.this, "You signed up successfully. Please sign in",
                                            Toast.LENGTH_SHORT).show();
                                }

                                else {
                                    // Getting the profession form the spinner and assigning for
                                    // profession_location_provider node
                                    String profession = professionSpinner.getSelectedItem().toString().toLowerCase();

                                    writeProfessionToFBDB(profession, location, userId);
                                    updateUI(FBuser);

                                    // Notifying the user
                                    Toast.makeText(SignUpActivity.this, "You signed up successfully. Please sign in",
                                            Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpActivity.this, "Sign up failed, try again.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
    }

    // Validation Methods
    private boolean validateAll() {
        return validateEmail() && validateFirstName() && validateLastName()
                && validatePassword() && validatePasswordConfirm();

    }

    private boolean validateEmail() {
        String emailInput = emailText.getText().toString().trim();

        if (emailInput.isEmpty()) {
            emailText.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            emailText.setError("Please enter a valid email address");
            return false;
        } else {
            emailText.setError(null);
            return true;
        }
    }

    private boolean validateFirstName() {
        String firstNameInput = firstNametext.getText().toString().trim();
        if (firstNameInput.isEmpty()) {
            firstNametext.setError("Field can't be empty");
            return false;
        } else if (!firstNameInput.matches("[a-zA-Z]+")) {
            firstNametext.setError("Please enter characters only and not white spaces");
            return false;
        } else {
            firstNametext.setError(null);
            return true;
        }
    }

    private boolean validateLastName() {
        String lastNameInput = lastNametext.getText().toString().trim();
        if (lastNameInput.isEmpty()) {
            lastNametext.setError("Field can't be empty");
            return false;
        } else if (!lastNameInput.matches("[a-zA-Z]+")) {
            lastNametext.setError("Please enter characters only and not white spaces");
            return false;
        } else {
            lastNametext.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = passwordText.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            passwordText.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            passwordText
                    .setError("At least 1 digit, 1 lowercase letter, 1 uppercase letter, no white spaces," +
                            " at least 8 characters");
            return false;
        } else {
            passwordText.setError(null);
            return true;
        }
    }

    private boolean validatePasswordConfirm() {
        String passwordInput = passwordConfirmText.getText().toString().trim();
        String passwordInput2 = passwordText.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            passwordConfirmText.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            passwordConfirmText.setError("Password is weak");
            return false;
        } else if (!passwordInput.equals(passwordInput2)) {
            passwordConfirmText.setError("Not equal passwords");
            return false;
        }
        else {
            passwordConfirmText.setError(null);
            return true;
        }
    }


    private void makeProfessionVisible() {
        // Getting the type from the radio buttons to show the prpfession
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                String type = checkedRadioButton.getText().toString().toLowerCase();
                if(type.equals("provider")) {
                    professionTextView.setVisibility(View.VISIBLE);
                    professionSpinner.setVisibility(View.VISIBLE);
                }
                else {
                    professionTextView.setVisibility(View.GONE);
                    professionSpinner.setVisibility(View.GONE);
                }
            }
        });
    }


    // Changing the UI if the user is authenticated
    private void updateUI(FirebaseUser user) {
        // Checking if the user is authenticated
        if(user != null)
            startActivity(new Intent(this, LoginActivity.class));
    }


    // Writing the type to the Firebase Database
    private void writeUserToFBDB(String userId, User user) {
        // Creating a new $userId node under the user_profiles node and assigning whole user object
        userProfileRef.child(userId).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "writeUserType:success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "writeUserType:failure");
                    }
                });
    }

    // Writing the profession_location_provider to the Firebase Database
    private void writeProfessionToFBDB(String profession, String location, String userId) {
        // Creating a new $userId node under the user_profiles node and assigning whole user object
        professionLocationRef.child(profession).child(location).push().setValue(userId);
    }
}
