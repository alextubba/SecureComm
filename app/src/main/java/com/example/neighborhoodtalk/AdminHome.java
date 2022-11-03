package com.example.neighborhoodtalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AdminHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home);

        Intent intent = getIntent();
        String schoolId = intent.getStringExtra("school");

        getCodes(schoolId);
        checkForReports(schoolId);

    }

    private void getCodes(String schoolId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("schools/" + schoolId);

        TextView studentCode = findViewById(R.id.studentCode);
        TextView adminCode = findViewById(R.id.adminCode);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Log.d("login", password.getClass().getName() + " - " + dataSnapshot.getValue().toString().getClass().getName());
                if (!dataSnapshot.exists()) {
                    return;
                }
                Log.d("login", "got data");
                HashMap Data = (HashMap) dataSnapshot.getValue();

                // change code text to correct codes
                studentCode.setText("Student Code: " + Data.get("studentCode"));
                adminCode.setText("Admin Code: " + Data.get("adminCode"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("login", "getting data failed");
            }
        });
    }

    private void checkForReports(String schoolId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("reports/");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Log.d("login", password.getClass().getName() + " - " + dataSnapshot.getValue().toString().getClass().getName());
                if (!dataSnapshot.exists()) {
                    return;
                }

                HashMap Data = (HashMap) dataSnapshot.getValue();

                final LinearLayout linearLayout = findViewById(R.id.reportLayout);
                linearLayout.removeAllViews();

                for (Object key : Data.keySet()) {
                    if (key.equals("baseData")) {
                        continue;
                    }

                    HashMap report = (HashMap) Data.get(key);

                    final Button text = new Button(AdminHome.this);
                    text.setText((CharSequence) report.get("title"));
                    text.setTextSize(18);
                    linearLayout.addView(text);

                    text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent nextPage = new Intent (AdminHome.this, reportPage.class);
                            nextPage.putExtra("Title", (String) report.get("title"));
                            nextPage.putExtra("Desc", (String) report.get("desc"));
                            nextPage.putExtra("Threat", (String) report.get("threat"));
                            nextPage.putExtra("Name", (String) report.get("name"));
                            nextPage.putExtra("School", (String) schoolId);
                            nextPage.putExtra("Report", (String) key);

                            startActivity(nextPage);
                        }
                    });
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("login", "getting data failed");
            }
        });
    }
}