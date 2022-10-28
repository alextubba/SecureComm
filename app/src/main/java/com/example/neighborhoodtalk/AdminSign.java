package com.example.neighborhoodtalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Hashtable;

public class AdminSign extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_signup);

        FirebaseApp.initializeApp(this);

        Button signup = findViewById(R.id.adminCreate);
        EditText schoolCode = findViewById(R.id.adminSchoolCode3);

        Intent intent = getIntent();
        String schoolId = intent.getStringExtra("schoolCode");

        schoolCode.setText(schoolId);

        signup.setOnClickListener(view -> {
            Log.d("signup", "starting");

            // check if student inputted the right access code for their school
            checkCode(schoolCode.getText().toString());
        });
    }

    private void checkCode(String code)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("schools/");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Log.d("login", password.getClass().getName() + " - " + dataSnapshot.getValue().toString().getClass().getName());
                if (!dataSnapshot.exists()) {
                    return;
                }

                HashMap Data = (HashMap) dataSnapshot.getValue();

                for (Object key : Data.keySet()) {
                    HashMap school = (HashMap) Data.get(key);
                    Log.d("signup", String.valueOf(String.valueOf(school.get("adminCode")).equals(code)));
                    if (String.valueOf(school.get("adminCode")).equals(code)) {
                        // allow signup for student because right access code
                        saveUser();
                        break;
                    } else {
                        // deny access
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("login", "getting data failed");
            }
        });
    }

    private void saveUser() {
        EditText usr = findViewById(R.id.adminUser);
        EditText phone = findViewById(R.id.adminPhone);
        EditText password = findViewById(R.id.adminPass);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users/" + usr.getText().toString());

        Hashtable<String, String> data = new Hashtable<String, String>();

        data.put("pass", password.getText().toString());
        data.put("phone", phone.getText().toString());
        data.put("admin", "true");
        Log.d("signup", String.valueOf(data));

        myRef.setValue(data);

        Intent nextPage = new Intent (this, SignIn.class);
        startActivity(nextPage);
    }
}