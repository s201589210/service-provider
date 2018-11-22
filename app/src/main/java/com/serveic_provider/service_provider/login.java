package com.serveic_provider.service_provider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.serveic_provider.service_provider.classes.java.ActionOnDataChange;
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
    Activity content = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText)findViewById(R.id.edtUsername);
        password = (EditText)findViewById(R.id.edtPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        alertDialog = alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("login result");
       // boolean tst = usernameInp.length() == 0 && passwordInp.length() == 0;
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                usernameInp = username.getText().toString().trim();
                passwordInp = password.getText().toString().trim();
                if(usernameInp.length() != 0 && passwordInp.length() != 0){
                    login();
                }
            }
        });
    }

    public void login() {
        btnLogin.setClickable(false);
        User u = new User();
        u.setUsername(usernameInp);
        u.setPassword(passwordInp);
        u.login(new ActionOnDataChange() {
            @Override
            public void onCallbackObj(Object value) {
            }
            @Override
            public void onCallbackString(String value) {
                alertDialog.setMessage(value);
              //  Log.w("potatos",value);
                if(value.equals("successful"))
                    startActivity(new Intent(content ,ServiceProviderHomePage.class));
                else
                    alertDialog.show();
            }
            @Override
            public void onCallbackInt(int value) {
            }
        });
        btnLogin.setClickable(true);
    }

    public void newUser(View view) {
        startActivity(new Intent(this,SignUp.class));
    }
}
