package com.example.currentplacedetailsonmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    private Button gotoFilter;
    private Button gotoLogin;
    private Button gotoMaps;

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

        setContentView(R.layout.activity_menu);

        gotoFilter = (Button) findViewById(R.id.gotoFilter);
        gotoLogin = (Button) findViewById(R.id.gotoLogin);
        gotoMaps = (Button) findViewById(R.id.gotoMaps);

        gotoFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilter();
            }
        });

        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });

        gotoMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }

    public void openLogin() {
        intent.setClass(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openFilter() {
        intent.setClass(this, FilterActivity.class);
        startActivity(intent);
    }

    public void goBack() {
        intent.setClass(this, MapsActivityCurrentPlace.class);
        startActivity(intent);
    }
}
