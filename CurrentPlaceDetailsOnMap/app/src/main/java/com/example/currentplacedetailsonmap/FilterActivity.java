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
    private EditText mEdit;
    private Button confirm;
    public String lotType;
    public String specialty;
    public String upperBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_filters);

        spinner1 = (Spinner) findViewById(R.id.lotTypeDropDown);
        spinner2 = (Spinner) findViewById(R.id.specialtyDropDown);
        mEdit = (EditText) findViewById(R.id.upperBound);
        confirm = (Button) findViewById(R.id.confirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lotType = String.valueOf(spinner1);
                specialty = String.valueOf(spinner2);
                upperBound = String.valueOf(mEdit);
                submit();
            }
        });
    }

    public void submit() {
        Intent intent = new Intent(this, MapsActivityCurrentPlace.class);
        intent.putExtra("lotType", lotType);
        intent.putExtra("specialty", specialty);
        intent.putExtra("upperBound", upperBound);
        startActivity(intent);
    }
}
