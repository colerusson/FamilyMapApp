package com.example.familymapapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import model.User;
import request.LoginRequest;
import request.RegisterRequest;


public class LoginFragment extends Fragment {

    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;

    private Listener listener;

    public interface Listener {
        void notifyDone();
    }

    public void registerListener(Listener listener) { this.listener = listener; }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        EditText serverHostInput = view.findViewById(R.id.serverHostField);
        EditText serverPortInput = view.findViewById(R.id.serverPortField);
        EditText userNameInput = view.findViewById(R.id.userNameField);
        EditText passwordInput = view.findViewById(R.id.passwordField);
        EditText firstNameInput = view.findViewById(R.id.firstNameField);
        EditText lastNameInput = view.findViewById(R.id.lastNameField);
        EditText emailInput = view.findViewById(R.id.emailField);

        RadioButton genderButton = view.findViewById(R.id.radioGender);
        genderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Not sure how this will work, might need to change
                String gender = genderButton.getText().toString();
            }
        });

        Button loginButton = view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This is what you do when the user clicks a button
                loginRequest.setPassword(passwordInput.getText().toString());
                loginRequest.setUsername(userNameInput.getText().toString());
            }
        });

        Button registerButton = view.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerRequest.setPassword(passwordInput.getText().toString());
                registerRequest.setUsername(userNameInput.getText().toString());
                registerRequest.setEmail(emailInput.getText().toString());
                registerRequest.setFirstName(firstNameInput.getText().toString());
                registerRequest.setLastName(lastNameInput.getText().toString());
                registerRequest.setGender(genderButton.getText().toString());

            }
        });

        return view;
    }
}