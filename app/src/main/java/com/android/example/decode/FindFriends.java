package com.android.example.decode;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FindFriends extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rvFindFriends;
    FindFriendsAdapter adapter;
    ArrayList<User> users = new ArrayList<>();

    DatabaseReference mRef;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        mRef = FirebaseDatabase.getInstance().getReference();

        toolbar = (Toolbar) findViewById(R.id.app_bar_findFriends);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Find Friends");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvFindFriends = (RecyclerView) findViewById(R.id.rv_findFriends);
        rvFindFriends.setLayoutManager(new LinearLayoutManager(FindFriends.this));
        adapter = new FindFriendsAdapter(users, FindFriends.this);
        rvFindFriends.setAdapter(adapter);

        progressDialog = new ProgressDialog(FindFriends.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        DatabaseReference userRef = mRef.child("users");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot s : dataSnapshot.getChildren()) {
                    users.add(s.getValue(User.class));
                }
                progressDialog.dismiss();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

    }
}
