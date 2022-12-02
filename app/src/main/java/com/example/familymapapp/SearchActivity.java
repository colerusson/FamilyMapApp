package com.example.familymapapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import Data.DataCache;
import model.Event;
import model.Person;

public class SearchActivity extends AppCompatActivity {

    static private final DataCache cache = DataCache.getInstance();

    private static final int EVENT_ITEM_VIEW_TYPE = 0;
    private static final int PERSON_ITEM_VIEW_TYPE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        EditText userInput = findViewById(R.id.searchText);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        userInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputString = userInput.getText().toString();
                if (!inputString.equals("")) {
                    List<Event> eventList = cache.getSearchedEventList(inputString.toLowerCase());
                    List<Person> peopleList = cache.getSearchedPersonList(inputString.toLowerCase());

                    SearchResultsAdapter adapter = new SearchResultsAdapter(eventList, peopleList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsViewHolder> {
        private final List<Event> events;
        private final List<Person> people;

        SearchResultsAdapter(List<Event> eventList, List<Person> personList) {
            this.events = eventList;
            this.people = personList;
        }

        @Override
        public int getItemViewType(int position) {
            return position < events.size() ? EVENT_ITEM_VIEW_TYPE : PERSON_ITEM_VIEW_TYPE;
        }

        @NonNull
        @Override
        public SearchResultsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            if (viewType == EVENT_ITEM_VIEW_TYPE) {
                view = getLayoutInflater().inflate(R.layout.event_item, parent, false);
            }
            else {
                view = getLayoutInflater().inflate(R.layout.person_item, parent, false);
            }

            return new SearchResultsViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchResultsViewHolder holder, int position) {
            if (position < events.size()) {
                holder.bind(events.get(position));
            }
            else {
                holder.bind(people.get(position - events.size()));
            }
        }

        @Override
        public int getItemCount() {
            return events.size() + people.size();
        }

    }

    private class SearchResultsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView iconView;
        private final TextView eventOrPerson;
        private final TextView personOrGender;

        private final int viewType;
        private Event event;
        private Person person;

        SearchResultsViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            if (viewType == EVENT_ITEM_VIEW_TYPE) {
                iconView = itemView.findViewById(R.id.iconEvent);
                eventOrPerson = itemView.findViewById(R.id.eventName);
                personOrGender = itemView.findViewById(R.id.eventPerson);
            }
            else {
                iconView = itemView.findViewById(R.id.iconPerson);
                eventOrPerson = itemView.findViewById(R.id.personName);
                personOrGender = itemView.findViewById(R.id.personRelationship);
            }
        }

        private void bind(Event event) {
            this.event = event;
            iconView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_location_on_24, 0, 0, 0);
            String eventType = event.getEventType();
            String personID = event.getPersonID();
            String eventPersonGender = Objects.requireNonNull(cache.getPeople().get(personID)).getGender();
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
                    eventOrPerson.setText(getString(R.string.eventDetails, eventType, event.getCity(),
                            event.getCountry(), event.getYear()));

                    personOrGender.setText(getString(R.string.eventUser, Objects.requireNonNull(cache.getPeople().get(event.getPersonID())).getFirstName(),
                            Objects.requireNonNull(cache.getPeople().get(event.getPersonID())).getLastName()));
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
                    eventOrPerson.setText(getString(R.string.eventDetails, eventType, event.getCity(),
                            event.getCountry(), event.getYear()));

                    personOrGender.setText(getString(R.string.eventUser, Objects.requireNonNull(cache.getPeople().get(event.getPersonID())).getFirstName(),
                            Objects.requireNonNull(cache.getPeople().get(event.getPersonID())).getLastName()));
                }
            }
        }

        private void bind(Person person) {
            this.person = person;
            if (Objects.equals(person.getGender(), "f")) {
                iconView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_woman_24, 0, 0, 0);
            }
            else if (Objects.equals(person.getGender(), "m")) {
                iconView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_man_24, 0, 0, 0);
            }

            eventOrPerson.setText(getString(R.string.eventUser, person.getFirstName(), person.getLastName()));

            if (Objects.equals(person.getGender(), "f")) {
                personOrGender.setText(getString(R.string.radioButtonFemale));
            }
            else if (Objects.equals(person.getGender(), "m")) {
                personOrGender.setText(getString(R.string.radioButtonMale));
            }
        }

        @Override
        public void onClick(View view) {
            if (viewType == EVENT_ITEM_VIEW_TYPE) {
                Toast.makeText(SearchActivity.this,  "You selected an event!", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(SearchActivity.this,  "You selected a person!", Toast.LENGTH_LONG).show();
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