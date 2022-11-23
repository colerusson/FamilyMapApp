package Data;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import request.LoginRequest;
import request.RegisterRequest;
import result.EventResult;
import result.LoginResult;
import result.PersonResult;
import result.RegisterResult;

public class ServerProxy  {

    static private final DataCache cache = DataCache.getInstance();

    public void getEventList(String serverHost, String serverPort, String authToken) {

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            Gson gson = new Gson();

            // Add an auth token to the request in the HTTP "Authorization" header
            http.addRequestProperty("Authorization", authToken);

            // Connect to the server and send the HTTP request
            http.connect();


            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                EventResult eventResult = gson.fromJson(respData, EventResult.class);
                cache.setEvents(Arrays.asList(eventResult.getData()));
            }
            else {
                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = http.getErrorStream();
                // Extract data from the HTTP response body
                String respData = readString(respBody);
                EventResult eventResult = gson.fromJson(respData, EventResult.class);
            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }
    }

    public void getPersonList(String serverHost, String serverPort, String authToken) {

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            Gson gson = new Gson();

            // Add an auth token to the request in the HTTP "Authorization" header
            http.addRequestProperty("Authorization", authToken);

            // Connect to the server and send the HTTP request
            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                PersonResult personResult = gson.fromJson(respData, PersonResult.class);
                cache.setPeople(Arrays.asList(personResult.getData()));
            }
            else {
                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = http.getErrorStream();
                // Extract data from the HTTP response body
                String respData = readString(respBody);
                PersonResult personResult = gson.fromJson(respData, PersonResult.class);
            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }
    }

    public RegisterResult register(String serverHost, String serverPort, RegisterRequest registerRequest) {

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);	// There is a request body
            Gson gson = new Gson();

            // Connect to the server and send the HTTP request
            http.connect();

            String reqData = gson.toJson(registerRequest);
            // Get the output stream containing the HTTP request body
            OutputStream reqBody = http.getOutputStream();
            // Write the JSON data to the request body
            writeString(reqData, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                RegisterResult registerResult = gson.fromJson(respData, RegisterResult.class);
                cache.setCurrentAuthToken(registerResult.getAuthtoken());
                cache.setRootPersonID(registerResult.getPersonID());
                return registerResult;
            }
            else {
                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = http.getErrorStream();
                // Extract data from the HTTP response body
                String respData = readString(respBody);
                RegisterResult registerResult = gson.fromJson(respData, RegisterResult.class);
                return registerResult;
            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
            return null;
        }
    }

    public LoginResult login(String serverHost, String serverPort, LoginRequest loginRequest) {

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);	// There is a request body
            Gson gson = new Gson();

            // Connect to the server and send the HTTP request
            http.connect();

            String reqData = gson.toJson(loginRequest);
            // Get the output stream containing the HTTP request body
            OutputStream reqBody = http.getOutputStream();
            // Write the JSON data to the request body
            writeString(reqData, reqBody);
            reqBody.close();


            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                LoginResult loginResult = gson.fromJson(respData, LoginResult.class);
                cache.setCurrentAuthToken(loginResult.getAuthtoken());
                cache.setRootPersonID(loginResult.getPersonID());
                return loginResult;
            }
            else {
                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = http.getErrorStream();
                // Extract data from the HTTP response body
                String respData = readString(respBody);
                LoginResult loginResult = gson.fromJson(respData, LoginResult.class);
                return loginResult;
            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
            return null;
        }
    }


    /*
		The readString method shows how to read a String from an InputStream.
	*/
    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    /*
        The writeString method shows how to write a String to an OutputStream.
    */
    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

}
