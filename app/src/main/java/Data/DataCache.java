package Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Event;
import model.Person;

public class DataCache {

    private static final DataCache instance = new DataCache();

    public static DataCache getInstance() {
        return instance;
    }

    private DataCache() {
    }

    private final Map<String, Person> people = new HashMap<>();
    private final Map<String, Event> events = new HashMap<>();
    private final Map<String, List<Person>> personList = new HashMap<>();
    private String currentAuthToken;

    private String rootPersonID;

    public Map<String, Person> getPeople() {
        return people;
    }

    public Map<String, Event> getEvents() {
        return events;
    }

    public String getRootPersonID() {
        return rootPersonID;
    }

    public void setRootPersonID(String rootPersonID) {
        this.rootPersonID = rootPersonID;
    }

    public String getCurrentAuthToken() {
        return currentAuthToken;
    }

    public void setCurrentAuthToken(String currentAuthToken) {
        this.currentAuthToken = currentAuthToken;
    }

    public void setPeople(List<Person> persons) {
        for (Person personToAdd : persons) {
            people.put(personToAdd.getPersonID(), personToAdd);
        }
    }

    public void setEvents(List<Event> eventsList) {
        for (Event eventToAdd : eventsList) {
            events.put(eventToAdd.getEventID(), eventToAdd);
        }
    }

}
