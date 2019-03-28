package com.serveic_provider.service_provider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.serveic_provider.service_provider.serviceProvider.User;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

public class EditProfileActivity extends AppCompatActivity {

    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    FirebaseStorage storage;
    StorageReference storageReference;
    //user id
    String userId ;
    //declare all view element
    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText cityEditText;
    EditText phone;
    Button edtBtn;
    Button upldBtn;
    private ImageView imageView;


    User user;

    private static final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setTitle("Edit Profile");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        //assign all view fields
        firstNameEditText = (EditText)findViewById(R.id.edit_profile_first_name_edit_text);
        lastNameEditText = (EditText)findViewById(R.id.edit_profile_last_name_edit_text);
        cityEditText = (EditText)findViewById(R.id.edit_profile_city_edit_text);
        phone =(EditText)findViewById(R.id.edit_profile_phone_edit_text);
        edtBtn =(Button)findViewById(R.id.imgEdt);
        upldBtn =(Button)findViewById(R.id.imgUpld);
        imageView = (ImageView) findViewById(R.id.profile_image);

        //auth table reference
        FirebaseAuth mAuth = FirebaseAuth.getInstance();;
        //user reference
        FirebaseUser FBuser;
        //get user
        FBuser = mAuth.getCurrentUser();
        //get id
        userId = FBuser.getUid();

        buildProfile(userId);

        edtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
                upldBtn.setVisibility(View.VISIBLE);
            }
        });

        upldBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage(userId);
                upldBtn.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(String userId) {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            String st = UUID.randomUUID().toString();
            Log.w("here1",st + filePath);
            StorageReference ref = storageReference.child("userImages/"+userId+"/profileImage");
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(EditProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(EditProfileActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    public void buildProfile(final String userId){
        //build user obj from db
        DatabaseReference userProfileRef_type;
        userProfileRef_type = mDatabase.getReference("user_profiles").child(userId);
        userProfileRef_type.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                displayImage(userId);
                setFields(user);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public void displayImage(String userId){
        storageReference.child("userImages/"+userId+"/profileImage").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if(uri!=null) {

                    // Got the download URL for ''
                    Log.w("imageLink", uri.toString());
                    String imgUrl = uri.toString();
                    Picasso.get()
                            .load(imgUrl)
                            .into(imageView);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }
    public void setFields(User user){
        firstNameEditText.setText(user.getName());
        lastNameEditText.setText(user.getLastName());
        cityEditText.setText(user.getLocation());
        phone .setText(user.getPhone_number());
    }

    public void saveChanges() {
        user.setName(firstNameEditText.getText().toString());
        user.setLastName(lastNameEditText.getText().toString());
        user.setLocation(cityEditText.getText().toString());
        user.setPhone_number(phone.getText().toString());

        DatabaseReference userProfileRef = mDatabase.getReference("user_profiles");
        userProfileRef.child(userId).setValue(user);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_profile_changes,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.save_profile_button){
            saveChanges();
            finish();
        }

        return true;
    }
}
