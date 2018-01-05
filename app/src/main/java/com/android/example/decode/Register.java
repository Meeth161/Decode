package com.android.example.decode;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    Toolbar toolbar;

    EditText etEmail;
    EditText etPassword;
    Button btnRegister;

    ProgressDialog progressDialog;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        toolbar = (Toolbar) findViewById(R.id.app_bar_register);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(Register.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        etEmail = (EditText) findViewById(R.id.editText_email);
        etPassword = (EditText) findViewById(R.id.editText_password);
        btnRegister = (Button) findViewById(R.id.button_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(etEmail.getText()) && etPassword.getText().toString().length() > 7) {
                    progressDialog.show();
                    mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(Register.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Register.this, "Could Not Create New Account, Please Try Later", Toast.LENGTH_SHORT).show();
                                    }

                                    progressDialog.dismiss();
                                }
                            });
                } else {
                    Toast.makeText(Register.this, "Please Enter Valid Email and Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
