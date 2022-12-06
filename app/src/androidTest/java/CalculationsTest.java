import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Objects;

import Data.DataCache;
import Data.ServerProxy;
import model.Event;
import model.Person;
import request.LoginRequest;
import result.LoginResult;

public class CalculationsTest {

    static private final DataCache cache = DataCache.getInstance();
    private final ServerProxy serverProxy = new ServerProxy();

    @Before
    public void setUp() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("sheila");
        loginRequest.setPassword("parker");

        String serverHost = "10.0.2.2";
        String serverPort = "8080";
        LoginResult loginResult = serverProxy.login(serverHost, serverPort, loginRequest);

        serverProxy.getEventList(serverHost, serverPort, loginResult.getAuthtoken());
        serverProxy.getPersonList(serverHost, serverPort, loginResult.getAuthtoken());
        serverProxy.setLists();
    }

    @After
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

        assert(motherRelationship.equals("Mother"));
        assert(fatherRelationship.equals("Father"));
        assert(spouseRelationship.equals("Spouse"));
    }

    @Test
    public void relationshipTestFail() {
        String mother = "mother";
        String father = "father";
        String spouse = "spouse";

        String motherRelationship = cache.getRelationshipTypeTest(mother);
        String fatherRelationship = cache.getRelationshipTypeTest(father);
        String spouseRelationship = cache.getRelationshipTypeTest(spouse);

        assert(!motherRelationship.equals("Mother"));
        assert(!fatherRelationship.equals("Father"));
        assert(!spouseRelationship.equals("Spouse"));
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

        assert(eventsNoFilter.size() == eventsBothSides.size());
        assert(eventsNoFilter.size() != eventsUserSpouse.size());
        assert(eventsNoFilter.size() != eventsFatherSide.size());
        assert(eventsUserSpouse.size() == 6);
    }

    @Test
    public void filterEventsTestFail() {
        List<Event> eventsNoFilter = cache.getEventListForPerson(cache.getRootPersonID());
        List<Event> eventsUserSpouse = cache.filterEvents();
        List<Event> eventsFatherSide = cache.filterEvents();
        cache.setFatherSide(false);
        List<Event> eventsBothSides = cache.filterEvents();

        assert(eventsNoFilter.size() != eventsBothSides.size());
        assert(eventsNoFilter.size() == eventsUserSpouse.size());
        assert(eventsNoFilter.size() == eventsFatherSide.size());
        assert(eventsUserSpouse.size() != 6);
    }

    @Test
    public void sortEventsTestPass() {
        List<Event> events = cache.getEventListForPerson("Mrs_Jones");
        cache.sortEventsTest(events);

        for (int i = 0; i < events.size() - 1; ++i) {
            assert(!Objects.equals(events.get(i).getYear(), events.get(i + 1).getYear()));
        }
    }

    @Test
    public void sortEventsTestFail() {
        List<Event> events = cache.getEventListForPerson("Mrs_Jones");
        boolean notSorted = false;

        for (int i = 0; i < events.size() - 1; ++i) {
            if (events.get(i).getYear() > events.get(i + 1).getYear()) {
                notSorted = true;
            }
        }

        assert(notSorted);
    }

    @Test
    public void searchTestPass() {
        String inputPerson = "sheila";
        String inputEvent = "completed";
        List<Event> searchedEvents = cache.getSearchedEventList(inputEvent);
        List<Person> searchedPeople = cache.getSearchedPersonList(inputPerson);

        assert(searchedPeople != null);
        assert(searchedEvents != null);
        assert(searchedEvents.size() == 2);
        assert(searchedPeople.size() == 1);
        assert(searchedPeople.get(0).getFirstName().toLowerCase().equals("sheila"));
        assert(searchedEvents.get(0).getEventType().toLowerCase().equals("completed asteroids"));
    }

    @Test
    public void searchTestFail() {
        String inputPerson = "garbage";
        String inputEvent = "garbage";
        List<Event> searchedEvents = cache.getSearchedEventList(inputEvent);
        List<Person> searchedPeople = cache.getSearchedPersonList(inputPerson);

        assert(searchedPeople.size() == 0);
        assert(searchedEvents.size() == 0);
    }

}
