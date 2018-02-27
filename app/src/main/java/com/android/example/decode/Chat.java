package com.android.example.decode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Chat extends AppCompatActivity {

    Toolbar toolbar;

    String uid;
    String name;

    EditText etSendMessage;
    ImageView btnSendMessage;

    DatabaseReference mRef;
    FirebaseAuth mAuth;

    ArrayList<Message> messageList = new ArrayList<>();
    MessageAdapter adapter;

    RecyclerView rvMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        name = getIntent().getStringExtra("name");
        uid = getIntent().getStringExtra("uid");

        toolbar = (Toolbar) findViewById(R.id.app_bar_register);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();

        rvMessages = (RecyclerView) findViewById(R.id.rv_messages);
        rvMessages.setLayoutManager(new LinearLayoutManager(Chat.this));
        adapter = new MessageAdapter(Chat.this, messageList);
        rvMessages.setAdapter(adapter);

        DatabaseReference messageRef = mRef.child("messages").child(mAuth.getCurrentUser().getUid()).child(uid);
        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageList.clear();
                for(DataSnapshot d : dataSnapshot.getChildren()) {
                    messageList.add(d.getValue(Message.class));
                }
                adapter.notifyDataSetChanged();
                rvMessages.smoothScrollToPosition(rvMessages.getAdapter().getItemCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        etSendMessage = (EditText) findViewById(R.id.editText_send_message);
        btnSendMessage = (ImageView) findViewById(R.id.button_send_message);
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!TextUtils.isEmpty(etSendMessage.getText())) {

                    final Message m = new Message( ServerValue.TIMESTAMP.toString(), etSendMessage.getText().toString().trim(), mAuth.getCurrentUser().getUid());

                    final DatabaseReference messageRef = mRef.child("messages").child(mAuth.getCurrentUser().getUid()).child(uid);
                    messageRef.push().setValue(m)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    DatabaseReference messageRef = mRef.child("messages").child(uid).child(mAuth.getCurrentUser().getUid());
                                    messageRef.push().setValue(m);
                                    etSendMessage.setText("");
                                }
                            });

                }

            }
        });

    }
}
