package com.example.zachteiger.finalassessment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends Activity implements View.OnClickListener {

    private Button HReportSighting, HLogout, HSearchSighting, buttonDeleteBird, buttonFindBird;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        HReportSighting = findViewById(R.id.HReportSighting);
        HReportSighting.setOnClickListener(this);

        buttonFindBird = findViewById(R.id.buttonFindBird);
        buttonFindBird.setOnClickListener(this);

        buttonDeleteBird = findViewById(R.id.buttonDeleteBird);
        buttonDeleteBird.setOnClickListener(this);

        HSearchSighting = findViewById(R.id.HSearchSighting);
        HSearchSighting.setOnClickListener(this);

        HLogout = findViewById(R.id.HLogout);
        HLogout.setOnClickListener(this);

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
                startActivity(new Intent(Home.this, MainActivity.class));
                return true;
            case R.id.home_activity_menu_item:
                startActivity(new Intent(Home.this, Home.class));
                return true;
            case R.id.registerbird_activity_menu_item:
                startActivity(new Intent(Home.this, ReportBird.class));
                return true;
            case R.id.searchbird_activity_menu_item:
                startActivity(new Intent(Home.this, SearchBird.class));
                return true;
            case R.id.activity_deletebird_menu:
                startActivity(new Intent(Home.this, Delete.class));
                return true;
            case R.id.activity_findbird_menu:
                startActivity(new Intent(Home.this, FindBird.class));
                return true;


                default:
                    return false;
        }
    }





    @Override
    public void onClick(View v) {

        if (v == HLogout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Home.this, MainActivity.class));
        }

        if (v == HReportSighting){
            startActivity(new Intent(Home.this, ReportBird.class));
        }

        if (v == HSearchSighting){
            startActivity(new Intent(Home.this, SearchBird.class));
        }

        if (v == buttonDeleteBird){
            startActivity(new Intent(Home.this, Delete.class));
        }

        if (v == buttonFindBird){
            startActivity(new Intent(Home.this, FindBird.class));
        }
    }
}
