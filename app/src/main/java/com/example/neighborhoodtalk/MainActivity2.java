package com.example.neighborhoodtalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        FirebaseApp.initializeApp(this);

        Button signup = findViewById(R.id.signupButton);

        signup.setOnClickListener(view -> {
            Log.d("signup", "starting");

            EditText usr = findViewById(R.id.createUsername);
            EditText phone = findViewById(R.id.createPhone);
            EditText password = findViewById(R.id.createPassword);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("users/" + usr.getText().toString());

            myRef.setValue(password.getText().toString());

            Intent nextPage = new Intent (this, MainActivity.class);
            startActivity(nextPage);
        });
    }
}