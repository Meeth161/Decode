package com.android.example.decode;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    Button btnSave;
    FirebaseAuth mAuth;
    DatabaseReference mRef;

    CircleImageView civDp;
    EditText etName;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();

        civDp = (CircleImageView) findViewById(R.id.circleImageView_dp);
        etName = (EditText) findViewById(R.id.editText_name);

        progressDialog = new ProgressDialog(Profile.this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please Wait...");

        btnSave = (Button) findViewById(R.id.button_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(etName.getText())){
                    progressDialog.show();
                    DatabaseReference userRef = mRef.child("users").child(mAuth.getCurrentUser().getUid());
                    userRef.setValue(etName.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    progressDialog.dismiss();

                                    if(task.isSuccessful()) {
                                        Toast.makeText(Profile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Profile.this, "Profile Update Failed", Toast.LENGTH_SHORT).show();
                                    }

                                    startActivity(new Intent(Profile.this, MainActivity.class));
                                    finish();
                                }
                            });
                }
            }
        });
    }
}
