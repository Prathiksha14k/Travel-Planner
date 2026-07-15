package com.example.travel.planner.adapters;

import android.content.Context;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travel.planner.R;
import com.example.travel.planner.activities.AddDestinationActivity.SearchResult;
import java.util.List;

public class SearchResultAdapter extends
        RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    public interface OnResultClick {
        void onClick(SearchResult result);
    }

    private final Context context;
    private final List<SearchResult> results;
    private final OnResultClick listener;

    public SearchResultAdapter(Context ctx,
                               List<SearchResult> results, OnResultClick listener) {
        this.context  = ctx;
        this.results  = results;
        this.listener = listener;
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder, int pos) {
        SearchResult r = results.get(pos);

        // Show clean "City, Country" name
        holder.tvName.setText("📍 " + r.name);

// Show type hint
        String hint = r.address.contains(",") ?
                r.address.split(",")[r.address.split(",").length - 1].trim() : "";
        holder.tvAddress.setText(hint.isEmpty() ? r.address : "🌍 " + hint);
        String shortAddr = r.address;
        if (shortAddr.length() > 60)
            shortAddr = shortAddr.substring(0, 60) + "...";
        holder.tvAddress.setText(shortAddr);

        holder.itemView.setOnClickListener(
                v -> listener.onClick(r));
    }

    @Override
    public int getItemCount() { return results.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress;
        ViewHolder(View v) {
            super(v);
            tvName    = v.findViewById(R.id.tv_result_name);
            tvAddress = v.findViewById(R.id.tv_result_address);
        }
    }
}