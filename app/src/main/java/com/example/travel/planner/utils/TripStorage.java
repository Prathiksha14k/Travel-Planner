package com.example.travel.planner.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.travel.planner.models.Trip;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TripStorage {
    private static final String PREF_NAME = "TravelPlannerPrefs";
    private static final String KEY_TRIPS = "trips";
    private static TripStorage instance;
    private final SharedPreferences prefs;
    private final Gson gson;

    private TripStorage(Context ctx) {
        prefs = ctx.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static synchronized TripStorage getInstance(Context ctx) {
        if (instance == null) instance = new TripStorage(ctx);
        return instance;
    }

    public List<Trip> getAllTrips() {
        String json = prefs.getString(KEY_TRIPS, "[]");
        Type type = new TypeToken<List<Trip>>(){}.getType();
        List<Trip> trips = gson.fromJson(json, type);
        return trips != null ? trips : new ArrayList<>();
    }

    public void saveTrip(Trip trip) {
        List<Trip> trips = getAllTrips();
        for (int i = 0; i < trips.size(); i++) {
            if (trips.get(i).getId().equals(trip.getId())) {
                trips.set(i, trip);
                persist(trips);
                return;
            }
        }
        trips.add(0, trip);
        persist(trips);
    }

    public void deleteTrip(String tripId) {
        List<Trip> trips = getAllTrips();
        trips.removeIf(t -> t.getId().equals(tripId));
        persist(trips);
    }

    public Trip getTripById(String id) {
        for (Trip t : getAllTrips())
            if (t.getId().equals(id)) return t;
        return null;
    }

    private void persist(List<Trip> trips) {
        prefs.edit().putString(KEY_TRIPS, gson.toJson(trips)).apply();
    }
}