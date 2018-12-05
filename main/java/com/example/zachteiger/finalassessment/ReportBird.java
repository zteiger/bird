package com.example.zachteiger.finalassessment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReportBird extends Activity implements View.OnClickListener {

    public FirebaseDatabase database;
    public FirebaseAuth mAuth;
    public DatabaseReference myRef;

    private Button buttonReportBird;
    private Button mPickDate;
    public int BirdImportance;
    private EditText rBirdName, rZipCode, rUserName;
    private TextView showMyDate;

    private int mYear;
    private int mMonth;
    private int mDay;

    static final int DATE_DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportbird);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        myRef = database.getReference("Bird");


        buttonReportBird = findViewById(R.id.buttonReportBird);
        buttonReportBird.setOnClickListener(this);

        mPickDate = findViewById(R.id.myDatePickerButton);
        mPickDate.setOnClickListener(this);

        rBirdName = findViewById(R.id.rBirdName);
        showMyDate = findViewById(R.id.showMyDate);
        rZipCode = findViewById(R.id.rZipCode);
        // rSightingImportance = findViewById(R.id.rSightingImportance);
        rUserName = findViewById(R.id.rUserName);

        showMyDate = (TextView) findViewById(R.id.showMyDate);
        mPickDate = (Button) findViewById(R.id.myDatePickerButton);

        mPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


    }

    private void updateDisplay() {
        this.showMyDate.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(mMonth + 1).append("-").append(mDay).append("-").append(mYear).append(" "));

    }



    @Override
    public void onClick(View v) {


        if (v == buttonReportBird) {

            // first check if the bird siting (most recent siting per zipcode) and that would be checked via zip code

            final DatabaseReference mRef = myRef.child(rZipCode.getText().toString()); // tried removing .child(rZipCode.getText().toString()); so that it would create a
            Bird newBird = new Bird(rZipCode.getText().toString().toUpperCase(),
                    rBirdName.getText().toString().toUpperCase(),
                    new Date(TextUtils.join("/", showMyDate.getText().toString().split("-"))).getTime(),
                    BirdImportance = 0, rUserName.getText().toString());

            mRef.push().setValue(newBird);

            Toast.makeText(ReportBird.this, "Bird Reported!", Toast.LENGTH_SHORT).show();

            rBirdName.setText("");
            showMyDate.setText("");
            rZipCode.setText("");


        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };


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
                startActivity(new Intent(ReportBird.this, MainActivity.class));
                return true;
            case R.id.home_activity_menu_item:
                startActivity(new Intent(ReportBird.this, Home.class));
                return true;
            case R.id.registerbird_activity_menu_item:
                startActivity(new Intent(ReportBird.this, ReportBird.class));
                return true;
            case R.id.searchbird_activity_menu_item:
                startActivity(new Intent(ReportBird.this, SearchBird.class));
                return true;
            case R.id.activity_findbird_menu:
                startActivity(new Intent(ReportBird.this, FindBird.class));
                return true;
            case R.id.activity_deletebird_menu:
                startActivity(new Intent(ReportBird.this, Delete.class));
                return true;

            default:
                return false;
        }
    }

}
