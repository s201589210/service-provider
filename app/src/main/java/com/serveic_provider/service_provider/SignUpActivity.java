package com.serveic_provider.service_provider;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {
    // For debugging
    private static final String TAG = "SignUpActivity";

    // Used to get the userId
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    // Getting the user_profiles node
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference userProfileRef = mDatabase.getReference("user_profiles");

    // The userId of the authenticated user
    private String userId;

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
    @BindView(R.id.password_edit_text)
    EditText passwordText;
    @BindView(R.id.password_confirm_edit_text)
    EditText passwordConfirmText;
    @BindView(R.id.group_radio_button)
    RadioGroup radioGroup;
    @BindView(R.id.type_text_view)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Binding the UI elements
        ButterKnife.bind(this);
    }
    // Assigning onClick for the sign up using Butterknife instead of the onClick on the XML file
    @OnClick(R.id.sign_up_button)
    public void signUp(View view) {
        // Validate information. this can be commented in case of testing to ease the sign up
        if (validateEmail() && validatePassword() && validatePasswordConfirm())
            mAuth.createUserWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                // The userId of the authenticated user, the unique id for all of the users used in the Firebase Database
                                // user.getEmail() return the email in String
                                userId = user.getUid();
                                // Getting the type from the radio buttons
                                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                                RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonId);
                                String type = selectedRadioButton.getText().toString();

                                // Using both the unique userId and type to write the type under user_profiles/$userId/type
                                writeUserTypeToFBDB(userId, type);

                                // Notifying the user
                                Toast.makeText(SignUpActivity.this, "You signed up successfully. Please sign in",
                                        Toast.LENGTH_SHORT).show();

                                updateUI(user);

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
    }

    // Validation Methods

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

    private boolean validatePassword() {
        String passwordInput = passwordText.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            passwordText.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            passwordText.setError("Password is weak");
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

    // Changing the UI if the user is authenticated
    private void updateUI(FirebaseUser user) {
        // Checking if the user is authenticated
        if(user != null)
            startActivity(new Intent(this, LoginActivity.class));
    }
    // Writing the type to the Firebase Database
    private void writeUserTypeToFBDB(String userId, String type) {
        // Creating a new $userId/type/$type node under the user_profiles node
        userProfileRef.child(userId).child("type").setValue(type)
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

   /* private class BackgroundWorker extends AsyncTask<String, Void, Void> {
        Context context;
        AlertDialog alertDialog;

        BackgroundWorker(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(final String... inputs) {

            FireBaseCon fcb = new FireBaseCon();

            //validate inputs
            if (inputs[0].matches("") || inputs[1].matches("") || inputs[2].matches("")) {
                alertDialog.setMessage("some fields are empty");
            } else {
                //check if user contains special char
                Pattern p = Pattern.compile("[^A-Za-z0-9]+");
                Matcher m = p.matcher(inputs[1]);
                if (m.find()) {
                    alertDialog.setMessage("username should only contains numbers and characters");
                } else {
                    //input are valid create new user
                    User user = new User();
                    //set all attribute
                    user.setUsername(inputs[1]);
                   // user.setEmail(inputs[0]);
                   // user.setPassword(inputs[2]);
                    // create new firebase connection
                    FireBaseCon fbc = new FireBaseCon();
                    //pass user object to be inserted
                    fbc.insertObj("user", user);
                    //show success message
                    alertDialog.setMessage("you have successfully signed up");
                }
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("sign up result");
        }

        @Override
        protected void onPostExecute(Void value) {
            alertDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }*/
}
