package com.example.travel.planner.adapters;

import android.content.Context;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travel.planner.R;
import com.example.travel.planner.models.Trip;
import java.util.ArrayList;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    public interface OnTripClickListener {
        void onTripClick(Trip trip);
        void onTripDelete(Trip trip);
    }

    private List<Trip> trips = new ArrayList<>();
    private final Context context;
    private final OnTripClickListener listener;

    private final int[] GRADIENTS = {
            R.drawable.trip_card_gradient_1,
            R.drawable.trip_card_gradient_2,
            R.drawable.trip_card_gradient_3,
            R.drawable.trip_card_gradient_4,
            R.drawable.trip_card_gradient_5
    };

    public TripAdapter(Context ctx, OnTripClickListener l) {
        this.context  = ctx;
        this.listener = l;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public TripViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_trip, parent, false);
        return new TripViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int pos) {
        Trip trip = trips.get(pos);

        holder.tvEmoji.setText(trip.getCoverEmoji());
        holder.tvName.setText(trip.getTripName());
        holder.tvDates.setText(
                trip.getStartDate() + " → " + trip.getEndDate());
        holder.tvDestCount.setText(
                trip.getDestinations().size() + " destinations");
        holder.tvDuration.setText(
                trip.getDestinations().size() + " days planned");

        // Set gradient background
        holder.viewCardBg.setBackgroundResource(
                GRADIENTS[pos % GRADIENTS.length]);

        holder.itemView.setOnClickListener(
                v -> listener.onTripClick(trip));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onTripDelete(trip);
            return true;
        });
    }

    @Override public int getItemCount() { return trips.size(); }

    static class TripViewHolder extends RecyclerView.ViewHolder {
        View viewCardBg;
        TextView tvEmoji, tvName, tvDates, tvDestCount, tvDuration;

        TripViewHolder(View v) {
            super(v);
            viewCardBg  = v.findViewById(R.id.view_card_bg);
            tvEmoji     = v.findViewById(R.id.tv_trip_emoji);
            tvName      = v.findViewById(R.id.tv_trip_name);
            tvDates     = v.findViewById(R.id.tv_trip_dates);
            tvDestCount = v.findViewById(R.id.tv_trip_dest_count);
            tvDuration  = v.findViewById(R.id.tv_trip_duration);
        }
    }
}