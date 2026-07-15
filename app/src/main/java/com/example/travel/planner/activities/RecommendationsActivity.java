package com.example.travel.planner.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travel.planner.R;
import com.example.travel.planner.adapters.RecommendationAdapter;
import com.example.travel.planner.models.Destination;
import com.example.travel.planner.models.Recommendation;
import com.example.travel.planner.models.Trip;
import com.example.travel.planner.utils.RecommendationFallback;
import com.example.travel.planner.utils.TripStorage;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;

public class RecommendationsActivity extends AppCompatActivity {

    private Trip trip;
    private TripStorage storage;
    private RecyclerView rvRecommendations;
    private RecommendationAdapter adapter;
    private final List<Recommendation> allRecommendations = new ArrayList<>();
    private final List<Recommendation> filteredList = new ArrayList<>();
    private ProgressBar progressBar;

    private TextView tvStatus, tvDestName, tvEmptyMsg;
    private ChipGroup chipGroupFilter;
    private LinearLayout layoutEmpty;
    private Spinner spinnerDestination;
    private int selectedDestIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);

        storage = TripStorage.getInstance(this);
        String tripId = getIntent().getStringExtra("trip_id");
        trip = storage.getTripById(tripId);



        initViews();
        setupRecyclerView();
        setupFilterChips();
        setupDestinationSpinner();
    }

    private void initViews() {
        tvDestName        = findViewById(R.id.tv_rec_dest_name);
        tvStatus          = findViewById(R.id.tv_rec_status);
        tvEmptyMsg        = findViewById(R.id.tv_empty_rec_msg);
        progressBar       = findViewById(R.id.progress_recommendations);
        rvRecommendations = findViewById(R.id.rv_recommendations);
        chipGroupFilter   = findViewById(R.id.chip_group_filter);
        layoutEmpty       = findViewById(R.id.layout_rec_empty);
        spinnerDestination= findViewById(R.id.spinner_destination);

        findViewById(R.id.btn_back_rec)
                .setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        adapter = new RecommendationAdapter(
                this, filteredList, rec -> addToSchedule(rec));
        rvRecommendations.setLayoutManager(
                new LinearLayoutManager(this));
        rvRecommendations.setAdapter(adapter);
    }

    private void setupFilterChips() {
        String[] filters = {
                "All", "Tourist", "Food", "Hotel"
        };
        String[] labels = {
                "🗺️ All", "🏛️ Tourist", "🍽️ Food", "🏨 Hotel"
        };

        chipGroupFilter.removeAllViews();

        for (int i = 0; i < filters.length; i++) {
            Chip chip = new Chip(this);
            chip.setText(labels[i]);
            chip.setTag(filters[i]); // store simple tag
            chip.setCheckable(true);
            chip.setCheckedIconVisible(false);
            if (i == 0) chip.setChecked(true);
            chipGroupFilter.addView(chip);
        }

        chipGroupFilter.setOnCheckedStateChangeListener(
                (group, checkedIds) -> {
                    if (checkedIds.isEmpty()) {
                        ((Chip) group.getChildAt(0)).setChecked(true);
                        return;
                    }
                    Chip selected = group.findViewById(checkedIds.get(0));
                    if (selected != null) {
                        // ✅ use tag not text for filtering
                        filterRecommendations(
                                selected.getTag().toString());
                    }
                });
    }

    private void setupDestinationSpinner() {
        if (trip.getDestinations().isEmpty()) {
            showEmpty("Add destinations to your trip first!");
            return;
        }

        List<String> names = new ArrayList<>();
        for (Destination d : trip.getDestinations())
            names.add(d.getName());

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                names);
        spinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinnerDestination.setAdapter(spinnerAdapter);

        spinnerDestination.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> p,
                                               View v, int pos, long id) {
                        selectedDestIndex = pos;
                        loadForDestination(
                                trip.getDestinations().get(pos));
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> p) {}
                });
    }

    private void loadForDestination(Destination dest) {
        // Show loading state
        showLoading("Loading recommendations for "
                + dest.getName() + "...");
        tvDestName.setText("📍 " + dest.getName());

        // ✅ ALWAYS load fallback first — instant, no network
        List<Recommendation> fallback =
                RecommendationFallback.getFor(dest.getName());

        if (!fallback.isEmpty()) {
            // Show fallback immediately
            runOnUiThread(() -> {
                allRecommendations.clear();
                allRecommendations.addAll(fallback);
                filterRecommendations("All");
                hideLoading();
            });
        }

        // Then try API in background to get real results
        // If API works, update the list
        tryLoadFromApi(dest);
    }

    private void tryLoadFromApi(Destination dest) {
        double lat = dest.getLatitude();
        double lng = dest.getLongitude();

        // Skip API if no coordinates
        if (lat == 0 && lng == 0) return;

        new Thread(() -> {
            try {
                List<Recommendation> apiResults =
                        new ArrayList<>();
                apiResults.addAll(fetchCategory(lat, lng,
                        "tourism", "attraction", "🏛️ Tourist Spot"));
                apiResults.addAll(fetchCategory(lat, lng,
                        "amenity", "restaurant", "🍽️ Restaurant"));
                apiResults.addAll(fetchCategory(lat, lng,
                        "tourism", "hotel", "🏨 Hotel"));

                if (!apiResults.isEmpty()) {
                    // Sort by distance
                    for (Recommendation r : apiResults)
                        r.setDistance(distance(lat, lng,
                                r.getLatitude(), r.getLongitude()));
                    apiResults.sort((a, b) ->
                            Double.compare(
                                    a.getDistance(), b.getDistance()));

                    runOnUiThread(() -> {
                        allRecommendations.clear();
                        allRecommendations.addAll(
                                apiResults.subList(0,
                                        Math.min(30, apiResults.size())));
                        filterRecommendations("All");
                    });
                }
            } catch (Exception e) {
                // API failed — fallback already showing, no action needed
            }
        }).start();
    }

    private List<Recommendation> fetchCategory(
            double lat, double lng,
            String key, String value,
            String label) throws Exception {
        List<Recommendation> list = new ArrayList<>();
        String query =
                "[out:json][timeout:8];" +
                        "node[\"" + key + "\"=\"" + value + "\"]" +
                        "(around:5000," + lat + "," + lng + ");" +
                        "out body 8;";
        String urlStr =
                "https://overpass-api.de/api/interpreter?data=" +
                        java.net.URLEncoder.encode(query, "UTF-8");

        java.net.URL url = new java.net.URL(urlStr);
        java.net.HttpURLConnection conn =
                (java.net.HttpURLConnection) url.openConnection();
        conn.setRequestProperty(
                "User-Agent", "TravelPlannerApp/1.0");
        conn.setConnectTimeout(6000);
        conn.setReadTimeout(6000);

        java.io.BufferedReader reader =
                new java.io.BufferedReader(
                        new java.io.InputStreamReader(
                                conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
            sb.append(line);
        reader.close();

        org.json.JSONObject response =
                new org.json.JSONObject(sb.toString());
        org.json.JSONArray elements =
                response.getJSONArray("elements");

        for (int i = 0; i < elements.length(); i++) {
            org.json.JSONObject el =
                    elements.getJSONObject(i);
            org.json.JSONObject tags =
                    el.optJSONObject("tags");
            if (tags == null) continue;
            String name = tags.optString("name", "");
            if (name.isEmpty()) continue;
            double rLat = el.getDouble("lat");
            double rLng = el.getDouble("lon");
            list.add(new Recommendation(
                    name, label, value, rLat, rLng, ""));
        }
        return list;
    }

    // ✅ Clean simple filter using tag
    private void filterRecommendations(String filter) {
        filteredList.clear();

        for (Recommendation r : allRecommendations) {
            switch (filter) {
                case "All":
                    filteredList.add(r);
                    break;
                case "Tourist":
                    if (r.getCategory().contains("Tourist"))
                        filteredList.add(r);
                    break;
                case "Food":
                    if (r.getCategory().contains("Restaurant") ||
                            r.getCategory().contains("Cafe") ||
                            r.getCategory().contains("Food"))
                        filteredList.add(r);
                    break;
                case "Hotel":
                    if (r.getCategory().contains("Hotel"))
                        filteredList.add(r);
                    break;
            }
        }

        adapter.notifyDataSetChanged();

        if (filteredList.isEmpty()) {
            showEmpty("No " + filter +
                    " recommendations found.");
        } else {
            layoutEmpty.setVisibility(View.GONE);
            rvRecommendations.setVisibility(View.VISIBLE);
        }
    }

    private void addToSchedule(Recommendation rec) {
        if (trip.getSchedule() == null || trip.getSchedule().isEmpty()) {
            Snackbar.make(rvRecommendations,
                    "Open Day Planner first to create a schedule!",
                    Snackbar.LENGTH_LONG).show();
            return;
        }

        // Build day options for picker
        String[] dayOptions = new String[trip.getSchedule().size()];
        for (int i = 0; i < trip.getSchedule().size(); i++) {
            dayOptions[i] = trip.getSchedule().get(i).getDayLabel()
                    + " — " + trip.getSchedule().get(i).getDate();
        }

        new android.app.AlertDialog.Builder(this)
                .setTitle("Add to which day?")
                .setItems(dayOptions, (dialog, which) -> {
                    com.example.travel.planner.models.ScheduleItem item =
                            new com.example.travel.planner.models.ScheduleItem(
                                    rec.getName(), rec.getAddress(),
                                    "10:00 AM", rec.getCategory());
                    item.setLatitude(rec.getLatitude());
                    item.setLongitude(rec.getLongitude());

                    trip.getSchedule().get(which).addItem(item); // ← uses selected day
                    storage.saveTrip(trip);

                    Snackbar.make(rvRecommendations,
                                    rec.getName() + " added to " +
                                            trip.getSchedule().get(which).getDayLabel() + "!",
                                    Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(getColor(R.color.colorPrimary))
                            .setTextColor(Color.WHITE)
                            .show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showLoading(String msg) {
        progressBar.setVisibility(View.VISIBLE);
        tvStatus.setVisibility(View.VISIBLE);
        tvStatus.setText(msg);
        layoutEmpty.setVisibility(View.GONE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
        tvStatus.setVisibility(View.GONE);
        rvRecommendations.setVisibility(View.VISIBLE);
    }

    private void showEmpty(String msg) {
        progressBar.setVisibility(View.GONE);
        tvStatus.setVisibility(View.GONE);
        rvRecommendations.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.VISIBLE);
        tvEmptyMsg.setText(msg);
    }

    private double distance(double lat1, double lng1,
                            double lat2, double lng2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) *
                        Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        return R * 2 * Math.atan2(
                Math.sqrt(a), Math.sqrt(1-a));
    }
}