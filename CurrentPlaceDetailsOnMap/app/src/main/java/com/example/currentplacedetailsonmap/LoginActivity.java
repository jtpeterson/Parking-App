package com.example.currentplacedetailsonmap;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.util.Log;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class LoginActivity extends AppCompatActivity {
    protected String usernameOverall;
    protected String password;


    public Button cancel;
    public Button confirm;
    public Button gotoRegistration;



    public boolean loggedIn;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = getIntent();

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            loggedIn = false;
            intent.putExtra("loggedIn", loggedIn);
        } else {
            if (extras.getBoolean("loggedIn")) {
                loggedIn = true;
            } else {
                loggedIn = false;
            }
        }

        setContentView(R.layout.activity_login);

        cancel = (Button) findViewById(R.id.cancel);
        confirm = (Button) findViewById(R.id.confirm);
        gotoRegistration = (Button) findViewById(R.id.gotoRegistration);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editUsername = (EditText) findViewById(R.id.editUser);
                String username = editUsername.getText().toString();

                EditText editPassword = (EditText) findViewById(R.id.editPass);
                String password = editPassword.getText().toString();

                authenticate(username, password);
            }
        });

        gotoRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRegistration();
            }
        });
    }

    public void authenticate(final String username, final String password){
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference users = root.child("users");
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child(username).exists()) {
                    String fireBasePass = snapshot.child(username).child("password").getValue(String.class);
                    if (password.equals(fireBasePass)) {
                        loggedIn = true;
                        usernameOverall = username;
                        login();

                    }else{
                        final AlertDialog alert = new AlertDialog.Builder(LoginActivity.this).create();
                        alert.setTitle("Authentication Failed");
                        alert.setMessage("Incorrect username or password");
                        alert.setButton(Dialog.BUTTON_POSITIVE,"OK",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alert.dismiss();
                            }
                        });
                        alert.show();
                    }
                }else{
                    final AlertDialog alert = new AlertDialog.Builder(LoginActivity.this).create();
                    alert.setTitle("Authentication Failed");
                    alert.setMessage("Incorrect username or password");
                    alert.setButton(Dialog.BUTTON_POSITIVE,"OK",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alert.dismiss();
                        }
                    });
                    alert.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void goBack() {
        intent = getIntent();
        Bundle extras = intent.getExtras();
        Intent i = new Intent(this, MenuActivity.class);
        i.putExtras(extras);
        startActivity(i);
    }

    public void login() {
        intent = getIntent();
        Bundle extras = intent.getExtras();
        Intent i = new Intent(this, MapsActivityCurrentPlace.class);
        i.putExtras(extras);
        i.putExtra("loggedIn", loggedIn);
        i.putExtra("username", usernameOverall);
        startActivity(i);
    }

    public void gotoRegistration() {
        intent = getIntent();
        Bundle extras = intent.getExtras();
        Intent i = new Intent(this, RegistrationActivity.class);
        i.putExtras(extras);
        startActivity(i);
    }
}
