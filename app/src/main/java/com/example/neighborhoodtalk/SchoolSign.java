package com.example.neighborhoodtalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
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
            int schoolNum = getSchoolNum();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("schools/school" + String.valueOf(schoolNum));

            Hashtable<String, String> data = new Hashtable<String, String>();

            int studentCode = (int)(Math.random() * 100000);
            int adminCode = (int)(Math.random() * 100000);

            data.put("studentCode", String.valueOf(studentCode));
            data.put("adminCode", String.valueOf(adminCode));
            data.put("primary", prim.getText().toString());
            data.put("secondary", second.getText().toString());
            data.put("name", schoolName.getText().toString());

            myRef.setValue(data);

            Intent nextPage = new Intent (this, AdminSign.class);
            startActivity(nextPage);
        });
    }

    public Integer Amount = 0;

    public Integer getSchoolNum() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("schools/");
        Amount = 1;

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Log.d("login", password.getClass().getName() + " - " + dataSnapshot.getValue().toString().getClass().getName());
                if (!dataSnapshot.exists()) {
                    return;
                }

                HashMap Data = (HashMap) dataSnapshot.getValue();

                for (Object key : Data.keySet()) {
                    Amount += 1;
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("login", "getting data failed");
            }
        });

        return Amount;
    };
}