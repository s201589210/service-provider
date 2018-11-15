package com.serveic_provider.service_provider;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
            /// Othman !! please write your code here
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
