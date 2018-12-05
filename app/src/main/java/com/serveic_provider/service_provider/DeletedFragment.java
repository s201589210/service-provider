package com.serveic_provider.service_provider;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.serveic_provider.service_provider.R;
import com.serveic_provider.service_provider.adapters.ServiceAdapter;
import com.serveic_provider.service_provider.serviceProvider.Service;

import java.util.ArrayList;

public class DeletedFragment extends Fragment {
    View view;
    DatabaseReference typeRef;
    FirebaseDatabase mDatabase;
    ArrayList<Service> penddingServices = new ArrayList<Service>();

    public DeletedFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.pending_fragment, container, false);

        return view;
    }//on create method



}

