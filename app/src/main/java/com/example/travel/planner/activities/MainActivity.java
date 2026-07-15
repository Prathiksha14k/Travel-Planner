package com.example.travel.planner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.example.travel.planner.R;
import com.example.travel.planner.adapters.TripAdapter;
import com.example.travel.planner.models.Trip;
import com.example.travel.planner.utils.TripStorage;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements TripAdapter.OnTripClickListener {

    private RecyclerView rvTrips;
    private TripAdapter adapter;
    private View layoutEmpty;
    private TextView tvTotalTrips, tvTotalDestinations;
    private TripStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storage = TripStorage.getInstance(this);
        rvTrips              = findViewById(R.id.rv_trips);
        layoutEmpty          = findViewById(R.id.layout_empty);
        tvTotalTrips         = findViewById(R.id.tv_total_trips);
        tvTotalDestinations  = findViewById(R.id.tv_total_destinations);

        ExtendedFloatingActionButton fab = findViewById(R.id.fab_create_trip);
        fab.setOnClickListener(v ->
                startActivity(new Intent(this, CreateTripActivity.class)));

        // Empty state button
        findViewById(R.id.btn_empty_create).setOnClickListener(v ->
                startActivity(new Intent(this, CreateTripActivity.class)));

        rvTrips.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TripAdapter(this, this);
        rvTrips.setAdapter(adapter);

        rvTrips.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrolled(RecyclerView rv, int dx, int dy) {
                if (dy > 0) fab.shrink();
                else fab.extend();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTrips();
    }

    private void loadTrips() {
        List<Trip> trips = storage.getAllTrips();
        adapter.setTrips(trips);

        // Update stats
        int totalDest = 0;
        for (Trip t : trips) totalDest += t.getDestinations().size();
        tvTotalTrips.setText(String.valueOf(trips.size()));
        tvTotalDestinations.setText(String.valueOf(totalDest));

        boolean empty = trips.isEmpty();
        layoutEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
        rvTrips.setVisibility(empty ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onTripClick(Trip trip) {
        Intent intent = new Intent(this, TripDetailActivity.class);
        intent.putExtra("trip_id", trip.getId());
        startActivity(intent);
    }

    @Override
    public void onTripDelete(Trip trip) {
        storage.deleteTrip(trip.getId());
        loadTrips();
        Snackbar.make(rvTrips, "Trip deleted", Snackbar.LENGTH_SHORT).show();
    }
}