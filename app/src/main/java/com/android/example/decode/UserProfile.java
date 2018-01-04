package com.android.example.decode;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    String uid;

    CircleImageView ivDp;
    TextView tvName;
    Button btnSendRequest;

    DatabaseReference mRef;
    DatabaseReference userRef;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        uid = getIntent().getStringExtra("uid");

        mRef = FirebaseDatabase.getInstance().getReference();
        userRef = mRef.child("users").child(uid);

        ivDp = (CircleImageView) findViewById(R.id.circleImageView_userProfile);
        tvName = (TextView) findViewById(R.id.textView_name_userProfile);
        btnSendRequest = (Button) findViewById(R.id.button_sendFriendRequest);

        progressDialog = new ProgressDialog(UserProfile.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                tvName.setText(u.getName());
                Glide.with(UserProfile.this).load(u.getDpUrl()).into(ivDp);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }
}
