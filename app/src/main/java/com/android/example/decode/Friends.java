package com.android.example.decode;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Friends extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mRef;

    RecyclerView rvFriends;
    FriendsAdapter adapter;

    ArrayList<User> users = new ArrayList<>();

    ProgressDialog progressDialog;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        toolbar = (Toolbar) findViewById(R.id.app_bar_register);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Friends");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(Friends.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        rvFriends = (RecyclerView) findViewById(R.id.rv_friends);
        adapter = new FriendsAdapter(Friends.this, users);
        rvFriends.setLayoutManager(new LinearLayoutManager(Friends.this));
        rvFriends.setAdapter(adapter);

        DatabaseReference friendsRef = mRef.child("friends").child(mAuth.getCurrentUser().getUid());
        friendsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    DatabaseReference fRef = mRef.child("users").child(d.getKey());
                    fRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            users.add(dataSnapshot.getValue(User.class));
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
