package com.example.zachteiger.finalassessment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private Button buttonSignIn, buttonRegister;
    private AutoCompleteTextView editTextEmail;
    private EditText editTextPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        buttonRegister = findViewById(R.id.buttonRegister);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        buttonSignIn.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);

    }

    private void loginUser(final String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                          //  updateUI(user);
                            Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(MainActivity.this, Home.class);
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void registerUser(final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                   // updateUI(user);
                    Toast.makeText(MainActivity.this, "Registration Success", Toast.LENGTH_SHORT).show();

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                   // updateUI(null);
                }

                // ...
            }
        });
    }


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
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                return true;
            case R.id.home_activity_menu_item:
                startActivity(new Intent(MainActivity.this, Home.class));
                return true;
            case R.id.registerbird_activity_menu_item:
                startActivity(new Intent(MainActivity.this, ReportBird.class));
                return true;
            case R.id.searchbird_activity_menu_item:
                startActivity(new Intent(MainActivity.this, SearchBird.class));
                return true;
            case R.id.activity_findbird_menu:
                startActivity(new Intent(MainActivity.this, FindBird.class));
                return true;
            case R.id.activity_deletebird_menu:
                startActivity(new Intent(MainActivity.this, Delete.class));
                return true;

            default:
                return false;
        }
    }



    @Override
    public void onClick (View v){


        if (v == buttonRegister){
            registerUser(editTextEmail.getText().toString(), editTextPassword.getText().toString());
        }

        if (v == buttonSignIn){
            loginUser(editTextEmail.getText().toString(), editTextPassword.getText().toString());
        }

        }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
    }
}
