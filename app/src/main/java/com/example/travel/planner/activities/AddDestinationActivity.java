package com.example.travel.planner.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travel.planner.R;
import com.example.travel.planner.adapters.SearchResultAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class AddDestinationActivity extends AppCompatActivity {

    private TextInputEditText etSearch;
    private RecyclerView rvSearchResults;
    private WebView mapView;
    private MaterialButton btnConfirm;
    private TextView tvSelectedPlace, tvSearching;
    private SearchResultAdapter searchAdapter;
    private final List<SearchResult> searchResults = new ArrayList<>();

    private double selectedLat = 0, selectedLng = 0;
    private String selectedName = "", selectedAddress = "";

    private final Handler mapPollHandler = new Handler(Looper.getMainLooper());
    private boolean isPolling = false;
    private int lastClickCount = 0;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_destination);
        initViews();
        setupMap();
        setupSearch();
        setupConfirmButton();
    }

    private void initViews() {
        etSearch        = findViewById(R.id.et_search_place);
        rvSearchResults = findViewById(R.id.rv_search_results);
        mapView         = findViewById(R.id.osm_map);
        btnConfirm      = findViewById(R.id.btn_confirm_destination);
        tvSelectedPlace = findViewById(R.id.tv_selected_place);
        tvSearching     = findViewById(R.id.tv_searching);

        findViewById(R.id.btn_back_add).setOnClickListener(v -> finish());
        btnConfirm.setEnabled(false);
        btnConfirm.setAlpha(0.5f);

        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        searchAdapter = new SearchResultAdapter(
                this, searchResults, result -> {
            selectedLat     = result.lat;
            selectedLng     = result.lng;
            selectedName    = result.name;
            selectedAddress = result.address;

            rvSearchResults.setVisibility(View.GONE);
            etSearch.clearFocus();

            // Fly to location on map
            flyToLocation(result.lat, result.lng,
                    result.name, result.address,
                    result.boundingBox);

            showSelectedBanner(result.name, result.address);
        });
        rvSearchResults.setAdapter(searchAdapter);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupMap() {
        WebSettings settings = mapView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(true);

        mapView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                startPollingForMapClick();
            }
        });

        // Full featured Leaflet map — feels like Google Maps
        String html = buildMapHtml(20.5937, 78.9629, 5);
        mapView.loadDataWithBaseURL(
                "https://cdnjs.cloudflare.com",
                html, "text/html", "UTF-8", null);
    }

    private String buildMapHtml(double lat, double lng, int zoom) {
        return "<!DOCTYPE html><html><head>" +
                "<meta name='viewport' content='width=device-width," +
                "initial-scale=1.0,maximum-scale=1.0,user-scalable=no'>" +

                // Leaflet CSS
                "<link rel='stylesheet' href='https://unpkg.com/leaflet@1.9.4/dist/leaflet.css'/>" +
                // Leaflet JS
                "<script src='https://unpkg.com/leaflet@1.9.4/dist/leaflet.js'></script>" +

                "<style>" +
                "* { margin:0; padding:0; box-sizing:border-box; }" +
                "body { width:100vw; height:100vh; overflow:hidden; }" +
                "#map { width:100%; height:100vh; }" +

                // Custom marker style
                ".custom-pin {" +
                "  background:#FF6B35;" +
                "  border:3px solid white;" +
                "  border-radius:50% 50% 50% 0;" +
                "  transform:rotate(-45deg);" +
                "  width:30px; height:30px;" +
                "  box-shadow:0 2px 8px rgba(0,0,0,0.4);" +
                "}" +

                // Popup style
                ".leaflet-popup-content-wrapper {" +
                "  border-radius:12px;" +
                "  box-shadow:0 4px 16px rgba(0,0,0,0.2);" +
                "  border-left:4px solid #FF6B35;" +
                "}" +
                ".leaflet-popup-content {" +
                "  font-family:sans-serif;" +
                "  font-size:13px;" +
                "  line-height:1.5;" +
                "}" +
                ".popup-title {" +
                "  font-weight:bold;" +
                "  color:#FF6B35;" +
                "  font-size:14px;" +
                "  margin-bottom:2px;" +
                "}" +
                ".popup-addr {" +
                "  color:#666;" +
                "  font-size:12px;" +
                "}" +
                ".popup-btn {" +
                "  background:#FF6B35;" +
                "  color:white;" +
                "  border:none;" +
                "  border-radius:6px;" +
                "  padding:4px 12px;" +
                "  margin-top:6px;" +
                "  cursor:pointer;" +
                "  font-size:12px;" +
                "  width:100%;" +
                "}" +
                "</style>" +
                "</head><body>" +
                "<div id='map'></div>" +
                "<script>" +

                // Click tracking variables
                "var clickedLat=0,clickedLng=0,clickCount=0;" +
                "var currentMarker=null;" +

                // Initialize map
                "var map=L.map('map',{" +
                "  zoomControl:true," +
                "  attributionControl:true" +
                "}).setView([" + lat + "," + lng + "]," + zoom + ");" +

                // CARTO tiles — clean, fast, no blocking
                "L.tileLayer('https://{s}.basemaps.cartocdn.com" +
                "/rastertiles/voyager/{z}/{x}/{y}.png',{" +
                "  attribution:'© OpenStreetMap © CARTO'," +
                "  maxZoom:19,minZoom:2" +
                "}).addTo(map);" +

                // Custom orange pin icon
                "var pinIcon = L.divIcon({" +
                "  className:''," +
                "  html:'<div class=\"custom-pin\"></div>'," +
                "  iconSize:[30,30]," +
                "  iconAnchor:[15,30]," +
                "  popupAnchor:[0,-35]" +
                "});" +

                // Function to fly to a searched location
                "function flyToPlace(lat,lng,name,addr,swLat,swLng,neLat,neLng){" +
                "  if(currentMarker) map.removeLayer(currentMarker);" +
                "  if(swLat && swLng && neLat && neLng){" +
                // Fit to bounding box — shows the whole city
                "    map.fitBounds([[swLat,swLng],[neLat,neLng]],{" +
                "      padding:[40,40],maxZoom:14" +
                "    });" +
                "  } else {" +
                "    map.flyTo([lat,lng],13,{duration:1.5});" +
                "  }" +
                // Place marker with rich popup
                "  currentMarker=L.marker([lat,lng],{icon:pinIcon})" +
                "    .addTo(map)" +
                "    .bindPopup(" +
                "      '<div class=\"popup-title\">📍 '+name+'</div>'" +
                "      +'<div class=\"popup-addr\">'+addr+'</div>'" +
                "    ).openPopup();" +
                "}" +

                // Map click handler
                "map.on('click',function(e){" +
                "  clickedLat=e.latlng.lat;" +
                "  clickedLng=e.latlng.lng;" +
                "  clickCount++;" +
                "  if(currentMarker) map.removeLayer(currentMarker);" +
                "  currentMarker=L.marker(" +
                "    [clickedLat,clickedLng],{icon:pinIcon}" +
                "  ).addTo(map)" +
                "   .bindPopup('<div class=\"popup-title\">📍 Selected</div>')" +
                "   .openPopup();" +
                "});" +

                "</script></body></html>";
    }

    // Fly map to searched location with bounding box
    private void flyToLocation(double lat, double lng,
                               String name, String address, double[] bbox) {
        String safeName = name.replace("'", "\\'")
                .replace("\"", "");
        String safeAddr = address.replace("'", "\\'")
                .replace("\"", "");

        String js;
        if (bbox != null && bbox.length == 4) {
            // bbox = [swLat, swLng, neLat, neLng]
            js = String.format(
                    "flyToPlace(%f,%f,'%s','%s',%f,%f,%f,%f);",
                    lat, lng, safeName, safeAddr,
                    bbox[0], bbox[1], bbox[2], bbox[3]);
        } else {
            js = String.format(
                    "flyToPlace(%f,%f,'%s','%s',0,0,0,0);",
                    lat, lng, safeName, safeAddr);
        }
        mapView.evaluateJavascript(js, null);
    }

    // Poll map for tap clicks
    private void startPollingForMapClick() {
        isPolling = true;
        mapPollHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isPolling) return;
                mapView.evaluateJavascript("clickCount", countStr -> {
                    try {
                        int count = Integer.parseInt(
                                countStr.replaceAll("[^0-9]", ""));
                        if (count > lastClickCount) {
                            lastClickCount = count;
                            mapView.evaluateJavascript(
                                    "clickedLat+','+clickedLng",
                                    coordStr -> {
                                        String clean = coordStr
                                                .replace("\"","").trim();
                                        String[] parts = clean.split(",");
                                        if (parts.length == 2) {
                                            try {
                                                double lat = Double
                                                        .parseDouble(parts[0].trim());
                                                double lng = Double
                                                        .parseDouble(parts[1].trim());
                                                runOnUiThread(() ->
                                                        reverseGeocode(lat, lng));
                                            } catch (NumberFormatException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                mapPollHandler.postDelayed(this, 800);
            }
        }, 1000);
    }

    private void reverseGeocode(double lat, double lng) {
        tvSelectedPlace.setText("Finding location...");
        tvSelectedPlace.setVisibility(View.VISIBLE);

        new Thread(() -> {
            try {
                String urlStr =
                        "https://nominatim.openstreetmap.org/reverse" +
                                "?lat=" + lat + "&lon=" + lng + "&format=json";
                URL url = new URL(urlStr);
                HttpURLConnection conn =
                        (HttpURLConnection) url.openConnection();
                conn.setRequestProperty(
                        "User-Agent", "TravelPlannerApp/1.0");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null)
                    sb.append(line);
                reader.close();

                JSONObject obj = new JSONObject(sb.toString());
                String display = obj.optString(
                        "display_name", "Selected Location");
                String name = display.split(",")[0].trim();

                runOnUiThread(() -> {
                    selectedLat     = lat;
                    selectedLng     = lng;
                    selectedName    = name;
                    selectedAddress = display;
                    showSelectedBanner(name, display);
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    selectedLat  = lat;
                    selectedLng  = lng;
                    selectedName = String.format(
                            "Location %.4f, %.4f", lat, lng);
                    selectedAddress = selectedName;
                    showSelectedBanner(selectedName, selectedAddress);
                });
            }
        }).start();
    }

    private void showSelectedBanner(String name, String address) {
        tvSelectedPlace.setText("📍 " + name);
        tvSelectedPlace.setVisibility(View.VISIBLE);
        btnConfirm.setEnabled(true);
        btnConfirm.setAlpha(1.0f);
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            private final Handler handler =
                    new Handler(Looper.getMainLooper());
            private Runnable runnable;

            @Override
            public void beforeTextChanged(
                    CharSequence s, int st, int c, int a) {}
            @Override
            public void onTextChanged(
                    CharSequence s, int st, int b, int c) {
                handler.removeCallbacks(runnable);
                if (s.toString().trim().length() < 3)
                    rvSearchResults.setVisibility(View.GONE);
            }
            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                if (query.length() < 3) return;
                runnable = () -> searchPlaces(query);
                handler.postDelayed(runnable, 600);
            }
        });
    }

    private void searchPlaces(String query) {
        tvSearching.setVisibility(View.VISIBLE);
        new Thread(() -> {
            try {
                String encoded = URLEncoder.encode(query, "UTF-8");


                String urlStr =
                        "https://nominatim.openstreetmap.org/search" +
                                "?q=" + encoded +
                                "&format=json" +
                                "&limit=5" +
                                "&dedupe=1" +
                                "&addressdetails=1" +
                                "&featuretype=settlement";

                URL url = new URL(urlStr);
                HttpURLConnection conn =
                        (HttpURLConnection) url.openConnection();
                conn.setRequestProperty(
                        "User-Agent", "TravelPlannerApp/1.0");
                conn.setConnectTimeout(6000);
                conn.setReadTimeout(6000);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null)
                    sb.append(line);
                reader.close();

                JSONArray arr = new JSONArray(sb.toString());
                List<SearchResult> results = new ArrayList<>();

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);

                    // Skip tiny villages — only show cities/towns/states
                    String type = obj.optString("type", "");
                    String clazz = obj.optString("class", "");
                    if (clazz.equals("amenity") ||
                            clazz.equals("shop") ||
                            clazz.equals("building") ||
                            type.equals("house") ||
                            type.equals("path") ||
                            type.equals("road")) continue;

                    SearchResult r = new SearchResult();
                    String displayName = obj.optString("display_name", "");

                    //  Build a clean label: "Paris, France"
                    r.name = buildCleanName(obj, displayName);
                    r.address = displayName;
                    r.lat = obj.getDouble("lat");
                    r.lng = obj.getDouble("lon");

                    // Get bounding box for smart zoom
                    if (obj.has("boundingbox")) {
                        JSONArray bbox = obj.getJSONArray("boundingbox");
                        r.boundingBox = new double[]{
                                bbox.getDouble(0), // swLat
                                bbox.getDouble(2), // swLng
                                bbox.getDouble(1), // neLat
                                bbox.getDouble(3)  // neLng
                        };
                    }
                    results.add(r);
                }

                // If featuretype returned nothing, fallback to normal search
                if (results.isEmpty()) {
                    results = fallbackSearch(query);
                }

                List<SearchResult> finalResults = results;
                runOnUiThread(() -> {
                    tvSearching.setVisibility(View.GONE);
                    searchResults.clear();
                    searchResults.addAll(finalResults);
                    searchAdapter.notifyDataSetChanged();
                    rvSearchResults.setVisibility(
                            finalResults.isEmpty() ?
                                    View.GONE : View.VISIBLE);
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    tvSearching.setVisibility(View.GONE);
                    Snackbar.make(mapView,
                            "Search failed. Check internet.",
                            Snackbar.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    //  Build "City, Country" style label
    private String buildCleanName(JSONObject obj, String displayName) {
        try {
            JSONObject address = obj.optJSONObject("address");
            if (address != null) {
                String city    = address.optString("city", "");
                String town    = address.optString("town", "");
                String state   = address.optString("state", "");
                String country = address.optString("country", "");
                String name    = address.optString("country_code","")
                        .isEmpty() ? "" : "";

                // Pick best city name
                String place = !city.isEmpty() ? city :
                        !town.isEmpty() ? town :
                                displayName.split(",")[0].trim();

                // Build "Paris, France" format
                if (!place.isEmpty() && !country.isEmpty()) {
                    return place + ", " + country;
                } else if (!place.isEmpty()) {
                    return place;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Fallback — first 2 parts of display name
        String[] parts = displayName.split(",");
        if (parts.length >= 2) {
            return parts[0].trim() + ", " + parts[parts.length - 1].trim();
        }
        return displayName.split(",")[0].trim();
    }

    // Fallback if settlement search returns nothing
    private List<SearchResult> fallbackSearch(String query)
            throws Exception {
        String encoded = URLEncoder.encode(query, "UTF-8");
        String urlStr =
                "https://nominatim.openstreetmap.org/search" +
                        "?q=" + encoded +
                        "&format=json&limit=5&dedupe=1&addressdetails=1";

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", "TravelPlannerApp/1.0");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) sb.append(line);
        reader.close();

        JSONArray arr = new JSONArray(sb.toString());
        List<SearchResult> results = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            SearchResult r = new SearchResult();
            String displayName = obj.optString("display_name", "");
            r.name    = buildCleanName(obj, displayName);
            r.address = displayName;
            r.lat     = obj.getDouble("lat");
            r.lng     = obj.getDouble("lon");
            if (obj.has("boundingbox")) {
                JSONArray bbox = obj.getJSONArray("boundingbox");
                r.boundingBox = new double[]{
                        bbox.getDouble(0),
                        bbox.getDouble(2),
                        bbox.getDouble(1),
                        bbox.getDouble(3)
                };
            }
            results.add(r);
        }
        return results;
    }

    private void setupConfirmButton() {
        btnConfirm.setOnClickListener(v -> {
            if (selectedName.isEmpty()) return;
            Intent result = new Intent();
            result.putExtra("dest_name",    selectedName);
            result.putExtra("dest_address", selectedAddress);
            result.putExtra("dest_lat",     selectedLat);
            result.putExtra("dest_lng",     selectedLng);
            setResult(RESULT_OK, result);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isPolling = false;
        mapPollHandler.removeCallbacksAndMessages(null);
    }

    public static class SearchResult {
        public String name, address;
        public double lat, lng;
        public double[] boundingBox;
    }
}