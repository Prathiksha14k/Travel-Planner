package com.example.travel.planner.activities;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travel.planner.R;
import com.example.travel.planner.adapters.DayPlanAdapter;
import com.example.travel.planner.models.DayPlan;
import com.example.travel.planner.models.Destination;
import com.example.travel.planner.models.ScheduleItem;
import com.example.travel.planner.models.Trip;
import com.example.travel.planner.utils.TripStorage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ScheduleActivity extends AppCompatActivity {

    private Trip trip;
    private TripStorage storage;
    private RecyclerView rvDays;
    private DayPlanAdapter dayAdapter;
    private List<DayPlan> dayPlans = new ArrayList<>();
    private TextView tvTripName, tvTripDates, tvTripEmoji;

    private final String[] CATEGORIES = {
            "🏛️ Sightseeing",
            "🍽️ Food & Dining",
            "🏨 Hotel & Stay",
            "🚗 Transport",
            "🛍️ Shopping",
            "🎭 Entertainment",
            "🏖️ Beach & Nature",
            "📸 Photography"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        storage = TripStorage.getInstance(this);
        String tripId = getIntent().getStringExtra("trip_id");
        trip = storage.getTripById(tripId);

        if (trip == null) { finish(); return; }

        initViews();
        buildDayPlans();
        setupRecyclerView();
    }

    private void initViews() {
        tvTripName  = findViewById(R.id.tv_schedule_trip_name);
        tvTripDates = findViewById(R.id.tv_schedule_dates);
        tvTripEmoji = findViewById(R.id.tv_schedule_emoji);
        rvDays      = findViewById(R.id.rv_day_plans);

        tvTripName.setText(trip.getTripName());
        tvTripDates.setText(trip.getStartDate() + " → " + trip.getEndDate());
        tvTripEmoji.setText(trip.getCoverEmoji());

        findViewById(R.id.btn_back_schedule)
                .setOnClickListener(v -> finish());

        FloatingActionButton fabAdd = findViewById(R.id.fab_add_schedule_item);
        fabAdd.setOnClickListener(v -> showAddItemDialog(0));
    }

    private void buildDayPlans() {
        dayPlans.clear();

        if (trip.getSchedule() != null && !trip.getSchedule().isEmpty()) {
            dayPlans.addAll(trip.getSchedule());
            return;
        }

        int numDays = calculateDays();
        if (numDays < 1) numDays = Math.max(1, trip.getDestinations().size());

        for (int i = 0; i < numDays; i++) {
            String dayLabel = "Day " + (i + 1);
            String date     = getDateForDay(i);
            DayPlan day     = new DayPlan(i + 1, dayLabel, date);

            if (i < trip.getDestinations().size()) {
                Destination dest = trip.getDestinations().get(i);
                ScheduleItem item = new ScheduleItem(
                        dest.getName(), dest.getAddress(), "09:00 AM", "🏛️ Sightseeing");
                item.setLatitude(dest.getLatitude());
                item.setLongitude(dest.getLongitude());
                day.addItem(item);
            }
            dayPlans.add(day);
        }

        trip.setSchedule(dayPlans);
        storage.saveTrip(trip);
    }

    private void setupRecyclerView() {
        rvDays.setLayoutManager(new LinearLayoutManager(this));
        dayAdapter = new DayPlanAdapter(this, dayPlans,
                dayIndex -> showAddItemDialog(dayIndex),
                (dayIndex, itemIndex) -> {
                    dayPlans.get(dayIndex).getItems().remove(itemIndex);
                    dayAdapter.notifyItemChanged(dayIndex);
                    saveSchedule();
                    Snackbar.make(rvDays, "Item removed", Snackbar.LENGTH_SHORT).show();
                },
                (dayIndex, itemIndex) -> {
                    ScheduleItem item = dayPlans.get(dayIndex).getItems().get(itemIndex);
                    showEditItemDialog(dayIndex, itemIndex, item);
                }
        );
        rvDays.setAdapter(dayAdapter);
    }

    private void showAddItemDialog(int dayIndex) {
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_add_schedule_item, null);

        TextInputEditText etPlace    = dialogView.findViewById(R.id.et_item_place);
        TextInputEditText etNotes    = dialogView.findViewById(R.id.et_item_notes);
        TextView          tvTime     = dialogView.findViewById(R.id.tv_item_time);
        Spinner           spinnerCategory = dialogView.findViewById(R.id.spinner_category);
        Spinner           spinnerDay = dialogView.findViewById(R.id.spinner_day);

        tvTime.setText("09:00 AM");
        tvTime.setOnClickListener(v -> {
            new TimePickerDialog(this, (view, hour, min) -> {
                String ampm = hour >= 12 ? "PM" : "AM";
                int h = hour > 12 ? hour - 12 : hour == 0 ? 12 : hour;
                tvTime.setText(String.format(Locale.getDefault(),
                        "%02d:%02d %s", h, min, ampm));
            }, 9, 0, false).show();
        });

        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, CATEGORIES);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(catAdapter);

        String[] dayLabels = new String[dayPlans.size()];
        for (int i = 0; i < dayPlans.size(); i++)
            dayLabels[i] = dayPlans.get(i).getDayLabel() + " — " + dayPlans.get(i).getDate();
        ArrayAdapter<String> dayAdapter2 = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, dayLabels);
        dayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(dayAdapter2);
        spinnerDay.setSelection(Math.min(dayIndex, dayPlans.size() - 1));

        if (!trip.getDestinations().isEmpty()) {
            etPlace.setHint("e.g. " + trip.getDestinations().get(0).getName());
        }

        new AlertDialog.Builder(this)
                .setTitle("➕ Add to Schedule")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String place = etPlace.getText() != null ?
                            etPlace.getText().toString().trim() : "";
                    if (place.isEmpty()) {
                        Snackbar.make(rvDays, "Enter a place name",
                                Snackbar.LENGTH_SHORT).show();
                        return;
                    }

                    int selectedDay = spinnerDay.getSelectedItemPosition();
                    String time     = tvTime.getText().toString();
                    String category = CATEGORIES[spinnerCategory.getSelectedItemPosition()];
                    String notes    = etNotes.getText() != null ?
                            etNotes.getText().toString() : "";

                    ScheduleItem item = new ScheduleItem(place, "", time, category);
                    item.setNotes(notes);

                    dayPlans.get(selectedDay).addItem(item);
                    sortItemsByTime(selectedDay);
                    this.dayAdapter.notifyItemChanged(selectedDay);
                    saveSchedule();

                    Snackbar.make(rvDays,
                            place + " added to " + dayPlans.get(selectedDay).getDayLabel(),
                            Snackbar.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showEditItemDialog(int dayIndex, int itemIndex, ScheduleItem item) {
        // ── Inflate dialog view ─────────────────────────────────────────────
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_add_schedule_item, null);

        // ── Find all views ──────────────────────────────────────────────────
        TextInputEditText etPlace    = dialogView.findViewById(R.id.et_item_place);
        TextInputEditText etNotes    = dialogView.findViewById(R.id.et_item_notes);
        TextView          tvTime     = dialogView.findViewById(R.id.tv_item_time);
        Spinner           spinnerCategory = dialogView.findViewById(R.id.spinner_category);
        Spinner           spinnerDay = dialogView.findViewById(R.id.spinner_day);

        // ── Pre-fill existing data ──────────────────────────────────────────
        etPlace.setText(item.getPlaceName());
        etNotes.setText(item.getNotes());
        tvTime.setText(item.getTime());

        // ── Time picker ─────────────────────────────────────────────────────
        tvTime.setOnClickListener(v -> {
            new TimePickerDialog(this, (view, hour, min) -> {
                String ampm = hour >= 12 ? "PM" : "AM";
                int h = hour > 12 ? hour - 12 : hour == 0 ? 12 : hour;
                tvTime.setText(String.format(Locale.getDefault(),
                        "%02d:%02d %s", h, min, ampm));
            }, 9, 0, false).show();
        });

        // ── Category spinner ────────────────────────────────────────────────
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, CATEGORIES);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(catAdapter);
        for (int i = 0; i < CATEGORIES.length; i++) {
            if (CATEGORIES[i].equals(item.getCategory())) {
                spinnerCategory.setSelection(i);
                break;
            }
        }

        // ── Day spinner ─────────────────────────────────────────────────────
        String[] dayLabels = new String[dayPlans.size()];
        for (int i = 0; i < dayPlans.size(); i++)
            dayLabels[i] = dayPlans.get(i).getDayLabel() + " — " + dayPlans.get(i).getDate();
        ArrayAdapter<String> dayAdpt = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, dayLabels);
        dayAdpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(dayAdpt);
        spinnerDay.setSelection(dayIndex); // pre-select current day

        // ── Build and show dialog ───────────────────────────────────────────
        new AlertDialog.Builder(this)
                .setTitle("✏️ Edit Schedule Item")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String place = etPlace.getText() != null ?
                            etPlace.getText().toString().trim() : "";
                    if (place.isEmpty()) return;

                    // Update item fields
                    item.setPlaceName(place);
                    item.setTime(tvTime.getText().toString());
                    item.setCategory(CATEGORIES[spinnerCategory.getSelectedItemPosition()]);
                    item.setNotes(etNotes.getText() != null ?
                            etNotes.getText().toString() : "");

                    int newDayIndex = spinnerDay.getSelectedItemPosition();

                    if (newDayIndex != dayIndex) {
                        // ── DAY CHANGED: move item between day lists ────────
                        dayPlans.get(dayIndex).getItems().remove(itemIndex);
                        dayPlans.get(newDayIndex).addItem(item);
                        sortItemsByTime(newDayIndex);
                        // Refresh BOTH affected day cards
                        dayAdapter.notifyItemChanged(dayIndex);
                        dayAdapter.notifyItemChanged(newDayIndex);
                    } else {
                        // ── SAME DAY: re-sort and refresh ───────────────────
                        sortItemsByTime(dayIndex);
                        dayAdapter.notifyItemChanged(dayIndex);
                    }

                    saveSchedule();
                    Snackbar.make(rvDays, "Updated!", Snackbar.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void sortItemsByTime(int dayIndex) {
        List<ScheduleItem> items = dayPlans.get(dayIndex).getItems();
        items.sort((a, b) -> {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                Date da = sdf.parse(a.getTime());
                Date db = sdf.parse(b.getTime());
                if (da != null && db != null) return da.compareTo(db);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        });
    }

    private void saveSchedule() {
        trip.setSchedule(dayPlans);
        storage.saveTrip(trip);
    }

    private int calculateDays() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy", Locale.getDefault());
            Date start = sdf.parse(trip.getStartDate());
            Date end   = sdf.parse(trip.getEndDate());
            if (start != null && end != null) {
                long diff = end.getTime() - start.getTime();
                int days = (int)(diff / (1000 * 60 * 60 * 24)) + 1;
                return Math.max(1, days);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Math.max(1, trip.getDestinations().size());
    }

    private String getDateForDay(int dayIndex) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy", Locale.getDefault());
            Date start = sdf.parse(trip.getStartDate());
            if (start != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(start);
                cal.add(Calendar.DAY_OF_MONTH, dayIndex);
                SimpleDateFormat display =
                        new SimpleDateFormat("EEE, d MMM", Locale.getDefault());
                return display.format(cal.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "Day " + (dayIndex + 1);
    }
}