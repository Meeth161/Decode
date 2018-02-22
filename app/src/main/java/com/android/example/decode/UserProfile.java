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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
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
    DatabaseReference friendRef;

    ProgressDialog progressDialog;

    FirebaseAuth mAuth;

    String mStatus = "not friends";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        uid = getIntent().getStringExtra("uid");

        mAuth = FirebaseAuth.getInstance();

        mRef = FirebaseDatabase.getInstance().getReference();
        userRef = mRef.child("users").child(uid);
        reqRef = mRef.child("friend_requests");
        friendRef = mRef.child("friends");

        ivDp = (CircleImageView) findViewById(R.id.circleImageView_userProfile);
        tvName = (TextView) findViewById(R.id.textView_name_userProfile);
        btnSendRequest = (Button) findViewById(R.id.button_sendFriendRequest);

        progressDialog = new ProgressDialog(UserProfile.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        statusCheck();

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
                if(mStatus.equals("not friends")) {
                    reqRef.child(mAuth.getCurrentUser().getUid()).child(uid).setValue("sent")
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UserProfile.this, "Could not send Friend Request", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    reqRef.child(uid).child(mAuth.getCurrentUser().getUid()).setValue("received")
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            })
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    statusCheck();
                                                }
                                            });
                                }
                            });
                } else if(mStatus.equals("sent")) {
                    
                    reqRef.child(mAuth.getCurrentUser().getUid()).child(uid).removeValue()
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    reqRef.child(uid).child(mAuth.getCurrentUser().getUid()).removeValue()
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    
                                                }
                                            })
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    statusCheck();
                                                }
                                            });
                                }
                            });
                    
                } else if(mStatus.equals("received")) {

                    friendRef.child(mAuth.getCurrentUser().getUid()).child(uid).setValue(ServerValue.TIMESTAMP)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    friendRef.child(uid).child(mAuth.getCurrentUser().getUid()).setValue(ServerValue.TIMESTAMP)
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            })
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    reqRef.child(mAuth.getCurrentUser().getUid()).child(uid).removeValue()
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {

                                                                }
                                                            })
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    reqRef.child(uid).child(mAuth.getCurrentUser().getUid()).removeValue()
                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {

                                                                                }
                                                                            })
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    statusCheck();
                                                                                }
                                                                            });
                                                                }
                                                            });

                                                }
                                            });
                                }
                            });

                }

            }
        });
    }

    private void statusCheck() {

        friendRef.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(uid)) {
                    mStatus = "friends";
                    btnSendRequest.setText("UnFriend");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        reqRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(uid)){
                    mStatus = dataSnapshot.child(uid).getValue().toString();
                    if(mStatus.equals("sent")) {
                        btnSendRequest.setText("cancel friend request");
                    } else if(mStatus.equals("received")) {
                        btnSendRequest.setText("accept friend request");
                    }
                }

                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
