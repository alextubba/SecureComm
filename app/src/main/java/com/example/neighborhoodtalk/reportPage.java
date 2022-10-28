package com.example.neighborhoodtalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class reportPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_page);

        Intent intent = getIntent();

        String title = intent.getStringExtra("Title");
        String desc = intent.getStringExtra("Desc");
        String threat = intent.getStringExtra("Threat");
        String report = intent.getStringExtra("Report");
        String school = intent.getStringExtra("School");
        String name = intent.getStringExtra("Name");

        TextView titleText = findViewById(R.id.titleText);
        TextView descText = findViewById(R.id.descText);
        TextView importText = findViewById(R.id.importText);
        TextView nameText = findViewById(R.id.nameText);
        Button remove = findViewById(R.id.removeButton5);

        titleText.setText(title);
        descText.setText(desc);
        nameText.setText("Sent By: " + name);

        String info = "Minor";

        if (threat.equals("yellow")) {
            info = "Semi-Important";
        } else if (threat.equals("red")) {
            info = "Important!";
        }

        importText.setText(info);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("reports/" + report);

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Log.d("login", password.getClass().getName() + " - " + dataSnapshot.getValue().toString().getClass().getName());
                        if (!dataSnapshot.exists()) {
                            return;
                        }

                        dataSnapshot.getRef().removeValue();

                        Intent nextPage = new Intent (reportPage.this, AdminHome.class);
                        nextPage.putExtra("school", school);
                        startActivity(nextPage);
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}