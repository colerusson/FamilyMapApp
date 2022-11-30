package Data;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private final Map<String, List<Person>> peopleListForPerson = new HashMap<>();
    private final Map<String, List<Event>> eventListForPerson = new HashMap<>();
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

    public List<Person> getPeopleListForPerson(String personID) {
        return peopleListForPerson.get(personID);
    }

    public void setPeopleListForPerson() {
        for (Person person : personList) {
            List<Person> listToAdd = new ArrayList<>();
            String spouseID = person.getSpouseID();
            String fatherID = person.getFatherID();
            String motherID = person.getMotherID();
            if (spouseID != null) {
                listToAdd.add(getPeople().get(spouseID));
            }
            if (fatherID != null) {
                listToAdd.add(getPeople().get(fatherID));
            }
            if (motherID != null) {
                listToAdd.add(getPeople().get(motherID));
            }
            for (Person personFamily : personList) {
                if (!Objects.equals(personFamily.getPersonID(), person.getPersonID())) {
                    if (personFamily.getFatherID() != null) {
                        if (personFamily.getFatherID().equals(person.getPersonID())) {
                            listToAdd.add(personFamily);
                        }
                    }
                    if (personFamily.getMotherID() != null) {
                        if (personFamily.getMotherID().equals(person.getPersonID())) {
                            listToAdd.add(personFamily);
                        }
                    }
                }
            }
            peopleListForPerson.put(person.getPersonID(), listToAdd);
        }
    }

    public List<Event> getEventListForPerson(String personID) {
        return eventListForPerson.get(personID);
    }

    public void setEventListForPerson() {
        for (Person person : personList) {
            List<Event> listToAdd = new ArrayList<>();
            String personID = person.getPersonID();
            for (Event eventToAdd : eventList) {
                String eventPersonID = eventToAdd.getPersonID();
                if (personID.equals(eventPersonID)) {
                    listToAdd.add(eventToAdd);
                }
            }
            eventListForPerson.put(person.getPersonID(), listToAdd);
        }
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
