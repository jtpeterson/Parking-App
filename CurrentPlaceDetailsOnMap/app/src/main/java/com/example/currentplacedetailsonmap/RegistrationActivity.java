package com.example.currentplacedetailsonmap;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistrationActivity extends AppCompatActivity {

    public boolean loggedIn;

    public Intent intent;

    public Button goBack;
    public Button registrationConfirm;

    EditText editUsername;
    EditText editPassword;
    EditText editConfirmPassword;
    EditText editEmail;

    private String username;

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
                username = extras.getString("username");
            } else {
                loggedIn = false;
            }
        }

        setContentView(R.layout.activity_registration);

        editUsername = (EditText) findViewById(R.id.registrationUsername);
        editPassword = (EditText) findViewById(R.id.registrationPassword);
        editConfirmPassword = (EditText) findViewById(R.id.registrationConfirmPassword);
        editEmail = (EditText) findViewById(R.id.registrationEmail);

        editPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if(!(editPassword.getText().toString().equals(editConfirmPassword.getText().toString()))){
                    editConfirmPassword.setError("Passwords Do Not Match");
                }
            }
        });

        editConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if(!(editPassword.getText().toString().equals(editConfirmPassword.getText().toString()))){
                    editConfirmPassword.setError("Passwords Do Not Match");
                }
            }
        });

        editEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                validateEmail();
            }
        });

        registrationConfirm = (Button) findViewById(R.id.registrationConfirm);

        registrationConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editPassword.getText().toString().equals(editConfirmPassword.getText().toString())) {
                    String username = editUsername.getText().toString();
                    String password = editPassword.getText().toString();
                    String email = editEmail.getText().toString();
                    register(username, password, email);
                }else{
                    final AlertDialog alert = new AlertDialog.Builder(RegistrationActivity.this).create();
                    alert.setTitle("Registration Failed");
                    alert.setMessage("Check fields for errors.");
                    alert.setButton(Dialog.BUTTON_POSITIVE,"OK",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alert.dismiss();
                        }
                    });
                    alert.show();
                }

            }
        });

        goBack = (Button) findViewById(R.id.goBack);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
    }

    private void register(final String username, final String password, final String email){
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference users = root.child("users");
        users.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot snapshot) {
            if (snapshot.child(username).exists()) {
                final AlertDialog alert = new AlertDialog.Builder(RegistrationActivity.this).create();
                alert.setTitle("Registration Failed");
                alert.setMessage("This username is already taken");
                alert.setButton(Dialog.BUTTON_POSITIVE,"OK",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alert.dismiss();
                    }
                });
                alert.show();
                } else{
                    users.child(username).setValue(username);
                    users.child(username).child("username").setValue(username);
                    users.child(username).child("password").setValue(password);
                    users.child(username).child("email").setValue(email);
                    loggedIn = true;
                    login();

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
    }

    private void validateEmail(){
        String emailInput = editEmail.getText().toString().trim();

        if (emailInput.isEmpty()) {
            editEmail.setError("Field can't be empty");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            editEmail.setError("Please enter a valid email address");
        } else {
            editEmail.setError(null);
        }
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
        startActivity(i);
    }
}
