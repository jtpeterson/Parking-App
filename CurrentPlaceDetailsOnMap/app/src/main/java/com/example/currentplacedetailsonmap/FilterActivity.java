package com.example.currentplacedetailsonmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends AppCompatActivity {

    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;

    private Button confirm;

    public String lotType;
    public String specialty;
    public String priceRange;

    public List<String> prices;
    public List<String> lots;
    public List<String> spots;

    public boolean loggedIn;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = getIntent();

        setContentView(R.layout.activity_filters);

        setStrings();

        spinner1 = (Spinner) findViewById(R.id.lotTypeDropDown);
        spinner2 = (Spinner) findViewById(R.id.specialtyDropDown);
        spinner3 = (Spinner) findViewById(R.id.priceRangeDropDown);
        confirm = (Button) findViewById(R.id.confirm);

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
            if (extras.getString("lotType") != null) {
                spinner1.setSelection(lots.indexOf(extras.getString("lotType")));
                spinner2.setSelection(spots.indexOf(extras.getString("specialty")));
                spinner3.setSelection(prices.indexOf(extras.getString("priceRange")));
            }
        }

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
        intent = getIntent();
        Bundle extras = getIntent().getExtras();
        Intent i = new Intent(this, MenuActivity.class);
        i.putExtras(extras);
        i.putExtra("lotType", lotType);
        i.putExtra("specialty", specialty);
        i.putExtra("priceRange", priceRange);
        startActivity(i);
    }

    public void setStrings() {
        prices = new ArrayList<>();
        prices.add("Pick One");
        prices.add("$0.00 - $5.00 per hour");
        prices.add("$0.00 - $10.00 per hour");
        prices.add("$0.00 - $20.00 per hour");
        prices.add("All");

        lots = new ArrayList<>();
        lots.add("Pick One");
        lots.add("Grass");
        lots.add("Underground");
        lots.add("Garage/Deck");
        lots.add("Street");

        spots = new ArrayList<>();
        spots.add("Pick One");
        spots.add("Electric");
        spots.add("Handicap");
        spots.add("Motorcycle");
        spots.add("Bus");
    }
}
