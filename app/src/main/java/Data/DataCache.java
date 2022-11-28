package Data;

import java.util.ArrayList;
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
    private List<Person> personList = new ArrayList<>();
    private List<Event> eventList = new ArrayList<>();
    private Map<String, List<Person>> peopleListForPerson = new HashMap<>();
    private Map<String, List<Event>> eventListForPerson = new HashMap<>();
    private String currentAuthToken;
    private String rootPersonID;
    private final List<String> authTokenList = new ArrayList<>();

    public Map<String, Person> getPeople() {
        return people;
    }

    public Map<String, Event> getEvents() {
        return events;
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

    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public Map<String, List<Person>> getPeopleListForPerson() {
        return peopleListForPerson;
    }

    public void setPeopleListForPerson(Map<String, List<Person>> peopleListForPerson) {
        this.peopleListForPerson = peopleListForPerson;
    }

    public Map<String, List<Event>> getEventListForPerson() {
        return eventListForPerson;
    }

    public void setEventListForPerson(Map<String, List<Event>> eventListForPerson) {
        this.eventListForPerson = eventListForPerson;
    }

    public List<String> getAuthTokenList() {
        return authTokenList;
    }

    public void setAuthTokenList(String authToken) {
        authTokenList.add(authToken);
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

    public void setCurrentAuthToken(String currentAuthToken) { this.currentAuthToken = currentAuthToken; }


}
