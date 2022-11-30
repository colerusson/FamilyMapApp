package com.example.familymapapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import Data.DataCache;
import model.Event;
import model.Person;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {

    private GoogleMap map;
    static private final DataCache cache = DataCache.getInstance();
    private final Map<String, Float> unknownEvents = new HashMap<>();
    private final Map<Float, Boolean> usedColors = new HashMap<>();
    private final List<Polyline> polyLines = new ArrayList<>();

    private TextView topText;
    private TextView bottomText;
    private TextView icon;
    private Event currentEvent;
    private Event selectedEvent;

    public MapFragment() {
        // Required empty public constructor
    }

    public MapFragment(Event event) {
        // Use this constructor when making a class from the event activity
        selectedEvent = event;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        topText = view.findViewById(R.id.topText);
        bottomText = view.findViewById(R.id.bottomText);
        icon = view.findViewById(R.id.icon);

        if (selectedEvent == null) {
            topText.setText(getResources().getString(R.string.click_on_a_marker_to_see_event));
            bottomText.setText(getResources().getString(R.string.details));
            icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_android_24, 0, 0, 0);
        }
        else {
            String firstName = Objects.requireNonNull(cache.getPeople().get(selectedEvent.getPersonID())).getFirstName();
            String lastName = Objects.requireNonNull(cache.getPeople().get(selectedEvent.getPersonID())).getLastName();
            String eventType = selectedEvent.getEventType();
            String city = selectedEvent.getCity();
            String country = selectedEvent.getCountry();
            int year = selectedEvent.getYear();
            topText.setText(getString(R.string.eventUser, firstName, lastName));
            bottomText.setText(getString(R.string.eventDetails, eventType, city, country, year));
            String gender = Objects.requireNonNull(cache.getPeople().get(selectedEvent.getPersonID())).getGender();
            if (Objects.equals(gender, "f")) {
                icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_woman_24, 0, 0, 0);
            }
            else if (Objects.equals(gender, "m")) {
                icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_man_24, 0, 0, 0);
            }
        }

        LinearLayout layout = view.findViewById(R.id.eventLayout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentEvent != null) {
                    personSelected(currentEvent);
                }
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLoadedCallback(this);
        setUsedColors();

        Marker marker;

        for (String eventString : cache.getEvents().keySet()) {
            Event event = cache.getEvents().get(eventString);
            assert event != null;
            LatLng newCity = new LatLng(event.getLatitude(), event.getLongitude());
            float googleColor;
            if (Objects.equals(event.getEventType().toLowerCase(), "birth")) {
                googleColor = BitmapDescriptorFactory.HUE_RED;
            }
            else if (Objects.equals(event.getEventType().toLowerCase(), "death")) {
                googleColor = BitmapDescriptorFactory.HUE_BLUE;

            }
            else if (Objects.equals(event.getEventType().toLowerCase(), "marriage")) {
                googleColor = BitmapDescriptorFactory.HUE_GREEN;

            }
            else {
                if (unknownEvents.containsKey(event.getEventType().toLowerCase())) {
                    googleColor = unknownEvents.get(event.getEventType());
                }
                else {
                    Float colorType = BitmapDescriptorFactory.HUE_RED;
                    for (Float color : usedColors.keySet()) {
                        if (Boolean.FALSE.equals(usedColors.get(color))) {
                            colorType = color;
                            setColorUsed(color);
                            break;
                        }
                    }
                    if (colorType == BitmapDescriptorFactory.HUE_RED) {
                        colorType = BitmapDescriptorFactory.HUE_AZURE;
                    }
                    unknownEvents.put(event.getEventType().toLowerCase(), colorType);
                    googleColor = colorType;
                }
            }
            marker = map.addMarker(new MarkerOptions().position(newCity).title(event.getCity() + ", "
                    + event.getCountry()).icon(BitmapDescriptorFactory.defaultMarker(googleColor)));
            assert marker != null;

            marker.setTag(event);

            if (selectedEvent != null) {
                LatLng latLng = new LatLng(selectedEvent.getLatitude(), selectedEvent.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                currentEvent = (Event) marker.getTag();
                for (Polyline line : polyLines) {
                    line.remove();
                }
                polyLines.clear();
                eventInfo((Event) Objects.requireNonNull(marker.getTag()));
                return false;
            }
        });

    }

    private void eventInfo(Event event) {
        String firstName = Objects.requireNonNull(cache.getPeople().get(event.getPersonID())).getFirstName();
        String lastName = Objects.requireNonNull(cache.getPeople().get(event.getPersonID())).getLastName();
        String eventType = event.getEventType();
        String city = event.getCity();
        String country = event.getCountry();
        String personID = event.getPersonID();
        int year = event.getYear();

        if (eventType.equals("death")) { eventType = "DEATH"; }
        if (eventType.equals("birth")) { eventType = "BIRTH"; }
        if (eventType.equals("marriage")) { eventType = "MARRIAGE"; }

        topText.setText(getString(R.string.eventUser, firstName, lastName));
        bottomText.setText(getString(R.string.eventDetails, eventType, city, country, year));
        String gender = Objects.requireNonNull(cache.getPeople().get(event.getPersonID())).getGender();
        if (Objects.equals(gender, "f")) {
            icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_woman_24, 0, 0, 0);
        }
        else if (Objects.equals(gender, "m")) {
            icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_man_24, 0, 0, 0);
        }

        makeChronologyLines(personID);
        makeSpouseLines(personID, event);
        makeFamilyLines(personID, event, 12);
    }

    private void makeChronologyLines(String personID) {
        List<Event> events = cache.getEventListForPerson(personID);
        if (events != null && events.size() > 0) {
            sortEvents(events);
        }
        for (int i = 0; i < Objects.requireNonNull(events).size() - 1; ++i) {
            Event startEvent = events.get(i);
            Event endEvent = events.get(i + 1);
            addLine(startEvent, endEvent, Color.BLUE, 10);
        }
    }

    private void makeSpouseLines(String personID, Event rootEvent) {
        String spouseID = Objects.requireNonNull(cache.getPeople().get(personID)).getSpouseID();
        if (spouseID != null) {
            Person spouse = cache.getPeople().get(spouseID);
            assert spouse != null;
            List<Event> spouseEvents = cache.getEventListForPerson(spouse.getPersonID());
            if (spouseEvents != null) {
                if (spouseEvents.size() > 0) {
                    Event firstEvent = getFirstEvent(spouseEvents);
                    if (firstEvent != null) {
                        addLine(rootEvent, firstEvent, Color.RED, 10);
                    }
                }
            }
        }
    }

    private void makeFamilyLines(String personID, Event rootEvent, float width) {
        float divisor = 2;
        if (Objects.requireNonNull(cache.getPeople().get(personID)).getFatherID() != null) {
            String fatherID = Objects.requireNonNull(cache.getPeople().get(personID)).getFatherID();
            List<Event> fatherEvents = cache.getEventListForPerson(fatherID);
            if (fatherEvents != null) {
                if (fatherEvents.size() > 0) {
                    Event fatherEvent = getFirstEvent(fatherEvents);
                    if (fatherEvent != null) {
                        addLine(rootEvent, fatherEvent, Color.GREEN, width);
                        makeFamilyLines(fatherID, fatherEvent, width / divisor);
                    }
                }
            }
        }
        if (Objects.requireNonNull(cache.getPeople().get(personID)).getMotherID() != null) {
            String motherID = Objects.requireNonNull(cache.getPeople().get(personID)).getMotherID();
            List<Event> motherEvents = cache.getEventListForPerson(motherID);
            if (motherEvents != null) {
                if (motherEvents.size() > 0) {
                    Event motherEvent = getFirstEvent(motherEvents);
                    if (motherEvent != null) {
                        addLine(rootEvent, motherEvent, Color.GREEN, width);
                        makeFamilyLines(motherID, motherEvent, width / divisor);
                    }
                }
            }
        }
    }

    private void addLine(Event startEvent, Event endEvent, int googleColor, float thickness) {
        LatLng startCity = new LatLng(startEvent.getLatitude(), startEvent.getLongitude());
        LatLng endCity = new LatLng(endEvent.getLatitude(), endEvent.getLongitude());

        PolylineOptions options = new PolylineOptions()
                .add(startCity)
                .add(endCity)
                .color(googleColor)
                .width(thickness);
        polyLines.add(this.map.addPolyline(options));
    }

    private Event getFirstEvent(List<Event> events) {
        Event firstEvent = null;
        int year = 2022;
        for (Event event : events) {
            if (event.getYear() < year) {
                year = event.getYear();
                firstEvent = event;
            }
        }
        return firstEvent;
    }

    private void sortEvents(List<Event> events) {
        for (int i = 0; i < events.size() - 1; ++i) {
            if (events.get(i).getYear() > events.get(i + 1).getYear()) {
                Event temp = events.get(i);
                events.set(i, events.get(i + 1));
                events.set(i + 1, temp);
            }
            else if (Objects.equals(events.get(i).getYear(), events.get(i + 1).getYear())) {
                String event1 = events.get(i).getEventType().toLowerCase();
                String event2 = events.get(i + 1).getEventType().toLowerCase();
                if (event1.compareTo(event2) > 0) {
                    Event temp = events.get(i);
                    events.set(i, events.get(i + 1));
                    events.set(i + 1, temp);
                }
            }
        }
    }

    private void personSelected(Event event) {
        Intent intent = new Intent(getActivity(), PersonActivity.class);
        intent.putExtra(PersonActivity.PERSON_ID, event.getPersonID());
        startActivity(intent);
    }

    private void setColorUsed(Float color) {
        usedColors.put(color, true);
    }

    private void setUsedColors() {
        usedColors.put(BitmapDescriptorFactory.HUE_AZURE, false);
        usedColors.put(BitmapDescriptorFactory.HUE_ORANGE, false);
        usedColors.put(BitmapDescriptorFactory.HUE_CYAN, false);
        usedColors.put(BitmapDescriptorFactory.HUE_MAGENTA, false);
        usedColors.put(BitmapDescriptorFactory.HUE_ROSE, false);
        usedColors.put(BitmapDescriptorFactory.HUE_VIOLET, false);
        usedColors.put(BitmapDescriptorFactory.HUE_YELLOW, false);
    }

    @Override
    public void onMapLoaded() {
        // probably won't need this
    }

}