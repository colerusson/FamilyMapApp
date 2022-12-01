package Data;

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

    // Settings for user
    private boolean lifeStoryLines;
    private boolean familyTreeLines;
    private boolean spouseLines;
    private boolean fatherSide;
    private boolean motherSide;
    private boolean maleEvents;
    private boolean femaleEvents;

    private String rootPersonID;
    private final Map<String, Person> people = new HashMap<>();
    private final Map<String, Event> events = new HashMap<>();
    private List<Person> personList = new ArrayList<>();
    private List<Event> eventList = new ArrayList<>();
    private final Map<String, List<Person>> peopleListForPerson = new HashMap<>();
    private final Map<String, List<Event>> eventListForPerson = new HashMap<>();
    private final List<Event> maternalEvents = new ArrayList<>();
    private final List<Event> paternalEvents = new ArrayList<>();
    private final List<Event> userSpouseEvents = new ArrayList<>();

    public void setUserSpouseEvents(String personID) {
        List<Event> eventsUser = eventListForPerson.get(personID);
        assert eventsUser != null;
        Person person = people.get(personID);
        assert person != null;
        String spouseID = person.getSpouseID();
        List<Event> eventsSpouse = eventListForPerson.get(spouseID);
        userSpouseEvents.addAll(eventsUser);
        assert eventsSpouse != null;
        userSpouseEvents.addAll(eventsSpouse);
    }

    public List<Event> getUserSpouseEvents() {
        return userSpouseEvents;
    }

    public List<Event> getMaternalEvents() {
        return maternalEvents;
    }

    public List<Event> getPaternalEvents() {
        return paternalEvents;
    }

    public void addPersonToFamilyEvents(String personID) {
        List<Event> events = eventListForPerson.get(personID);
        assert events != null;
        maternalEvents.addAll(events);
        paternalEvents.addAll(events);
        Person person = people.get(personID);
        assert person != null;
        String spouseID = person.getSpouseID();
        List<Event> eventsSpouse = eventListForPerson.get(spouseID);
        assert eventsSpouse != null;
        maternalEvents.addAll(eventsSpouse);
        paternalEvents.addAll(eventsSpouse);
    }

    public void setMaternalEvents(String personID, int counter) {
        Person person = people.get(personID);
        assert person != null;
        if (counter > 0) {
            if (person.getFatherID() != null) {
                String fatherID = Objects.requireNonNull(people.get(personID)).getFatherID();
                List<Event> fatherEvents = eventListForPerson.get(fatherID);
                if (fatherEvents != null) {
                    if (fatherEvents.size() > 0) {
                        maternalEvents.addAll(fatherEvents);
                        setMaternalEvents(fatherID, counter + 1);
                    }
                }
            }
        }
        if (person.getMotherID() != null) {
            String motherID = Objects.requireNonNull(people.get(personID)).getMotherID();
            List<Event> motherEvents = eventListForPerson.get(motherID);
            if (motherEvents != null) {
                if (motherEvents.size() > 0) {
                    maternalEvents.addAll(motherEvents);
                    setMaternalEvents(motherID, counter + 1);
                }
            }
        }
    }

    public void setPaternalEvents(String personID, int counter) {
        Person person = people.get(personID);
        assert person != null;
        if (person.getFatherID() != null) {
            String fatherID = Objects.requireNonNull(people.get(personID)).getFatherID();
            List<Event> fatherEvents = eventListForPerson.get(fatherID);
            if (fatherEvents != null) {
                if (fatherEvents.size() > 0) {
                    paternalEvents.addAll(fatherEvents);
                    setPaternalEvents(fatherID, counter + 1);
                }
            }
        }
        if (counter > 0) {
            if (person.getMotherID() != null) {
                String motherID = Objects.requireNonNull(people.get(personID)).getMotherID();
                List<Event> motherEvents = eventListForPerson.get(motherID);
                if (motherEvents != null) {
                    if (motherEvents.size() > 0) {
                        paternalEvents.addAll(motherEvents);
                        setPaternalEvents(motherID, counter + 1);
                    }
                }
            }
        }
    }

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

    public String getRootPersonID() {
        return rootPersonID;
    }

    public void setRootPersonID(String rootPersonID) {
        this.rootPersonID = rootPersonID;
    }

    public boolean isLifeStoryLines() {
        return lifeStoryLines;
    }

    public void setLifeStoryLines(boolean lifeStoryLines) {
        this.lifeStoryLines = lifeStoryLines;
    }

    public boolean isFamilyTreeLines() {
        return familyTreeLines;
    }

    public void setFamilyTreeLines(boolean familyTreeLines) {
        this.familyTreeLines = familyTreeLines;
    }

    public boolean isSpouseLines() {
        return spouseLines;
    }

    public void setSpouseLines(boolean spouseLines) {
        this.spouseLines = spouseLines;
    }

    public boolean isFatherSide() {
        return fatherSide;
    }

    public void setFatherSide(boolean fatherSide) {
        this.fatherSide = fatherSide;
    }

    public boolean isMotherSide() {
        return motherSide;
    }

    public void setMotherSide(boolean motherSide) {
        this.motherSide = motherSide;
    }

    public boolean isMaleEvents() {
        return maleEvents;
    }

    public void setMaleEvents(boolean maleEvents) {
        this.maleEvents = maleEvents;
    }

    public boolean isFemaleEvents() {
        return femaleEvents;
    }

    public void setFemaleEvents(boolean femaleEvents) {
        this.femaleEvents = femaleEvents;
    }

    public void setSettingsTrue() {
        setFatherSide(true);
        setMotherSide(true);
        setMaleEvents(true);
        setFemaleEvents(true);
        setLifeStoryLines(true);
        setSpouseLines(true);
        setFamilyTreeLines(true);
    }

    public void clear() {
        setSettingsTrue();
        people.clear();
        events.clear();
        personList.clear();
        eventList.clear();
        peopleListForPerson.clear();
        eventListForPerson.clear();
        rootPersonID = null;
    }

}
