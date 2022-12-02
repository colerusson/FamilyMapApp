package Test;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import Data.DataCache;
import Data.ServerProxy;
import model.Event;
import model.Person;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;

public class ServerProxyTest {

    static private final DataCache cache = DataCache.getInstance();
    private final String serverHost = "localhost";
    private final String serverPort = "8080";
    private final ServerProxy serverProxy = new ServerProxy();

    @BeforeEach
    public void setUp() {
        serverProxy.clear(serverHost, serverPort);
        cache.clear();
    }

    @AfterEach
    public void takeDown() {
        serverProxy.clear(serverHost, serverPort);
        cache.clear();
    }

    @Test
    public void loginTestPass() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFirstName("First");
        registerRequest.setLastName("Last");
        registerRequest.setUsername("username");
        registerRequest.setPassword("password");
        registerRequest.setEmail("user@email.com");
        registerRequest.setGender("m");

        serverProxy.register(serverHost, serverPort, registerRequest);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username");
        loginRequest.setPassword("password");

        LoginResult loginResult = serverProxy.login(serverHost, serverPort, loginRequest);

        assertNotNull(loginResult.getPersonID());
        assertEquals(loginResult.getUsername(), "username");
        assertNotNull(loginResult.getMessage());
        assertTrue(loginResult.isSuccess());
    }

//    @Test
//    public void loginTestFail() {
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setUsername("username");
//        loginRequest.setPassword("password");
//
//        LoginResult loginResult = serverProxy.login(serverHost, serverPort, loginRequest);
//
//        assert(loginResult.getPersonID() == null);
//        assert(loginResult.getUsername() == null);
//        assert(loginResult.getMessage() != null);
//        assert(!loginResult.isSuccess());
//    }
//
//    @Test
//    public void registerTestPass() {
//        RegisterRequest registerRequest = new RegisterRequest();
//        registerRequest.setFirstName("First");
//        registerRequest.setLastName("Last");
//        registerRequest.setUsername("username");
//        registerRequest.setPassword("password");
//        registerRequest.setEmail("user@email.com");
//        registerRequest.setGender("m");
//
//        RegisterResult registerResult = serverProxy.register(serverHost, serverPort, registerRequest);
//
//        assert(registerResult.isSuccess());
//        assert(registerResult.getPersonID() != null);
//        assert(registerResult.getMessage() == null);
//        assert(registerResult.getUsername().equals("username"));
//    }
//
//    @Test
//    public void registerTestFail() {
//        RegisterRequest registerRequest = new RegisterRequest();
//        registerRequest.setFirstName("First");
//        registerRequest.setLastName("Last");
//        registerRequest.setUsername("username");
//        registerRequest.setPassword("password");
//        registerRequest.setEmail("user@email.com");
//        registerRequest.setGender("m");
//
//        RegisterRequest registerRequestFail = new RegisterRequest();
//        registerRequestFail.setFirstName("First");
//        registerRequestFail.setLastName("Last");
//        registerRequestFail.setUsername("username");
//        registerRequestFail.setPassword("password");
//        registerRequestFail.setEmail("user@email.com");
//        registerRequestFail.setGender("m");
//
//        RegisterResult registerResult = serverProxy.register(serverHost, serverPort, registerRequestFail);
//
//        assert(!registerResult.isSuccess());
//        assert(registerResult.getPersonID() == null);
//        assert(registerResult.getMessage() != null);
//        assert(registerResult.getUsername() == null);
//    }
//
//    @Test
//    public void getPeopleTestPass() {
//        RegisterRequest registerRequest = new RegisterRequest();
//        registerRequest.setFirstName("First");
//        registerRequest.setLastName("Last");
//        registerRequest.setUsername("username");
//        registerRequest.setPassword("password");
//        registerRequest.setEmail("user@email.com");
//        registerRequest.setGender("m");
//
//        RegisterResult registerResult = serverProxy.register(serverHost, serverPort, registerRequest);
//
//        serverProxy.getPersonList(serverHost, serverPort, registerResult.getAuthtoken());
//
//        List<Person> people = cache.getPersonList();
//
//        assert(people != null);
//        assert(people.size() > 0);
//        assert(people.get(0).getAssociatedUsername().equals("username"));
//    }
//
//    @Test
//    public void getPeopleTestFail() {
//        RegisterRequest registerRequest = new RegisterRequest();
//        registerRequest.setFirstName("First");
//        registerRequest.setLastName("Last");
//        registerRequest.setUsername("username");
//        registerRequest.setPassword("password");
//        registerRequest.setEmail("user@email.com");
//        registerRequest.setGender("m");
//
//        RegisterResult registerResult = serverProxy.register(serverHost, serverPort, registerRequest);
//
//        serverProxy.clear(serverHost, serverPort);
//
//        serverProxy.getPersonList(serverHost, serverPort, registerResult.getAuthtoken());
//
//        List<Person> people = cache.getPersonList();
//
//        assert(people == null);
//    }
//
//    @Test
//    public void getEventsTestPass() {
//        RegisterRequest registerRequest = new RegisterRequest();
//        registerRequest.setFirstName("First");
//        registerRequest.setLastName("Last");
//        registerRequest.setUsername("username");
//        registerRequest.setPassword("password");
//        registerRequest.setEmail("user@email.com");
//        registerRequest.setGender("m");
//
//        RegisterResult registerResult = serverProxy.register(serverHost, serverPort, registerRequest);
//
//        serverProxy.getEventList(serverHost, serverPort, registerResult.getAuthtoken());
//
//        List<Event> events = cache.getEventList();
//
//        assert(events != null);
//        assert(events.size() > 0);
//        assert(events.get(0).getAssociatedUsername().equals("username"));
//    }
//
//    @Test
//    public void getEventsTestFail() {
//        RegisterRequest registerRequest = new RegisterRequest();
//        registerRequest.setFirstName("First");
//        registerRequest.setLastName("Last");
//        registerRequest.setUsername("username");
//        registerRequest.setPassword("password");
//        registerRequest.setEmail("user@email.com");
//        registerRequest.setGender("m");
//
//        RegisterResult registerResult = serverProxy.register(serverHost, serverPort, registerRequest);
//
//        serverProxy.clear(serverHost, serverPort);
//
//        serverProxy.getEventList(serverHost, serverPort, registerResult.getAuthtoken());
//
//        List<Event> events = cache.getEventList();
//
//        assert(events == null);
//    }

}
