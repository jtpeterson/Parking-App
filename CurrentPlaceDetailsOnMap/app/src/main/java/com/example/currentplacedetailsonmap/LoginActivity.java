package com.example.currentplacedetailsonmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    protected String username;
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

        // Missing logic for logging in. At the moment just logs in automatically
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loggedIn = true;
                login();
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
        Intent i = new Intent(this, MenuActivity.class);
        i.putExtras(extras);
        i.putExtra("loggedIn", loggedIn);
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
