package com.example.travel.planner.adapters;

import android.content.Context;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travel.planner.R;
import com.example.travel.planner.models.Recommendation;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class RecommendationAdapter extends
        RecyclerView.Adapter<RecommendationAdapter.RecViewHolder> {

    public interface OnAddClick {
        void onAdd(Recommendation recommendation);
    }

    private final Context context;
    private final List<Recommendation> items;
    private final OnAddClick addListener;

    public RecommendationAdapter(Context ctx,
                                 List<Recommendation> items, OnAddClick listener) {
        this.context     = ctx;
        this.items       = items;
        this.addListener = listener;
    }

    @NonNull @Override
    public RecViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_recommendation, parent, false);
        return new RecViewHolder(v);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecViewHolder holder, int pos) {
        Recommendation rec = items.get(pos);

        holder.tvName.setText(rec.getName());
        holder.tvCategory.setText(rec.getCategory());
        holder.tvDistance.setText(rec.getDistanceString());
        holder.tvAddress.setText(
                rec.getAddress().isEmpty() ?
                        "Nearby" : rec.getAddress());

        // Set category icon color
        int color = getCategoryColor(rec.getCategory());
        holder.tvCategoryIcon.setText(
                getCategoryIcon(rec.getCategory()));
        holder.tvCategoryIcon.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(color));

        holder.btnAdd.setOnClickListener(
                v -> addListener.onAdd(rec));
    }

    private int getCategoryColor(String category) {
        if (category.contains("Tourist")) return 0xFFFF6B35;
        if (category.contains("Restaurant")) return 0xFFE74C3C;
        if (category.contains("Cafe"))  return 0xFF8B4513;
        if (category.contains("Hotel")) return 0xFF3498DB;
        return 0xFF888888;
    }

    private String getCategoryIcon(String category) {
        if (category.contains("Tourist"))    return "🏛";
        if (category.contains("Restaurant")) return "🍽";
        if (category.contains("Cafe"))       return "☕";
        if (category.contains("Hotel"))      return "🏨";
        return "📍";
    }

    @Override
    public int getItemCount() { return items.size(); }

    public static class RecViewHolder
            extends RecyclerView.ViewHolder {
        TextView tvName, tvCategory, tvDistance,
                tvAddress, tvCategoryIcon;
        MaterialButton btnAdd;

        RecViewHolder(View v) {
            super(v);
            tvName         = v.findViewById(R.id.tv_rec_name);
            tvCategory     = v.findViewById(R.id.tv_rec_category);
            tvDistance     = v.findViewById(R.id.tv_rec_distance);
            tvAddress      = v.findViewById(R.id.tv_rec_address);
            tvCategoryIcon = v.findViewById(R.id.tv_rec_icon);
            btnAdd         = v.findViewById(R.id.btn_add_rec);
        }
    }
}