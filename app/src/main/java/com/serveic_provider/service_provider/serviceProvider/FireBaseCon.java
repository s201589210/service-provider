package com.serveic_provider.service_provider.serviceProvider;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
/*
fireBaseCon class controls all communication of the system to the firebase data base
*/
public class FireBaseCon {
    //list of all system objects;
    User user;
    public FireBaseCon(){

    }
    public void getObj(final String table, String id, final fireBaseCallBack fbcb){
        // get instance of the firebase db
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // reference to the database based on the provided input @table and the desired node @id
        DatabaseReference mUserRef = database.getReference().child(table).child(id);
        //Listener to fetch data
        ValueEventListener userListner = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // initiate the correct object based on @table
                if(table.toLowerCase()=="user")
                    user = dataSnapshot.getValue(User.class);
                // ...

                //notify interface once data is delivered
                fbcb.onCallback(user);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // error the object was not found
                Log.w("loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mUserRef.addValueEventListener(userListner);
    }
    public void insertObj(String table,Object obj){
        table = table.toLowerCase().trim();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mUserRef = database.getReference();

        if(table=="user"){
            user = (User) obj;
            mUserRef.child(table).child(user.getUsername()).setValue(obj);
        }

    }

}
