package Data;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import request.LoginRequest;
import request.RegisterRequest;

public class ServerProxy {

    // TODO: need to use request classes to make the requests and pass them in here,
    //  then use the result classes to pass back

    // TODO: Edit these classes so they actually take in what is needed

    private static void getEventList(String serverHost, String serverPort) {

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);

            // Add an auth token to the request in the HTTP "Authorization" header
            http.addRequestProperty("Authorization", "afj232hj2332");
            http.addRequestProperty("Accept", "application/json");

            // Connect to the server and send the HTTP request
            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                // Get the input stream containing the HTTP response body
                InputStream respBody = http.getInputStream();
                // Extract JSON data from the HTTP response body
                String respData = readString(respBody);
                // Display the JSON data returned from the server
                System.out.println(respData);
            }
            else {
                // The HTTP response status code indicates an error
                // occurred, so print out the message from the HTTP response
                System.out.println("ERROR: " + http.getResponseMessage());
                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = http.getErrorStream();
                // Extract data from the HTTP response body
                String respData = readString(respBody);
                // Display the data returned from the server
                System.out.println(respData);
            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }
    }

    private static void getPersonList(String serverHost, String serverPort) {

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);

            // Add an auth token to the request in the HTTP "Authorization" header
            http.addRequestProperty("Authorization", "afj232hj2332");
            http.addRequestProperty("Accept", "application/json");

            // Connect to the server and send the HTTP request
            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                // Get the input stream containing the HTTP response body
                InputStream respBody = http.getInputStream();
                // Extract JSON data from the HTTP response body
                String respData = readString(respBody);
                // Display the JSON data returned from the server
                System.out.println(respData);
            }
            else {
                // The HTTP response status code indicates an error
                // occurred, so print out the message from the HTTP response
                System.out.println("ERROR: " + http.getResponseMessage());
                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = http.getErrorStream();
                // Extract data from the HTTP response body
                String respData = readString(respBody);
                // Display the data returned from the server
                System.out.println(respData);
            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }
    }

    private static void register(String serverHost, String serverPort, RegisterRequest registerRequest) {

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);	// There is a request body

            // Add an auth token to the request in the HTTP "Authorization" header
            http.addRequestProperty("Authorization", "afj232hj2332");
            http.addRequestProperty("Accept", "application/json");

            // Connect to the server and send the HTTP request
            http.connect();

            String reqData =
                    "{" +
                            "\"route\": \"atlanta-miami\"" +
                            "}";

            // Get the output stream containing the HTTP request body
            OutputStream reqBody = http.getOutputStream();
            // Write the JSON data to the request body
            writeString(reqData, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Route successfully claimed.");
            }
            else {
                // The HTTP response status code indicates an error
                // occurred, so print out the message from the HTTP response
                System.out.println("ERROR: " + http.getResponseMessage());
                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = http.getErrorStream();
                // Extract data from the HTTP response body
                String respData = readString(respBody);
                // Display the data returned from the server
                System.out.println(respData);
            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }
    }

    private static void login(String serverHost, String serverPort, LoginRequest loginRequest) {

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);	// There is a request body

            // Add an auth token to the request in the HTTP "Authorization" header
            http.addRequestProperty("Authorization", "afj232hj2332");
            http.addRequestProperty("Accept", "application/json");

            // Connect to the server and send the HTTP request
            http.connect();

            String reqData =
                    "{" +
                            "\"route\": \"atlanta-miami\"" +
                            "}";

            // Get the output stream containing the HTTP request body
            OutputStream reqBody = http.getOutputStream();
            // Write the JSON data to the request body
            writeString(reqData, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Route successfully claimed.");
            }
            else {
                // The HTTP response status code indicates an error
                // occurred, so print out the message from the HTTP response
                System.out.println("ERROR: " + http.getResponseMessage());
                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = http.getErrorStream();
                // Extract data from the HTTP response body
                String respData = readString(respBody);
                // Display the data returned from the server
                System.out.println(respData);
            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
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
