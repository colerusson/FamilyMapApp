package Data;


import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Event;
import model.Person;

public class DataCache {

    private static DataCache instance = new DataCache();

    public static DataCache getInstance() {
        return instance;
    }

    private DataCache() {
    }

    private Map<String, Person> people;
    private Map<String, Event> events;
    private Map<String, List<Event>> personEvents;
    private Set<String> paternalAncestors;
    private Set<String> maternalAncestors;
    private String currentAuthToken;
    private boolean currentSuccess;

    public boolean isCurrentSuccess() {
        return currentSuccess;
    }

    public void setCurrentSuccess(boolean currentSuccess) {
        this.currentSuccess = currentSuccess;
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
