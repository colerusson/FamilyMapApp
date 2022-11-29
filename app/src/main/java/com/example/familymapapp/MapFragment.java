package com.example.familymapapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import Data.DataCache;
import model.Event;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {

    private GoogleMap map;
    static private final DataCache cache = DataCache.getInstance();
    private Map<String, Float> unknownEvents = new HashMap<>();
    private Map<Float, Boolean> usedColors = new HashMap<>();

    public MapFragment() {
        // Required empty public constructor
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

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
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
            LatLng newCity = new LatLng(event.getLatitude(), event.getLongitude());
            float googleColor;
            if (Objects.equals(event.getEventType(), "birth")) {
                googleColor = BitmapDescriptorFactory.HUE_RED;
            }
            else if (Objects.equals(event.getEventType(), "death")) {
                googleColor = BitmapDescriptorFactory.HUE_BLUE;

            }
            else if (Objects.equals(event.getEventType(), "marriage")) {
                googleColor = BitmapDescriptorFactory.HUE_GREEN;

            }
            else {
                if (unknownEvents.containsKey(event.getEventType())) {
                    googleColor = unknownEvents.get(event.getEventType());
                }
                else {
                    Float colorType = BitmapDescriptorFactory.HUE_RED;
                    for (Float color : usedColors.keySet()) {
                        if (Boolean.FALSE.equals(usedColors.get(color))) {
                            colorType = color;
                            setColorUsed(color, true);
                            break;
                        }
                    }
                    if (colorType == BitmapDescriptorFactory.HUE_RED) {
                        colorType = BitmapDescriptorFactory.HUE_AZURE;
                    }
                    unknownEvents.put(event.getEventType(), colorType);
                    googleColor = colorType;
                }
            }
            marker = map.addMarker(new MarkerOptions().position(newCity).title(event.getCity() + ", " + event.getCountry()).icon(BitmapDescriptorFactory.defaultMarker(googleColor)));
            assert marker != null;

            marker.setTag(event);
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                eventInfo((Event) marker.getTag());
                return false;
            }
        });

    }

    @Override
    public void onMapLoaded() {
        // probably won't need this
    }

    private void setColorUsed(Float color, Boolean value) {
        usedColors.put(color, value);
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

    private void eventInfo(Event event) {
        // here I could set up the info for the selected event
        // then check what settings are enabled
        // then call respective functions for each type of line
        // then they each call addLine

        // make sure to clear poly-lines each time

    }

    private void addLine(Event startEvent, Event endEvent, float googleColor, float thickness) {
        LatLng startCity = new LatLng(startEvent.getLatitude(), startEvent.getLongitude());
        LatLng endCity = new LatLng(endEvent.getLatitude(), endEvent.getLongitude());

        PolylineOptions options = new PolylineOptions()
                .add(startCity)
                .add(endCity)
                .color((int) googleColor)
                .width(thickness);
        this.map.addPolyline(options);
    }


    private void makeChronologyLines() {

    }

    private void makeSpouseLines() {

    }

    private void makeFamilyLines() {

    }

    private void setMarkers() {

    }


}