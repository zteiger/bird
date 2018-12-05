package com.example.zachteiger.finalassessment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchBird extends Activity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mRef;

    private Button buttonAddImp, buttonSubImp, buttonSearchBird;
    private EditText sZipCode;
    private TextView sBirdName, sBirdImportance, sUserName, sBirdDate;

    private Bird currentBird;
    private String currentBirdID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbird);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Bird");

        currentBird = null;


        buttonAddImp = findViewById(R.id.buttonAddImp);
        buttonAddImp.setOnClickListener(this);

        buttonSearchBird = findViewById(R.id.buttonSearchBird);
        buttonSearchBird.setOnClickListener(this);

        buttonSubImp = findViewById(R.id.buttonSubImp);
        buttonSubImp.setOnClickListener(this);

        sZipCode = findViewById(R.id.sZipCode);
        sBirdImportance = findViewById(R.id.sBirdImportance);
        sBirdName = findViewById(R.id.sBirdName);
        sUserName = findViewById(R.id.sBirdViewer);
        sBirdDate = findViewById(R.id.sBirdDate);

    }

    public void setCurrentBird(Bird newBird, String birdID){
        currentBird = newBird;
        currentBirdID = birdID;

    }


    @Override
    public void onClick(View v) {

        if (v == buttonSearchBird) {

            if (!sZipCode.getText().toString().equals("")) {

                final Query myRef = mRef.child(sZipCode.getText().toString()).orderByChild("birdDate").limitToLast(1);
                myRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Bird b = dataSnapshot.getValue(Bird.class);


                        sBirdImportance.setText(b.birdImportance + "");
                        sBirdName.setText(b.birdName);
                        sUserName.setText(b.userName);
                        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
                        sBirdDate.setText(df.format(new Date(b.birdDate)));


                        Toast.makeText(SearchBird.this, b.birdName, Toast.LENGTH_SHORT).show();


                        setCurrentBird(b, dataSnapshot.getKey());
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

            else {

                Toast.makeText(SearchBird.this, "Please Enter a Zip Code", Toast.LENGTH_SHORT).show();

            }

        }


        if (v == buttonAddImp){

            currentBird.birdImportance ++;

            //make sure someone can only add one importance
            //maybe something like a unique user ID

            //WRITE THE CODE BACK TO THE DATABASE (use similar code as the Report Bird)
            String ZipCode = sZipCode.getText().toString();
            final DatabaseReference myRef = mRef.child(sZipCode.getText().toString()).child(currentBirdID);

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        Bird updatedBird = dataSnapshot.getValue(Bird.class);
                        updatedBird.birdImportance = Integer.parseInt(sBirdImportance.getText().toString());

                        myRef.setValue(updatedBird);

                        sBirdName.setText("");
                        sBirdDate.setText("");
                        sZipCode.setText("");
                        sBirdImportance.setText("");
                        sUserName.setText("");

                        Toast.makeText(SearchBird.this, "All members only allowed one Vote", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            //Update the UI

            sBirdImportance.setText(currentBird.birdImportance + "");
        }

        // Subtract importance is the same thing as add just with currentBird.birdImportance --;

        if (v == buttonSubImp){

            currentBird.birdImportance --;

            String ZipCode = sZipCode.getText().toString();
            final DatabaseReference myRef = mRef.child(sZipCode.getText().toString()).child(currentBirdID);

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        Bird updatedBird = dataSnapshot.getValue(Bird.class);
                        updatedBird.birdImportance = Integer.parseInt(sBirdImportance.getText().toString());


                        myRef.setValue(updatedBird);

                        sBirdName.setText("");
                        sBirdDate.setText("");
                        sZipCode.setText("");
                        sBirdImportance.setText("");
                        sUserName.setText("");

                        Toast.makeText(SearchBird.this, "All Members only allowed one vote", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            //Update the UI

            sBirdImportance.setText(currentBird.birdImportance + "");


        }


    }







        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            MenuInflater optionsMenuInflater = getMenuInflater();
            optionsMenuInflater.inflate(R.menu.dropdown_menu, menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override public boolean onMenuItemSelected ( int featureId, MenuItem item){
            switch (item.getItemId()) {
                case R.id.main_activity_menu_item:
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(SearchBird.this, MainActivity.class));
                    return true;
                case R.id.home_activity_menu_item:
                    startActivity(new Intent(SearchBird.this, Home.class));
                    return true;
                case R.id.registerbird_activity_menu_item:
                    startActivity(new Intent(SearchBird.this, ReportBird.class));
                    return true;
                case R.id.searchbird_activity_menu_item:
                    startActivity(new Intent(SearchBird.this, SearchBird.class));
                    return true;
                case R.id.activity_findbird_menu:
                    startActivity(new Intent(SearchBird.this, FindBird.class));
                    return true;
                case R.id.activity_deletebird_menu:
                    startActivity(new Intent(SearchBird.this, Delete.class));
                    return true;

                default:
                    return false;
            }
        }

}