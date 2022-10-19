package com.example.neighborhoodtalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Hashtable;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        FirebaseApp.initializeApp(this);

        Button signup = findViewById(R.id.signupButton);
        ImageButton admin = findViewById(R.id.openAdmin);

        signup.setOnClickListener(view -> {
            Log.d("signup", "starting");

            EditText usr = findViewById(R.id.createUsername);
            EditText email = findViewById(R.id.createPhone);
            EditText password = findViewById(R.id.createPassword);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("users/" + usr.getText().toString());

            Hashtable<String, String> data = new Hashtable<String, String>();

            data.put("pass", password.getText().toString());
            data.put("email", email.getText().toString());
            Log.d("signup", String.valueOf(data));

            Intent nextPage = new Intent (this, SignIn.class);
            startActivity(nextPage);
        });

        admin.setOnClickListener(view -> {
            Intent nextPage = new Intent (this, Registration.class);
            startActivity(nextPage);
        });
    }
}