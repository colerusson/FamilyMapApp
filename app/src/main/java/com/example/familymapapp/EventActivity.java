package com.example.familymapapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import Data.DataCache;
import model.Event;

public class EventActivity extends AppCompatActivity {

    public static final String EVENT_ID = "eventID";
    static private final DataCache cache = DataCache.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Intent intent = getIntent();
        String eventID = intent.getStringExtra(EVENT_ID);
        Event event = cache.getEvents().get(eventID);

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = new MapFragment(event);

        fragmentManager.beginTransaction()
                .add(R.id.eventFragmentFrameLayout, fragment)
                .commit();
    }
}