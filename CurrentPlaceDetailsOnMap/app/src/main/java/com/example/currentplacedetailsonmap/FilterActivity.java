package com.example.currentplacedetailsonmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class FilterActivity extends AppCompatActivity {

    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;

    private Button confirm;

    public String lotType;
    public String specialty;
    public String priceRange;

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

        setContentView(R.layout.activity_filters);

        spinner1 = (Spinner) findViewById(R.id.lotTypeDropDown);
        spinner2 = (Spinner) findViewById(R.id.specialtyDropDown);
        spinner3 = (Spinner) findViewById(R.id.priceRangeDropDown);
        confirm = (Button) findViewById(R.id.confirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lotType = String.valueOf(spinner1.getSelectedItem());
                specialty = String.valueOf(spinner2.getSelectedItem());
                priceRange = String.valueOf(spinner3.getSelectedItem());
                submit();
            }
        });
    }

    public void submit() {
        intent.setClass(this, MenuActivity.class);
        intent.putExtra("lotType", lotType);
        intent.putExtra("specialty", specialty);
        intent.putExtra("priceRange", priceRange);
        startActivity(intent);
    }
}
