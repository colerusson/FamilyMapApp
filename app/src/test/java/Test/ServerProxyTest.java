package Test;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import Data.DataCache;
import Data.ServerProxy;
import request.LoginRequest;

public class ServerProxyTest {

    static private final DataCache cache = DataCache.getInstance();
    private final String serverHost = "localhost";
    private final String serverPort = "8080";
    private LoginRequest loginRequest;
    private ServerProxy serverProxy;

    @Before
    public void setUp() {

    }

    @After
    public void takeDown() {

    }

    @Test
    public void loginTestPass() {

    }

    @Test
    public void loginTestFail() {

    }

    @Test
    public void registerTestPass() {

    }

    @Test
    public void registerTestFail() {

    }

    @Test
    public void getPeopleTestPass() {

    }

    @Test
    public void getPeopleTestFail() {

    }

    @Test
    public void getEventsTestPass() {

    }

    @Test
    public void getEventsTestFail() {

    }

}
