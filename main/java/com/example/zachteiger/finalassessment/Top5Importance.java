package com.example.zachteiger.finalassessment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Top5Importance extends Activity implements View.OnClickListener {

    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    private ListView listViewTopFive;
    private Button buttonTopFive;
    private EditText editTextZipCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top5importance);

        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Bird");
        mAuth = FirebaseAuth.getInstance();

        listViewTopFive = findViewById(R.id.listViewTopFive);

        buttonTopFive = findViewById(R.id.buttonTopFive);
        buttonTopFive.setOnClickListener(this);

        editTextZipCode = findViewById(R.id.editTextZipCode);



    }



    @Override
    public void onClick(View v) {

        if (v == buttonTopFive){

            final Query myRef = mRef.child(editTextZipCode.getText().toString()).orderByChild("birdImportance").limitToLast(5);
            myRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Bird b = dataSnapshot.getValue(Bird.class);

                    // listViewTopFive.setAdapter();

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }

    }
}

