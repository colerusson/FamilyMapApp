package com.example.familymapapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        Intent intent = getIntent();
        String personID = intent.getStringExtra(PERSON_ID);
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
            TextView iconView = eventView.findViewById(R.id.iconEvent);
            iconView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_location_on_24, 0, 0, 0);

            TextView eventName = eventView.findViewById(R.id.eventName);
            eventName.setText(getString(R.string.eventDetails, events.get(childPosition).getEventType(), events.get(childPosition).getCity(),
                    events.get(childPosition).getCountry(), events.get(childPosition).getYear()));

            TextView eventPerson = eventView.findViewById(R.id.eventPerson);
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
            personRelationship.setText("Add Relationship");

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

}