package com.example.travel.planner.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.example.travel.planner.R;
import com.example.travel.planner.models.Trip;
import com.example.travel.planner.utils.TripStorage;
import java.util.*;

public class CreateTripActivity extends AppCompatActivity {

    private TextInputEditText etTripName, etNotes;
    private TextView tvStartDate, tvEndDate;
    private ChipGroup chipGroupEmoji;
    private MaterialButton btnSaveTrip;

    private String selectedEmoji = "✈️";
    private String startDate = "", endDate = "";
    private TripStorage storage;

    private final String[] EMOJIS = {
            "✈️","🏖️","🏔️","🌴","🗺️","🏛️","🌊","🎒","🌸","🏕️"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);
        storage = TripStorage.getInstance(this);
        initViews();
        setupEmojiPicker();
        setupDatePickers();
        setupSaveButton();
    }

    private void initViews() {
        etTripName     = findViewById(R.id.et_trip_name);
        etNotes        = findViewById(R.id.et_notes);
        tvStartDate    = findViewById(R.id.tv_start_date);
        tvEndDate      = findViewById(R.id.tv_end_date);
        chipGroupEmoji = findViewById(R.id.chip_group_emoji);
        btnSaveTrip    = findViewById(R.id.btn_save_trip);
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }

    private void setupEmojiPicker() {
        for (String emoji : EMOJIS) {
            Chip chip = new Chip(this);
            chip.setText(emoji);
            chip.setCheckable(true);
            chip.setTextSize(22f);
            chipGroupEmoji.addView(chip);
            if (emoji.equals("✈️")) chip.setChecked(true);
        }
        chipGroupEmoji.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!checkedIds.isEmpty()) {
                Chip c = group.findViewById(checkedIds.get(0));
                if (c != null) selectedEmoji = c.getText().toString();
            }
        });
    }

    private void setupDatePickers() {
        Calendar cal = Calendar.getInstance();
        tvStartDate.setOnClickListener(v ->
                new DatePickerDialog(this, (view, y, m, d) -> {
                    startDate = d + "/" + (m + 1) + "/" + y;
                    tvStartDate.setText(startDate);
                }, cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show()
        );
        tvEndDate.setOnClickListener(v ->
                new DatePickerDialog(this, (view, y, m, d) -> {
                    endDate = d + "/" + (m + 1) + "/" + y;
                    tvEndDate.setText(endDate);
                }, cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show()
        );
    }

    private void setupSaveButton() {
        btnSaveTrip.setOnClickListener(v -> {
            String name = etTripName.getText() != null ?
                    etTripName.getText().toString().trim() : "";

            if (TextUtils.isEmpty(name)) {
                etTripName.setError("Please enter a trip name");
                return;
            }
            if (TextUtils.isEmpty(startDate)) {
                Snackbar.make(v, "Please select a start date",
                        Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(endDate)) {
                Snackbar.make(v, "Please select an end date",
                        Snackbar.LENGTH_SHORT).show();
                return;
            }

            Trip trip = new Trip(name, startDate, endDate, selectedEmoji);
            trip.setNotes(etNotes.getText() != null ?
                    etNotes.getText().toString() : "");

            storage.saveTrip(trip);

            Intent intent = new Intent(this, TripDetailActivity.class);
            intent.putExtra("trip_id", trip.getId());
            intent.putExtra("is_new", true);
            startActivity(intent);
            finish();
        });
    }
}