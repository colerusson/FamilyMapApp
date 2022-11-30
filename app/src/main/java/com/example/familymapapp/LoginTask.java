package com.example.familymapapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import Data.ServerProxy;
import request.LoginRequest;
import result.LoginResult;

public class LoginTask implements Runnable {

    private final Handler messageHandler;
    private final LoginRequest loginRequest;
    private final String serverHost;
    private final String serverPort;

    public LoginTask(Handler messageHandler, LoginRequest loginRequest, String serverHost, String serverPort) {
        this.messageHandler = messageHandler;
        this.loginRequest = loginRequest;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        ServerProxy serverProxy = new ServerProxy();
        LoginResult loginResult = serverProxy.login(serverHost, serverPort, loginRequest);

        boolean success = loginResult.isSuccess();

        if (success) {
            serverProxy.getEventList(serverHost, serverPort, loginResult.getAuthtoken());
            serverProxy.getPersonList(serverHost, serverPort, loginResult.getAuthtoken());
            serverProxy.setLists();
        }

        sendMessage(success);
    }

    private void sendMessage(boolean success) {
        Message message = Message.obtain();

        Bundle messageBundle = new Bundle();
        messageBundle.putBoolean(LoginFragment.SUCCESS_KEY, success);
        message.setData(messageBundle);

        messageHandler.sendMessage(message);
    }
}
