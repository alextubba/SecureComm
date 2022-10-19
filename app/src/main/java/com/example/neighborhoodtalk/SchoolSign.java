package com.example.neighborhoodtalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Hashtable;

public class SchoolSign extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school_signup);

        TextView schoolName = findViewById(R.id.schoolName);
        TextView prim = findViewById(R.id.primaryNumber);
        TextView second = findViewById(R.id.secondaryNumber);
        Button register = findViewById(R.id.register);

        register.setOnClickListener(view -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("schools/" + schoolName.getText().toString());

            Hashtable<String, String> data = new Hashtable<String, String>();

            int studentCode = (int)(Math.random() * 100000);
            int adminCode = (int)(Math.random() * 100000);

            data.put("studentCode", String.valueOf(studentCode));
            data.put("adminCode", String.valueOf(adminCode));
            data.put("primary", prim.getText().toString());
            data.put("secondary", second.getText().toString());

            myRef.setValue(data);

            Intent nextPage = new Intent (this, SignIn.class);
            startActivity(nextPage);
        });
    }
}