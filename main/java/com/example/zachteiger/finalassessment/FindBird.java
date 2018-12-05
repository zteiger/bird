package com.example.zachteiger.finalassessment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FindBird extends Activity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mRef;


    private Button buttonFindBirdSighting;
    private TextView textViewBirdSighting;
    private EditText editTextFindBird;

    // These are my global variables
    private Bird currentBird;
    private String currentBirdID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findbird);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Bird");

        buttonFindBirdSighting = findViewById(R.id.buttonFindBirdSighting);
        buttonFindBirdSighting.setOnClickListener(this);

        textViewBirdSighting = findViewById(R.id.textViewBirdSighting);
        editTextFindBird = findViewById(R.id.editTextFindBird);

    }


    // 1. create a global variable that can hold the most recent bird so far by that bird name

    // This is the method that sets these values
    public void setCurrentBird(Bird newBird, String birdID){
        currentBird = newBird;
        currentBirdID = birdID;

    }


    // 2. iterate through all of my birds and do a for loop for each one
    // get to the next level by doing .getChildren


    private void checkForBid(final String birdName){            //define the method to find the bird name. when we click the button, we will call this method and give it the nbame of the bird we're looking for

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {          // adding a listener to start at the root of our database "Birds"
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setCurrentBird(null, "");                           // clears out the value of Current Bird
                for (DataSnapshot zip : dataSnapshot.getChildren()){                //   first for loop. go into the zip level of the firebase tree
                    for (DataSnapshot sighting : zip.getChildren()){                // second for loop. get children of the zipcodes (actual sightings)
                        Bird b = sighting.getValue(Bird.class);                 // store the entire class in a variable so that we can call upon it later on\

                        if (editTextFindBird.getText().toString().equals(b.birdName)){              // check if the names match a name in the dataase
                            if (currentBird == null  || currentBird.birdDate < b.birdDate){                         // makes sure that the current bird isn't null, moves on to new bird if it is. Then check to see if dates are chronological

                                setCurrentBird(b, sighting.getKey());               //              store the key for the current bird

                            }
                        }

                    }
                } if (currentBird != null){

                    textViewBirdSighting.setText(currentBird.birdZip);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


//

    // 3. Inside the for loops, when we get to a bird, if the name is the name of the bird we're searching for, and the date is more recent than the currently stored bird,
    // then replace the currently stored bird with the current bird
    // 4. At the end of the nested for loops, pull the zip code


    /* private void checkForFriend(final String BirdSighting){
        final Boolean[] sawBird = {false};
        DatabaseReference rootRef = database.getReference().child("Bird");

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot us : dataSnapshot.getChildren()){
                    final String key = us.getKey();


                    final DatabaseReference BirdSighting = mRef.child(editTextFindBird.getText().toString());
                    BirdSighting.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot bird : dataSnapshot.getChildren()){
                                String userName = bird.child("userName").getValue(String.class);
                                if (BirdSighting.equals(userName)){
                                    sawBird[0] = true;

                                    Bird b = dataSnapshot.getValue(Bird.class);

                                    editTextFindBird.setText(b.userName);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(FindBird.this, "Error", Toast.LENGTH_SHORT).show();


                        }
                    }); //nested loop 2 end

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FindBird.this, "Error", Toast.LENGTH_SHORT).show();

            }
        }); // nested loop 1 end

        if (!sawBird[0])
            Toast.makeText(FindBird.this, sawBird[0] + "", Toast.LENGTH_SHORT).show();

    } */






    @Override
    public void onClick(View v) {

        if (v == buttonFindBirdSighting){

            checkForBid(editTextFindBird.getText().toString());

        }

    }

    //limit to last 5


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater optionsMenuInflater = getMenuInflater();
        optionsMenuInflater.inflate(R.menu.dropdown_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item){
        switch (item.getItemId()){
            case R.id.main_activity_menu_item:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(FindBird.this, MainActivity.class));
                return true;
            case R.id.home_activity_menu_item:
                startActivity(new Intent(FindBird.this, Home.class));
                return true;
            case R.id.registerbird_activity_menu_item:
                startActivity(new Intent(FindBird.this, ReportBird.class));
                return true;
            case R.id.searchbird_activity_menu_item:
                startActivity(new Intent(FindBird.this, SearchBird.class));
                return true;
            case R.id.activity_findbird_menu:
                startActivity(new Intent(FindBird.this, FindBird.class));
            case R.id.activity_deletebird_menu:
                startActivity(new Intent(FindBird.this, Delete.class));
                return true;

            default:
                return false;
        }
    }
}
