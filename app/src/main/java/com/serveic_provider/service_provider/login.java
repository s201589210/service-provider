package com.serveic_provider.service_provider;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.serveic_provider.service_provider.classes.java.FireBaseCon;
import com.serveic_provider.service_provider.classes.java.User;
import com.serveic_provider.service_provider.classes.java.fireBaseCallBack;


public class login extends AppCompatActivity {
    private EditText username, password;
    private Button btnLogin;
    AlertDialog alertDialog;
    String usernameInp ;
    String passwordInp ;
    boolean isValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText)findViewById(R.id.edtUsername);
        password = (EditText)findViewById(R.id.edtPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        alertDialog = alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("login result");
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
               // resTxt.setText(username.getText());
                if(username.getText().toString()!=null && password.getText().toString()!=null){
                    usernameInp = username.getText().toString().trim();
                    passwordInp = password.getText().toString().trim();
                    login();
                }
            }
        });
    }

    public void login() {
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! warning any special character input will cause a crash !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        FireBaseCon fbc = new FireBaseCon();
        //retrieve user object from data base with id=> username == input username
        fbc.getObj("user",usernameInp,new fireBaseCallBack() {
            @Override
            public void onCallback(Object user) {
                User u = (User)user;
                if(u==null)
                    alertDialog.setMessage("user doesn't exist");
                else if((u.getPassword()).equals(passwordInp)) {
                    isValid = true;
                }
                else {
                    alertDialog.setMessage("wrong password");
                }
            }
        });


        if(isValid){
            startActivity(new Intent(this,ServiceProviderHomePage.class));
        }else{
            alertDialog.show();
        }

    }

    public void newUser(View view) {
        startActivity(new Intent(this,SignUp.class));
    }
}
