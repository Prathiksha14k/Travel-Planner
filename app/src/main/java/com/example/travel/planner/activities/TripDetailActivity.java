package com.example.travel.planner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.view.View;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travel.planner.R;
import com.example.travel.planner.adapters.DestinationAdapter;
import com.example.travel.planner.models.Destination;
import com.example.travel.planner.models.Trip;
import com.example.travel.planner.utils.TripStorage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class TripDetailActivity extends AppCompatActivity {

    private Trip trip;
    private TripStorage storage;
    private WebView mapView;
    private DestinationAdapter adapter;
    private RecyclerView rvDestinations;

    private final ActivityResultLauncher<Intent> addDestLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(), result -> {
                        if (result.getResultCode() == RESULT_OK
                                && result.getData() != null) {
                            Intent data   = result.getData();
                            String name   = data.getStringExtra("dest_name");
                            String address= data.getStringExtra("dest_address");
                            double lat    = data.getDoubleExtra("dest_lat", 0);
                            double lng    = data.getDoubleExtra("dest_lng", 0);

                            Destination dest =
                                    new Destination(name, address, lat, lng);
                            dest.setDayLabel("Day " +
                                    (trip.getDestinations().size() + 1));
                            trip.addDestination(dest);
                            storage.saveTrip(trip);

                            adapter.notifyItemInserted(
                                    trip.getDestinations().size() - 1);
                            updateDestCount();
                            refreshMap();

                            Snackbar.make(mapView,
                                    name + " added to trip!",
                                    Snackbar.LENGTH_SHORT).show();
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);

        storage = TripStorage.getInstance(this);
        String tripId = getIntent().getStringExtra("trip_id");
        trip = storage.getTripById(tripId);
        if (trip == null) { finish(); return; }

        setupUI();
        setupMap();

        if (getIntent().getBooleanExtra("is_new", false)) {
            Snackbar.make(findViewById(android.R.id.content),
                    "Trip created! Add destinations below.",
                    Snackbar.LENGTH_LONG).show();
        }
    }

    private void setupUI() {
        ((TextView) findViewById(R.id.tv_detail_emoji))
                .setText(trip.getCoverEmoji());
        ((TextView) findViewById(R.id.tv_detail_name))
                .setText(trip.getTripName());
        ((TextView) findViewById(R.id.tv_detail_dates))
                .setText(String.format("%s → %s",
                        trip.getStartDate(), trip.getEndDate()));

        updateDestCount();

        rvDestinations = findViewById(R.id.rv_detail_destinations);
        rvDestinations.setLayoutManager(new LinearLayoutManager(this));
        rvDestinations.setNestedScrollingEnabled(true);

        adapter = new DestinationAdapter(this,
                trip.getDestinations(), pos -> {
            trip.getDestinations().remove(pos);
            storage.saveTrip(trip);
            adapter.notifyItemRemoved(pos);
            for (int i = 0; i < trip.getDestinations().size(); i++)
                trip.getDestinations().get(i)
                        .setDayLabel("Day " + (i + 1));
            adapter.notifyItemRangeChanged(
                    0, trip.getDestinations().size());
            updateDestCount();
            refreshMap();
        });
        rvDestinations.setAdapter(adapter);

        findViewById(R.id.btn_back_detail)
                .setOnClickListener(v -> finish());
        findViewById(R.id.btn_share)
                .setOnClickListener(v -> shareTrip());

        FloatingActionButton fabAdd =
                findViewById(R.id.fab_add_destination);
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this,
                    AddDestinationActivity.class);
            intent.putExtra("trip_id", trip.getId());
            addDestLauncher.launch(intent);
        });

        // Schedule button
        findViewById(R.id.btn_view_schedule).setOnClickListener(v -> {
            Intent intent = new Intent(this, ScheduleActivity.class);
            intent.putExtra("trip_id", trip.getId());
            startActivity(intent);
        });
        findViewById(R.id.btn_recommendations).setOnClickListener(v -> {
            Intent intent = new Intent(this,
                    RecommendationsActivity.class);
            intent.putExtra("trip_id", trip.getId());
            startActivity(intent);
        });
    }

    private void updateDestCount() {
        int count = trip.getDestinations().size();
        ((TextView) findViewById(R.id.tv_detail_dest_count))
                .setText(String.format("%d destinations planned", count));
    }

    private void setupMap() {
        mapView = findViewById(R.id.web_map);
        View mapLoading = findViewById(R.id.map_loading);

        WebSettings settings = mapView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        // ✅ Allow loading resources from https
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        mapView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view,
                                      String url, android.graphics.Bitmap favicon) {
                mapLoading.setVisibility(View.VISIBLE);
                mapView.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                mapLoading.setVisibility(View.GONE);
                mapView.setVisibility(View.VISIBLE);
            }
        });

        refreshMap();
    }

    private void refreshMap() {
        View mapLoading = findViewById(R.id.map_loading);
        mapLoading.setVisibility(View.VISIBLE);
        mapView.setVisibility(View.INVISIBLE);

        // Build destination data for JS
        StringBuilder destData = new StringBuilder();
        double centerLat = 20.5937, centerLng = 78.9629;
        int zoom = 4;
        boolean hasCoords = false;

        for (int i = 0; i < trip.getDestinations().size(); i++) {
            Destination d = trip.getDestinations().get(i);
            if (d.getLatitude() == 0 && d.getLongitude() == 0) continue;
            if (!hasCoords) {
                centerLat = d.getLatitude();
                centerLng = d.getLongitude();
                zoom = 10;
                hasCoords = true;
            }
            destData.append("{")
                    .append("lat:").append(d.getLatitude()).append(",")
                    .append("lng:").append(d.getLongitude()).append(",")
                    .append("name:'")
                    .append(d.getName().replace("'", "\\'")).append("',")
                    .append("day:'")
                    .append(d.getDayLabel().replace("'", "\\'")).append("'")
                    .append("},");
        }

        String html =
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "<meta charset='utf-8'/>" +
                        "<meta name='viewport' content='width=device-width," +
                        "initial-scale=1.0,maximum-scale=1.0,user-scalable=no'/>" +

                        // ✅ Leaflet CSS inline — no external dependency
                        "<style>" +
                        "* { margin:0; padding:0; box-sizing:border-box; }" +
                        "html,body { width:100%; height:100%; overflow:hidden; }" +
                        "#map { width:100%; height:100%; min-height:260px; }" +
                        ".leaflet-container { background:#e8e8e8; }" +
                        "</style>" +

                        // ✅ Load Leaflet from unpkg with fallback
                        "<link rel='stylesheet' " +
                        "href='https://unpkg.com/leaflet@1.9.4/dist/leaflet.css' " +
                        "crossorigin=''/>" +
                        "<script src='https://unpkg.com/leaflet@1.9.4/dist/leaflet.js' " +
                        "crossorigin=''></script>" +

                        "</head>" +
                        "<body>" +
                        "<div id='map'></div>" +
                        "<script>" +
                        "try {" +

                        // Init map
                        "var map = L.map('map', {" +
                        "  zoomControl: true," +
                        "  attributionControl: false" +
                        "}).setView([" + centerLat + "," + centerLng + "]," + zoom + ");" +

                        // ✅ Use multiple tile sources as fallback
                        "var tileLoaded = false;" +

                        // Try CARTO first
                        "var cartoTile = L.tileLayer(" +
                        "  'https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}.png'," +
                        "  {maxZoom:19,crossOrigin:true}" +
                        ");" +

                        // Fallback to OSM
                        "var osmTile = L.tileLayer(" +
                        "  'https://tile.openstreetmap.org/{z}/{x}/{y}.png'," +
                        "  {maxZoom:19,crossOrigin:true}" +
                        ");" +

                        "cartoTile.on('tileerror', function() {" +
                        "  if(!tileLoaded) { map.removeLayer(cartoTile); osmTile.addTo(map); }" +
                        "});" +
                        "cartoTile.on('tileload', function() { tileLoaded = true; });" +
                        "cartoTile.addTo(map);" +

                        // Destination data
                        "var destinations = [" + destData + "];" +

                        // Draw markers and route
                        "var coords = [];" +
                        "var colors = ['#2ECC71','#FF6B35','#E74C3C'," +
                        "  '#3498DB','#9B59B6','#F39C12'];" +

                        "for(var i=0;i<destinations.length;i++){" +
                        "  var d = destinations[i];" +
                        "  var color = i===0 ? '#2ECC71' :" +
                        "    i===destinations.length-1 ? '#E74C3C' : '#FF6B35';" +

                        // Numbered circle marker
                        "  var icon = L.divIcon({" +
                        "    className:''," +
                        "    html: '<div style=\"" +
                        "      background:'+color+';" +
                        "      color:white;" +
                        "      border-radius:50%;" +
                        "      width:32px;height:32px;" +
                        "      display:flex;" +
                        "      align-items:center;" +
                        "      justify-content:center;" +
                        "      font-weight:bold;" +
                        "      font-size:14px;" +
                        "      border:3px solid white;" +
                        "      box-shadow:0 2px 8px rgba(0,0,0,0.4);" +
                        "    \">'+(i+1)+'</div>'," +
                        "    iconSize:[32,32]," +
                        "    iconAnchor:[16,16]," +
                        "    popupAnchor:[0,-20]" +
                        "  });" +

                        "  L.marker([d.lat,d.lng],{icon:icon})" +
                        "    .addTo(map)" +
                        "    .bindPopup(" +
                        "      '<b style=\"color:'+color+'\">'+d.day+'</b>" +
                        "      <br/>'+d.name" +
                        "    );" +

                        "  coords.push([d.lat,d.lng]);" +
                        "}" +

                        // Draw route line
                        "if(coords.length > 1){" +
                        "  L.polyline(coords,{" +
                        "    color:'#FF6B35'," +
                        "    weight:3," +
                        "    opacity:0.8," +
                        "    dashArray:'8,5'" +
                        "  }).addTo(map);" +
                        "}" +

                        // Fit all markers in view
                        "if(coords.length > 1){" +
                        "  map.fitBounds(coords,{padding:[30,30]});" +
                        "} else if(coords.length === 1){" +
                        "  map.setView(coords[0], 12);" +
                        "}" +

                        "} catch(e) {" +
                        "  document.body.innerHTML = " +
                        "    '<div style=\"padding:20px;text-align:center;color:#888\">" +
                        "    Map unavailable.<br>Check internet.</div>';" +
                        "}" +
                        "</script>" +
                        "</body>" +
                        "</html>";

        mapView.loadDataWithBaseURL(
                "https://unpkg.com",  // ✅ base URL matches script source
                html,
                "text/html",
                "UTF-8",
                null);
    }



    private void shareTrip() {
        StringBuilder sb = new StringBuilder();
        sb.append(trip.getCoverEmoji())
                .append(" *").append(trip.getTripName()).append("*\n");
        sb.append("Dates: ").append(trip.getStartDate())
                .append(" to ").append(trip.getEndDate()).append("\n\n");
        sb.append("Destinations:\n");
        for (Destination d : trip.getDestinations())
            sb.append("  - ").append(d.getDayLabel())
                    .append(": ").append(d.getName()).append("\n");
        sb.append("\nPlanned with Travel Planner App");

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, sb.toString());
        startActivity(Intent.createChooser(share, "Share Itinerary via"));
    }
}