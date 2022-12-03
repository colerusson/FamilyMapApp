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
import result.LoginResult;

public class CalculationsTest {

    static private final DataCache cache = DataCache.getInstance();
    private final ServerProxy serverProxy = new ServerProxy();

    @BeforeEach
    public void setUp() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("sheila");
        loginRequest.setPassword("parker");

        String serverHost = "localhost";
        String serverPort = "8080";
        LoginResult loginResult = serverProxy.login(serverHost, serverPort, loginRequest);

        serverProxy.getEventList(serverHost, serverPort, loginResult.getAuthtoken());
        serverProxy.getPersonList(serverHost, serverPort, loginResult.getAuthtoken());
        serverProxy.setLists();
    }

    @AfterEach
    public void takeDown() {
        cache.clear();
    }

    @Test
    public void relationshipTestPass() {
        Person rootPerson = cache.getPeople().get(cache.getRootPersonID());
        assert rootPerson != null;
        String mother = rootPerson.getMotherID();
        String father = rootPerson.getFatherID();
        String spouse = rootPerson.getSpouseID();

        String motherRelationship = cache.getRelationshipTypeTest(mother);
        String fatherRelationship = cache.getRelationshipTypeTest(father);
        String spouseRelationship = cache.getRelationshipTypeTest(spouse);

        assertEquals(motherRelationship, "Mother");
        assertEquals(fatherRelationship, "Father");
        assertEquals(spouseRelationship, "Spouse");
    }

    @Test
    public void relationshipTestFail() {
        String mother = "mother";
        String father = "father";
        String spouse = "spouse";

        String motherRelationship = cache.getRelationshipTypeTest(mother);
        String fatherRelationship = cache.getRelationshipTypeTest(father);
        String spouseRelationship = cache.getRelationshipTypeTest(spouse);

        assertNotEquals(motherRelationship, "Mother");
        assertNotEquals(fatherRelationship, "Father");
        assertNotEquals(spouseRelationship, "Spouse");
    }

    @Test
    public void filterEventsTestPass() {
        List<Event> eventsNoFilter = cache.getEventListForPerson(cache.getRootPersonID());
        cache.setFatherSide(false);
        cache.setMotherSide(false);
        List<Event> eventsUserSpouse = cache.filterEvents();
        cache.setFatherSide(true);
        List<Event> eventsFatherSide = cache.filterEvents();
        cache.setMotherSide(true);
        List<Event> eventsBothSides = cache.filterEvents();

        assertEquals(eventsNoFilter.size(), eventsBothSides.size());
        assertNotEquals(eventsNoFilter.size(), eventsUserSpouse.size());
        assertNotEquals(eventsNoFilter.size(), eventsFatherSide.size());
        assertEquals(6, eventsUserSpouse.size());
    }

    @Test
    public void filterEventsTestFail() {
        List<Event> eventsNoFilter = cache.getEventListForPerson(cache.getRootPersonID());
        List<Event> eventsUserSpouse = cache.filterEvents();
        List<Event> eventsFatherSide = cache.filterEvents();
        cache.setFatherSide(false);
        List<Event> eventsBothSides = cache.filterEvents();

        assertNotEquals(eventsNoFilter.size(), eventsBothSides.size());
        assertEquals(eventsNoFilter.size(), eventsUserSpouse.size());
        assertEquals(eventsNoFilter.size(), eventsFatherSide.size());
        assertNotEquals(6, eventsUserSpouse.size());
    }

    @Test
    public void sortEventsTestPass() {
        List<Event> events = cache.getEventListForPerson(cache.getRootPersonID());
        cache.sortEventsTest(events);

        for (int i = 0; i < events.size() - 1; ++i) {
            if (events.get(i).getYear() != 2014 && events.get(i + 1).getYear() != 2014) {
                assertNotEquals(events.get(i).getYear(), events.get(i + 1).getYear());
            }
        }
    }

    @Test
    public void sortEventsTestFail() {
        List<Event> events = cache.getEventListForPerson(cache.getRootPersonID());
        boolean notSorted = false;

        for (int i = 0; i < events.size() - 1; ++i) {
            if (events.get(i).getYear() > events.get(i + 1).getYear()) {
                notSorted = true;
            }
        }

        assertTrue(notSorted);
    }

    @Test
    public void searchTestPass() {
        String inputPerson = "sheila";
        String inputEvent = "completed";
        List<Event> searchedEvents = cache.getSearchedEventList(inputEvent);
        List<Person> searchedPeople = cache.getSearchedPersonList(inputPerson);

        assertNotNull(searchedPeople);
        assertNotNull(searchedEvents);
        assertEquals(2, searchedEvents.size());
        assertEquals(1, searchedPeople.size());
        assertEquals("sheila", searchedPeople.get(0).getFirstName().toLowerCase());
        assertEquals("completed asteroids", searchedEvents.get(0).getEventType().toLowerCase());
    }

    @Test
    public void searchTestFail() {
        String inputPerson = "garbage";
        String inputEvent = "garbage";
        List<Event> searchedEvents = cache.getSearchedEventList(inputEvent);
        List<Person> searchedPeople = cache.getSearchedPersonList(inputPerson);

        assertNull(searchedPeople);
        assertNull(searchedEvents);
    }

}
