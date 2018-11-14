package com.serveic_provider.service_provider;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.serveic_provider.service_provider.classes.java.FireBaseCon;
import com.serveic_provider.service_provider.classes.java.User;
import com.serveic_provider.service_provider.classes.java.fireBaseCallBack;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
    private EditText username, password;
    private Button btnLogin;
    private String result;
    private TextView resTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText)findViewById(R.id.edtUsername);
        password = (EditText)findViewById(R.id.edtPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        resTxt = (TextView)findViewById(R.id.txtViewRes);
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
               // resTxt.setText(username.getText());
                if(username.getText().toString()!="")
                    login();
            }
        });
    }

    public void login() {
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! warning any special character input will cause a crash !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        FireBaseCon fbc = new FireBaseCon();
        //retrieve user object from data base
        fbc.getObj("user",username.getText()+"",new fireBaseCallBack() {
            @Override
            public void onCallback(Object user) {
                User u = (User)user;
                if(user==null)
                    resTxt.setText("error");
                else
                    resTxt.setText("logged in with email"+u.getEmail());
            }
        });
    }
}
