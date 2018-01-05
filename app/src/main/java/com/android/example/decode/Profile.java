package com.android.example.decode;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mRef;
    DatabaseReference userRef;
    StorageReference storageReference;

    CircleImageView civDp;
    Bitmap photo;
    Uri url;

    EditText etName;
    EditText etStatus;
    Button btnSave;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        progressDialog = new ProgressDialog(Profile.this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please Wait...");

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();

        etName = (EditText) findViewById(R.id.editText_name);
        etStatus = (EditText) findViewById(R.id.editText_status);
        btnSave = (Button) findViewById(R.id.button_save);

        progressDialog.show();
        userRef = mRef.child("users").child(mAuth.getCurrentUser().getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    User u = dataSnapshot.getValue(User.class);
                    if (u != null) {
                        etName.setText(u.getName());
                        Glide.with(Profile.this).load(dataSnapshot.getValue(User.class).getDpUrl()).into(civDp);
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

        civDp = (CircleImageView) findViewById(R.id.circleImageView_dp);

        civDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(Profile.this);
            }
        });

        btnSave = (Button) findViewById(R.id.button_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(etName.getText())) {
                    progressDialog.show();

                    if (civDp.getDrawable() != null) {
                        progressDialog.show();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        photo = ((BitmapDrawable) civDp.getDrawable()).getBitmap();
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] image = baos.toByteArray();
                        String path = "images/" + mAuth.getCurrentUser().getUid() + ".JPEG";
                        storageReference = FirebaseStorage.getInstance().getReference(path);

                        UploadTask uploadTask = storageReference.putBytes(image);
                        uploadTask.addOnSuccessListener(Profile.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                url = taskSnapshot.getDownloadUrl();
                                userRef.setValue(new User(mAuth.getCurrentUser().getUid(), etName.getText().toString(), url.toString()))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(Profile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(Profile.this, MainActivity.class));
                                                finish();
                                                progressDialog.dismiss();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Profile.this, "Profile Update Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Profile.this, "Profile Update Failed", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });

                    } else {

                        userRef.setValue(new User(mAuth.getCurrentUser().getUid(), etName.getText().toString(), ""))
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            Toast.makeText(Profile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Profile.this, MainActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(Profile.this, "Profile Update Failed", Toast.LENGTH_SHORT).show();
                                        }
                                        progressDialog.dismiss();
                                    }
                                });

                    }

                } else {
                    Toast.makeText(Profile.this, "Enter Valid Name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                civDp.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
