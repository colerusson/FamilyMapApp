package com.example.familymapapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        loginFragment = (LoginFragment)fragmentManager.findFragmentById(R.id.loginFragment);
    }

//
//    private Fragment createLoginFragment() {
//        LoginFragment fragment = new LoginFragment();
//        fragment.registerListener((LoginFragment.Listener) this);
//        return fragment;
//    }

//    @Override
//    public void notifyDone() {
//        FragmentManager fragmentManager = this.getSupportFragmentManager();
//        MapFragment fragment = new MapFragment();
//    }

}