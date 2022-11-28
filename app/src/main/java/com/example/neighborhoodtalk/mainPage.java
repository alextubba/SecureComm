package com.example.neighborhoodtalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class mainPage extends AppCompatActivity {
    private String url = "http://127.0.0.1:5000/";
    private String postBodyString;
    private MediaType mediaType;
    private RequestBody requestBody;
    private Button connect;


    private RequestBody buildRequestBody(String msg) {
        postBodyString = msg;
        mediaType = MediaType.parse("text/plain");
        requestBody = RequestBody.create(postBodyString, mediaType);
        return requestBody;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_homepage);

        Button report = findViewById(R.id.reportButton);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        Spinner dropdown = findViewById(R.id.titleInput);
        String[] items = new String[]{"Violence", "Substance Abuse", "Behavior", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        report.setOnClickListener(view -> {
            Log.d("sendingData", "creating");

            getReportNum(name);
        });
    }

    private void sendToServer() {
        TextView title = findViewById(R.id.textView2);

        RequestBody requestBody = buildRequestBody("new Message");
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request
                .Builder()
                .post(requestBody)
                .url("http://alexculp.pythonanywhere.com/")
                .build();

        Log.d("sendingData", "sent");

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // make an error screen
                        Log.d("sendingData", "cannot send");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d("data", response.body().string());
                            title.setText(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void saveReport(Integer amount, String name) {
        Spinner title = findViewById(R.id.titleInput);
        TextView desc = findViewById(R.id.Description);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("reports/report" + amount);

        Hashtable<String, String> data = new Hashtable<String, String>();

        String importance = "green";
        RadioButton green = findViewById(R.id.green);
        RadioButton yellow = findViewById(R.id.yellow);
        RadioButton red = findViewById(R.id.red);

        if (green.isChecked()) {
            importance = "green";
        } else if (yellow.isChecked()) {
            importance = "yellow";
        } else if (red.isChecked()) {
            importance = "red";
        }

        data.put("title", title.getSelectedItem().toString());
        data.put("desc", desc.getText().toString());
        data.put("threat", importance);
        data.put("name", name);
        Log.d("signup", String.valueOf(data));

        myRef.setValue(data);

        Intent nextPage = new Intent (this, mainPage.class);
        startActivity(nextPage);
    }

    public Integer Amount = 0;
    public boolean debounce = false;

    public void getReportNum(String name) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("reports/");
        Amount = 1;

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Log.d("login", password.getClass().getName() + " - " + dataSnapshot.getValue().toString().getClass().getName());
                if (!dataSnapshot.exists()) {
                    return;
                }

                if (debounce == true) {
                    return;
                }

                HashMap Data = (HashMap) dataSnapshot.getValue();

                for (Object key : Data.keySet()) {
                    Amount += 1;

                }

                saveReport(Amount, name);
                debounce = false;
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("login", "getting data failed");
            }
        });
    };
}