package com.example.familymapapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import Data.DataCache;

public class SettingsActivity extends AppCompatActivity {

    static private final DataCache cache = DataCache.getInstance();

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Intent intent = new Intent(this, MainActivity.class);

        Switch lifeStoryLines = findViewById(R.id.lifeLinesSwitch);
        Switch familyTreeLines = findViewById(R.id.familyLinesSwitch);
        Switch spouseLines = findViewById(R.id.spouseLinesSwitch);
        Switch fatherSide = findViewById(R.id.fatherSideSwitch);
        Switch motherSide = findViewById(R.id.motherSideSwitch);
        Switch maleEvents = findViewById(R.id.maleEventsSwitch);
        Switch femaleEvents = findViewById(R.id.femaleEventsSwitch);

        lifeStoryLines.setChecked(cache.isLifeStoryLines());
        familyTreeLines.setChecked(cache.isFamilyTreeLines());
        spouseLines.setChecked(cache.isSpouseLines());
        fatherSide.setChecked(cache.isFatherSide());
        motherSide.setChecked(cache.isMotherSide());
        maleEvents.setChecked(cache.isMaleEvents());
        femaleEvents.setChecked(cache.isFemaleEvents());

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
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                cache.clear();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }

}