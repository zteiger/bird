package com.example.zachteiger.finalassessment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class Delete extends Activity implements View.OnClickListener {

    public FirebaseDatabase database;
    public FirebaseAuth mAuth;
    public DatabaseReference myRef;

    private Button buttonDeleteBirdSighting;
    private EditText editTextBirdName, editTextBirdZipCode;

    private Bird currentBird;
    private String currentBirdID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletebird);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        myRef = database.getReference("Bird");

        buttonDeleteBirdSighting = findViewById(R.id.buttonDeleteBirdSighting);
        buttonDeleteBirdSighting.setOnClickListener(this);


        editTextBirdName = findViewById(R.id.editTextBirdName);
        editTextBirdZipCode = findViewById(R.id.editTextBirdZipCode);

    }


    public void setCurrentBird(Bird newBird, String birdID){
        currentBird = newBird;
        currentBirdID = birdID;

    }



    // 2. iterate through all of my birds and do a for loop for each one
    // get to the next level by doing .getChildren


    private void checkForBid(final String birdName, String birdZip){            //define the method to find the bird name. when we click the button, we will call this method and give it the nbame of the bird we're looking for

        myRef.child(birdZip).addListenerForSingleValueEvent(new ValueEventListener() {          // adding a listener to start at the root of our database "Birds"
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setCurrentBird(null, "");                           // clears out the value of Current Bird
                    for (DataSnapshot sighting : dataSnapshot.getChildren()){                // second for loop. get children of the zipcodes (actual sightings)
                        Bird b = sighting.getValue(Bird.class);                 // store the entire class in a variable so that we can call upon it later on\

                        if (editTextBirdName.getText().toString().equals(b.birdName)){              // check if the names match a name in the dataase
                                                    // makes sure that the current bird isn't null, moves on to new bird if it is. Then check to see if dates are chronological

                                setCurrentBird(b, sighting.getKey());               //              store the key for the current bird

                            myRef.child(b.birdZip).child(sighting.getKey()).removeValue();
                            }
                        }

                    }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    public void onClick(View v) {

        if (v == buttonDeleteBirdSighting) {

            checkForBid(editTextBirdName.getText().toString(), editTextBirdZipCode.getText().toString());




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
                startActivity(new Intent(Delete.this, MainActivity.class));
                return true;
            case R.id.home_activity_menu_item:
                startActivity(new Intent(Delete.this, Home.class));
                return true;
            case R.id.registerbird_activity_menu_item:
                startActivity(new Intent(Delete.this, ReportBird.class));
                return true;
            case R.id.searchbird_activity_menu_item:
                startActivity(new Intent(Delete.this, SearchBird.class));
                return true;
            case R.id.activity_findbird_menu:
                startActivity(new Intent(Delete.this, FindBird.class));
                return true;
            case R.id.activity_deletebird_menu:
                startActivity(new Intent(Delete.this, Delete.class));
                return true;

            default:
                return false;
        }
    }
}



            // first check if the bird siting (most recent siting per zipcode) and that would be checked via zip code

            /*final DatabaseReference mRef = myRef.child(rZipCode.getText().toString());
            Bird newBird = new Bird(rZipCode.getText().toString().toUpperCase(),
                    rBirdName.getText().toString().toUpperCase(),
                    new Date(TextUtils.join("/", showMyDate.getText().toString().split("-"))).getTime(),
                    BirdImportance = 0, rUserName.getText().toString());

            mRef.push().setValue(newBird);

            Toast.makeText(ReportBird.this, "Bird Reported!", Toast.LENGTH_SHORT).show();

            rBirdName.setText("");
            showMyDate.setText("");
            rZipCode.setText(""); */


