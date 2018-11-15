package com.serveic_provider.service_provider;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.serveic_provider.service_provider.classes.java.FireBaseCon;
import com.serveic_provider.service_provider.classes.java.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

    }

    public void signup(View view) {
        String email = ((EditText)findViewById(R.id.editText)).getText().toString();
        String username = ((EditText)findViewById(R.id.editText3)).getText().toString();
        String password = ((EditText)findViewById(R.id.editText4)).getText().toString();
        String type = "signup";
        /*Add in Oncreate() funtion after setContentView()*/
        Switch simpleSwitch = (Switch) findViewById(R.id.typeSwitch); // initiate Switch
        String error;

        //validate inputs
        if(username.matches("")|| email.matches("") || password.matches("")){
            error = "some fields are empty";
        }
        else {
            //check if user contains special char
            Pattern p = Pattern.compile("[^A-Za-z0-9]+");
            Matcher m = p.matcher(username);
            if(m.find()){
                error = "username should only contains numbers and characters";
            }
            else{
                //input are valid create new user
                User user = new User();
                //set all attribute
                user.setUsername(username);
                user.setEmail(email);
                user.setPassword(password);
                // create new firebase connection
                FireBaseCon fbc = new FireBaseCon();
                //pass user object to be inserted
                fbc.insertObj("user", user);
            }
        }

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, email, username, password);
    }


    private class BackgroundWorker extends AsyncTask<String, Void, String> {
        Context context;
        AlertDialog alertDialog;

        BackgroundWorker (Context context){
            this.context = context;
        }

        @Override
        protected String doInBackground(String... voids) {
            /// Othman !! please write your code here mmmmmmmmmmmmm no

            return null;
        }

        @Override
        protected void onPreExecute() {
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("sign up result");
        }

        @Override
        protected void onPostExecute(String responce) {
            alertDialog.setMessage(responce);
            alertDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }
}
