package com.example.familymapapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Data.DataCache;
import request.LoginRequest;
import request.RegisterRequest;


public class LoginFragment extends Fragment {

    public static final String SUCCESS_KEY = "SUCCESS";
    static private final DataCache cache = DataCache.getInstance();
    private String gender;

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

        gender = "m";

        RadioGroup radioGroup = view.findViewById(R.id.radioGender);
        radioGroup.setOnCheckedChangeListener(((radioGender, i) -> {
            if (i == 0) {
                gender = "m";
            }
            else {
                gender = "f";
            }
        }));

        Button loginButton = view.findViewById(R.id.loginButton);
        loginButton.setEnabled(false);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This is what you do when the user clicks a button
                LoginRequest loginRequest = new LoginRequest();

                loginRequest.setPassword(passwordInput.getText().toString());
                loginRequest.setUsername(userNameInput.getText().toString());
                String serverHostVal = serverHostInput.getText().toString();
                String serverPortVal = serverPortInput.getText().toString();

                Handler uiThreadMessageHandler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();
                        boolean success = bundle.getBoolean(SUCCESS_KEY);
                        if (success) {
                            String firstName = cache.getPeople().get(cache.getRootPersonID()).getFirstName();
                            String lastName = cache.getPeople().get(cache.getRootPersonID()).getLastName();
                            Toast.makeText(getActivity(), firstName + " " + lastName, Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getActivity(),  "Error in logging user in", Toast.LENGTH_LONG).show();
                        }
                    }
                };

                LoginTask task = new LoginTask(uiThreadMessageHandler, loginRequest, serverHostVal, serverPortVal);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(task);

            }
        });

        Button registerButton = view.findViewById(R.id.registerButton);
        registerButton.setEnabled(false);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterRequest registerRequest = new RegisterRequest();

                registerRequest.setPassword(passwordInput.getText().toString());
                registerRequest.setUsername(userNameInput.getText().toString());
                registerRequest.setEmail(emailInput.getText().toString());
                registerRequest.setFirstName(firstNameInput.getText().toString());
                registerRequest.setLastName(lastNameInput.getText().toString());
                registerRequest.setGender(gender);
                String serverHostVal = serverHostInput.getText().toString();
                String serverPortVal = serverPortInput.getText().toString();

                Handler uiThreadMessageHandler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();
                        boolean success = bundle.getBoolean(SUCCESS_KEY);
                        if (success) {
                            String firstName = cache.getPeople().get(cache.getRootPersonID()).getFirstName();
                            String lastName = cache.getPeople().get(cache.getRootPersonID()).getLastName();
                            Toast.makeText(getActivity(), firstName + " " + lastName, Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getActivity(),  "Error in registering user", Toast.LENGTH_LONG).show();
                        }
                    }
                };

                RegisterTask task = new RegisterTask(uiThreadMessageHandler, registerRequest, serverHostVal, serverPortVal);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(task);
            }

        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String serverHost = serverHostInput.getText().toString().trim();
                String serverPort = serverPortInput.getText().toString().trim();
                String username = userNameInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                String firstName = firstNameInput.getText().toString().trim();
                String lastName = lastNameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();

                loginButton.setEnabled(!serverHost.isEmpty() && !serverPort.isEmpty() && !username.isEmpty() &&!password.isEmpty());
                registerButton.setEnabled(!serverHost.isEmpty() && !serverPort.isEmpty() && !username.isEmpty() &&!password.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };

        serverHostInput.addTextChangedListener(textWatcher);
        serverPortInput.addTextChangedListener(textWatcher);
        userNameInput.addTextChangedListener(textWatcher);
        passwordInput.addTextChangedListener(textWatcher);
        firstNameInput.addTextChangedListener(textWatcher);
        lastNameInput.addTextChangedListener(textWatcher);
        emailInput.addTextChangedListener(textWatcher);

        return view;
    }

}