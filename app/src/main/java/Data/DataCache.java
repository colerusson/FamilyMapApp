package Data;

import android.app.Person;
import android.media.metrics.Event;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataCache {

    private static DataCache instance = new DataCache();

    public static DataCache getInstance() {
        return instance;
    }

    private DataCache() {
    }

    Map<String, Person> people;
    Map<String, Event> events;
    Map<String, List<Event>> personEvents;
    Set<String> paternalAncestors;
    Set<String> maternalAncestors;

    // TODO: write the necessary setters so that they are correctly added to maps

    Person getPersonById(String id) {
        return people.get(id);
    }

    Event getEventById(String id) {
        return events.get(id);
    }

    List<Event> getPersonEvents(String id)  {
        return personEvents.get(id);
    }

}
