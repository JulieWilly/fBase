package com.android.fbase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    private EditText userName, userEmail, userCourse, userImgUrl;
    private Button btnSave, btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        userName = (EditText) findViewById(R.id.userName);
        userCourse = (EditText) findViewById(R.id.userCourse);
        userEmail = (EditText) findViewById(R.id.userEmail);
        userImgUrl = (EditText) findViewById(R.id.userImgUrl);

        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
                clear();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void insertData() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", userName.getText().toString());
        map.put("course", userCourse.getText().toString());
        map.put("email", userEmail.getText().toString());
        map.put("sUrl",userImgUrl.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("Students").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddActivity.this, "Data inserted successfully.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddActivity.this, "Error while inserting data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void clear(){
        userName.setText("");
        userCourse.setText("");
        userEmail.setText("");
        userImgUrl.setText("");
    }
}