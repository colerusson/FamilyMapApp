package com.example.familymapapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import Data.DataCache;
import model.Event;
import model.Person;


public class PersonActivity extends AppCompatActivity {

    static private final DataCache cache = DataCache.getInstance();
    public static final String PERSON_ID = "personID";
    private String rootPersonID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        Intent intent = getIntent();
        String personID = intent.getStringExtra(PERSON_ID);
        rootPersonID = personID;
        Person person = cache.getPeople().get(personID);
        assert person != null;
        String personFirstName = person.getFirstName();
        String personLastName = person.getLastName();
        String personGender = person.getGender();
        if (personGender.equals("f")) {
            personGender = "Female";
        }
        else if (personGender.equals("m")) {
            personGender = "Male";
        }

        List<Person> family = cache.getPeopleListForPerson(personID);
        List<Event> events = cache.getEventListForPerson(personID);

        sortEvents(events);

        TextView firstName = findViewById(R.id.firstNameLine);
        TextView lastName = findViewById(R.id.lastNameLine);
        TextView gender = findViewById(R.id.genderLine);

        firstName.setText(personFirstName);
        lastName.setText(personLastName);
        gender.setText(personGender);

        ExpandableListView expandableListView = findViewById(R.id.expandableListView);
        expandableListView.setAdapter(new ExpandableListAdapter(events, family));
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        private static final int EVENTS_POSITION = 0;
        private static final int FAMILY_POSITION = 1;

        private final List<Event> events;
        private final List<Person> family;

        private ExpandableListAdapter(List<Event> events, List<Person> family) {
            this.events = events;
            this.family = family;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch(groupPosition) {
                case EVENTS_POSITION:
                    return events.size();
                case FAMILY_POSITION:
                    return family.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch(groupPosition) {
                case EVENTS_POSITION:
                    return getString(R.string.eventTitle);
                case FAMILY_POSITION:
                    return getString(R.string.familyTitle);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch(groupPosition) {
                case EVENTS_POSITION:
                    return events.get(childPosition);
                case FAMILY_POSITION:
                    return family.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch(groupPosition) {
                case EVENTS_POSITION:
                    titleView.setText(R.string.eventTitle);
                    break;
                case FAMILY_POSITION:
                    titleView.setText(R.string.familyTitle);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            switch(groupPosition) {
                case EVENTS_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.event_item, parent, false);
                    initializeEventView(itemView, childPosition);
                    break;
                case FAMILY_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.person_item, parent, false);
                    initializePersonView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
            return itemView;
        }

        private void initializeEventView(View eventView, final int childPosition) {
            String eventType = events.get(childPosition).getEventType();
            String personID = events.get(childPosition).getPersonID();
            String eventPersonGender = Objects.requireNonNull(cache.getPeople().get(personID)).getGender();

            TextView iconView = eventView.findViewById(R.id.iconEvent);
            TextView eventName = eventView.findViewById(R.id.eventName);
            TextView eventPerson = eventView.findViewById(R.id.eventPerson);

            if (eventPersonGender.equals("f")) {
                if (cache.isFemaleEvents()) {
                    iconView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_location_on_24, 0, 0, 0);

                    if (eventType.equalsIgnoreCase("birth")) {
                        eventType = "BIRTH";
                    }
                    else if (eventType.equalsIgnoreCase("death")) {
                        eventType = "DEATH";
                    }
                    else if (eventType.equalsIgnoreCase("marriage")) {
                        eventType = "MARRIAGE";
                    }
                    eventName.setText(getString(R.string.eventDetails, eventType, events.get(childPosition).getCity(),
                            events.get(childPosition).getCountry(), events.get(childPosition).getYear()));

                    eventPerson.setText(getString(R.string.eventUser, Objects.requireNonNull(cache.getPeople().get(events.get(childPosition).getPersonID())).getFirstName(),
                            Objects.requireNonNull(cache.getPeople().get(events.get(childPosition).getPersonID())).getLastName()));

                    eventView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                            intent.putExtra(EventActivity.EVENT_ID, events.get(childPosition).getEventID());
                            startActivity(intent);
                        }
                    });
                }
            }
            else if (eventPersonGender.equals("m")) {
                if (cache.isMaleEvents()) {
                    iconView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_location_on_24, 0, 0, 0);

                    if (eventType.equalsIgnoreCase("birth")) {
                        eventType = "BIRTH";
                    }
                    else if (eventType.equalsIgnoreCase("death")) {
                        eventType = "DEATH";
                    }
                    else if (eventType.equalsIgnoreCase("marriage")) {
                        eventType = "MARRIAGE";
                    }
                    eventName.setText(getString(R.string.eventDetails, eventType, events.get(childPosition).getCity(),
                            events.get(childPosition).getCountry(), events.get(childPosition).getYear()));

                    eventPerson.setText(getString(R.string.eventUser, Objects.requireNonNull(cache.getPeople().get(events.get(childPosition).getPersonID())).getFirstName(),
                            Objects.requireNonNull(cache.getPeople().get(events.get(childPosition).getPersonID())).getLastName()));

                    eventView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                            intent.putExtra(EventActivity.EVENT_ID, events.get(childPosition).getEventID());
                            startActivity(intent);
                        }
                    });
                }
            }

        }

        private void initializePersonView(View personView, final int childPosition) {
            TextView iconView = personView.findViewById(R.id.iconPerson);
            if (Objects.equals(family.get(childPosition).getGender(), "f")) {
                iconView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_woman_24, 0, 0, 0);
            }
            else if (Objects.equals(family.get(childPosition).getGender(), "m")) {
                iconView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_man_24, 0, 0, 0);
            }

            TextView personName = personView.findViewById(R.id.personName);
            personName.setText(getString(R.string.eventUser, family.get(childPosition).getFirstName(), family.get(childPosition).getLastName()));

            TextView personRelationship = personView.findViewById(R.id.personRelationship);
            String relationship = family.get(childPosition).getPersonID();
            personRelationship.setText(getRelationshipType(relationship));

            personView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                    intent.putExtra(PersonActivity.PERSON_ID, family.get(childPosition).getPersonID());
                    startActivity(intent);
                }
            });
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    private String getRelationshipType(String relationshipPerson) {
        String relationship = null;
        Person rootPerson = cache.getPeople().get(rootPersonID);
        assert rootPerson != null;
        if (rootPerson.getMotherID() != null) {
            if (rootPerson.getMotherID().equals(relationshipPerson)) {
                relationship = "Mother";
                return relationship;
            }
        }
        if (rootPerson.getFatherID() != null) {
            if (rootPerson.getFatherID().equals(relationshipPerson)) {
                relationship = "Father";
                return relationship;
            }
        }
        if (rootPerson.getSpouseID() != null) {
            if (rootPerson.getSpouseID().equals(relationshipPerson)) {
                relationship = "Spouse";
                return relationship;
            }
        }
        relationship = "Child";
        return relationship;
    }

    private void sortEvents(List<Event> events) {
        for (int i = 0; i < events.size() - 1; ++i) {
            Event firstEvent = events.get(i);
            Event secondEvent = events.get(i + 1);
            if (firstEvent.getYear() > secondEvent.getYear()) {
                events.set(i, secondEvent);
                events.set(i + 1, firstEvent);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }


}