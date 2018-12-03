package com.example.currentplacedetailsonmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends AppCompatActivity {

    private String usernameOverall;


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
    private String username;

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
                username = extras.getString("username");
            } else {
                loggedIn = false;
            }
            if (extras.getString("lotType") != null) {
                spinner1.setSelection(lots.indexOf(extras.getString("lotType")));
                spinner2.setSelection(spots.indexOf(extras.getString("specialty")));
                spinner3.setSelection(prices.indexOf(extras.getString("priceRange")));
            }
        }

        usernameOverall = extras.getString("username");

        if (loggedIn) {
            DatabaseReference root = FirebaseDatabase.getInstance().getReference();
            DatabaseReference users = root.child("users");
            Log.i("Loggedin", usernameOverall);

            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.child(usernameOverall).child("lotTypeFilter").exists() &&
                            snapshot.child(usernameOverall).child("lotTypeFilter").getValue().toString() != "None") {
                        int spinner2Selection = Integer.parseInt(snapshot.child(usernameOverall).child("lotTypeFilter").getValue().toString());
                        spinner2.setSelection(spinner2Selection);
                    }

                    if(snapshot.child(usernameOverall).child("priceboundFilter").exists() &&
                            snapshot.child(usernameOverall).child("priceboundFilter").getValue().toString() != "None"){

                        int spinner1Selection = Integer.parseInt(snapshot.child(usernameOverall).child("priceboundFilter").getValue().toString());
                        spinner1.setSelection(spinner1Selection);
                    }

                    if(snapshot.child(usernameOverall).child("specialtyFilter").exists() &&
                            snapshot.child(usernameOverall).child("specialtyFilter").getValue().toString() != "None"){
                        int spinner3Selection = Integer.parseInt(snapshot.child(usernameOverall).child("specialtyFilter").getValue().toString());
                        spinner3.setSelection(spinner3Selection);
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lotType = String.valueOf(spinner1.getSelectedItem());
                specialty = String.valueOf(spinner2.getSelectedItem());
                priceRange = String.valueOf(spinner3.getSelectedItem());
                DatabaseReference root = FirebaseDatabase.getInstance().getReference();
                final DatabaseReference users = root.child("users");

                users.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int priceBoundFilter = spinner1.getSelectedItemPosition();
                        int lotTypeFilter = spinner2.getSelectedItemPosition();
                        int specialtyFilter = spinner3.getSelectedItemPosition();
                        Log.i("spin", Integer.toString(priceBoundFilter));
                        Log.i("spin", Integer.toString(lotTypeFilter));
                        Log.i("spin", Integer.toString(specialtyFilter));


                        users.child(username).child("lotTypeFilter").setValue(lotTypeFilter);
                        users.child(username).child("priceboundFilter").setValue(priceBoundFilter);
                        users.child(username).child("specialtyFilter").setValue(specialtyFilter);

//
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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
