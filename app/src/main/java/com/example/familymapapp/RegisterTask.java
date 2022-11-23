package com.example.familymapapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import Data.ServerProxy;
import request.RegisterRequest;
import result.RegisterResult;

public class RegisterTask implements Runnable {

    private final Handler messageHandler;
    private final RegisterRequest registerRequest;
    private final String serverHost;
    private final String serverPort;


    public RegisterTask(Handler messageHandler, RegisterRequest registerRequest, String serverHost, String serverPort) {
        this.messageHandler = messageHandler;
        this.registerRequest = registerRequest;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        ServerProxy serverProxy = new ServerProxy();
        RegisterResult registerResult = serverProxy.register(serverHost, serverPort, registerRequest);

        boolean success = registerResult.isSuccess();

        if (success) {
            serverProxy.getEventList(serverHost, serverPort, registerResult.getAuthtoken());
            serverProxy.getPersonList(serverHost, serverPort, registerResult.getAuthtoken());
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
