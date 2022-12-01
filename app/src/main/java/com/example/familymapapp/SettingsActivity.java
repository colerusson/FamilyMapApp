package com.example.familymapapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import Data.DataCache;

public class SettingsActivity extends AppCompatActivity {

    static private final DataCache cache = DataCache.getInstance();

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Switch lifeStoryLines = findViewById(R.id.lifeLinesSwitch);
        Switch familyTreeLines = findViewById(R.id.familyLinesSwitch);
        Switch spouseLines = findViewById(R.id.spouseLinesSwitch);
        Switch fatherSide = findViewById(R.id.fatherSideSwitch);
        Switch motherSide = findViewById(R.id.motherSideSwitch);
        Switch maleEvents = findViewById(R.id.maleEventsSwitch);
        Switch femaleEvents = findViewById(R.id.femaleEventsSwitch);

        lifeStoryLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lifeStoryLines.setChecked(isChecked);
                cache.setLifeStoryLines(isChecked);
            }
        });

        familyTreeLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                familyTreeLines.setChecked(isChecked);
                cache.setFamilyTreeLines(isChecked);
            }
        });

        spouseLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                spouseLines.setChecked(isChecked);
                cache.setSpouseLines(isChecked);
            }
        });

        fatherSide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fatherSide.setChecked(isChecked);
                cache.setFatherSide(isChecked);
            }
        });

        motherSide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                motherSide.setChecked(isChecked);
                cache.setMotherSide(isChecked);
            }
        });

        maleEvents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                maleEvents.setChecked(isChecked);
                cache.setMaleEvents(isChecked);
            }
        });

        femaleEvents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                femaleEvents.setChecked(isChecked);
                cache.setFemaleEvents(isChecked);
            }
        });

        LinearLayout logout = findViewById(R.id.logoutLayout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create an intent with 2 flags
                // one to clear stack except this activity
                // get this activity
                // when you logout, make sure you clear the data cache
                // cache.clear();
                Toast.makeText(SettingsActivity.this,  "Logging Out", Toast.LENGTH_LONG).show();
            }
        });

    }
}