package com.serveic_provider.service_provider;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import com.serveic_provider.service_provider.classes.java.FireBaseCon;
import com.serveic_provider.service_provider.classes.java.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

    }

    public void signup(View view) {
        String email = ((EditText) findViewById(R.id.editText)).getText().toString();
        String username = ((EditText) findViewById(R.id.editText3)).getText().toString();
        String password = ((EditText) findViewById(R.id.editText4)).getText().toString();

        /*Add in Oncreate() funtion after setContentView()*/
        Switch simpleSwitch = (Switch) findViewById(R.id.typeSwitch); // initiate Switch

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(email, username, password);
    }


    private class BackgroundWorker extends AsyncTask<String, Void, Void> {
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
                    user.setEmail(inputs[0]);
                    user.setPassword(inputs[2]);
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

    }
}
