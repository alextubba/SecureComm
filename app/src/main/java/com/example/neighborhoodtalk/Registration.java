package com.example.neighborhoodtalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class Registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        ImageButton school = findViewById(R.id.registerSchool);
        ImageButton admin = findViewById(R.id.registerAdmin);


        school.setOnClickListener(view -> {
            Intent nextPage = new Intent (this, SchoolSign.class);
            startActivity(nextPage);
        });

        admin.setOnClickListener(view -> {
            Intent nextPage = new Intent (this, AdminSign.class);
            nextPage.putExtra("schoolCode", "");

            startActivity(nextPage);
        });
    }
}