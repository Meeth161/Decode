package com.android.example.decode;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    String uid;

    CircleImageView ivDp;
    TextView tvName;
    Button btnSendRequest;

    DatabaseReference mRef;
    DatabaseReference userRef;
    DatabaseReference reqRef;

    ProgressDialog progressDialog;

    FirebaseAuth mAuth;

    String status = "send friend request";

    Request r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        uid = getIntent().getStringExtra("uid");

        mAuth = FirebaseAuth.getInstance();

        mRef = FirebaseDatabase.getInstance().getReference();
        userRef = mRef.child("users").child(uid);
        reqRef = mRef.child("friend_requests");

        ivDp = (CircleImageView) findViewById(R.id.circleImageView_userProfile);
        tvName = (TextView) findViewById(R.id.textView_name_userProfile);
        btnSendRequest = (Button) findViewById(R.id.button_sendFriendRequest);

        statusCheck();

        progressDialog = new ProgressDialog(UserProfile.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                tvName.setText(u.getName());
                Glide.with(UserProfile.this).setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.user)).load(u.getDpUrl()).into(ivDp);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals("send friend request")) {
                    status = "requested";
                    String key = reqRef.push().getKey();
                    reqRef.child(key).setValue(new Request(mAuth.getCurrentUser().getUid(), uid, status, key))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        statusCheck();
                                    }
                                }
                            });
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this)
                            .setMessage("Delete Request ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    reqRef.child(r.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                status = "send friend request";
                                                btnSendRequest.setText(status);
                                            }
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    AlertDialog ad = builder.create();
                    ad.show();
                }
            }
        });
    }

    private void statusCheck() {
        Query query = reqRef.orderByChild("senderUid").equalTo(mAuth.getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()) {
                    r = d.getValue(Request.class);
                    if(r.getReceiverUid().equals(uid)) {
                        status = r.getStatus();
                        btnSendRequest.setText(status);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
