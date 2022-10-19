package com.example.neighborhoodtalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        FirebaseApp.initializeApp(this);

        Button signin = findViewById(R.id.login);
        signin.setOnClickListener(view -> {
            Log.d("login", "started");

            EditText email = findViewById(R.id.signEmail);
            EditText password = findViewById(R.id.signPassword);


            if (checkUsername(email.getText().toString(), password.getText().toString())) {

                Log.d("login", "ended");
                Intent nextPage = new Intent (this, mainPage.class);
                startActivity(nextPage);
            } else {
                Log.d("login", "login failed");
            }

        });

        Button signup = findViewById(R.id.signup);
        signup.setOnClickListener(view -> {
            Intent nextPage = new Intent (this, SignUp.class);
            startActivity(nextPage);

            Log.d("signup", "signup opened");
        });
    }

    public boolean correct;

    private boolean checkUsername(String usr, String password)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users/" + usr);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Log.d("login", password.getClass().getName() + " - " + dataSnapshot.getValue().toString().getClass().getName());
                if (!dataSnapshot.exists()) {
                    return;
                }

                HashMap Data = (HashMap) dataSnapshot.getValue();

                if (Data.get("pass").equals(password))
                {
                    correct = true;
                } else {
                    if (dataSnapshot.exists() & !(Data.get("pass").equals(password))) {
                        Log.d("login", (String) Data.get("pass"));
                        Log.d("login", "wrong password");
                    }
                    correct = false;
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("login", "getting data failed");
            }
        });

        return correct;
    }
}