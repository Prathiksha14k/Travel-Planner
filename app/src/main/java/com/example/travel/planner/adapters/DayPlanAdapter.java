package com.example.travel.planner.adapters;

import android.content.Context;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travel.planner.R;
import com.example.travel.planner.models.DayPlan;
import com.example.travel.planner.models.ScheduleItem;
import java.util.List;

public class DayPlanAdapter extends
        RecyclerView.Adapter<DayPlanAdapter.DayViewHolder> {

    public interface OnAddItemClick {
        void onAdd(int dayIndex);
    }
    public interface OnDeleteItemClick {
        void onDelete(int dayIndex, int itemIndex);
    }
    public interface OnItemClick {
        void onClick(int dayIndex, int itemIndex);
    }

    private final Context context;
    private final List<DayPlan> dayPlans;
    private final OnAddItemClick addListener;
    private final OnDeleteItemClick deleteListener;
    private final OnItemClick itemClickListener;

    // Category colors
    private final String[] CAT_COLORS = {
            "#FF6B35", "#E74C3C", "#3498DB",
            "#2ECC71", "#9B59B6", "#F39C12",
            "#1ABC9C", "#E91E63"
    };

    public DayPlanAdapter(Context ctx, List<DayPlan> plans,
                          OnAddItemClick add,
                          OnDeleteItemClick delete,
                          OnItemClick click) {
        this.context          = ctx;
        this.dayPlans         = plans;
        this.addListener      = add;
        this.deleteListener   = delete;
        this.itemClickListener= click;
    }

    @NonNull @Override
    public DayViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_day_plan, parent, false);
        return new DayViewHolder(v);
    }

    @Override
    public void onBindViewHolder(
            @NonNull DayViewHolder holder, int pos) {
        DayPlan day = dayPlans.get(pos);

        holder.tvDayLabel.setText(day.getDayLabel());
        holder.tvDayDate.setText(day.getDate());
        holder.tvItemCount.setText(
                day.getItems().size() + " activities");

        // Day number circle color cycles
        int[] colors = {
                0xFFFF6B35, 0xFF004E89, 0xFF1A936F,
                0xFF845EC2, 0xFFE74C3C
        };
        holder.tvDayNumber.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(
                        colors[pos % colors.length]));
        holder.tvDayNumber.setText(
                String.valueOf(day.getDayNumber()));

        // Show/hide empty state
        if (day.getItems().isEmpty()) {
            holder.tvEmptyDay.setVisibility(View.VISIBLE);
            holder.rvScheduleItems.setVisibility(View.GONE);
        } else {
            holder.tvEmptyDay.setVisibility(View.GONE);
            holder.rvScheduleItems.setVisibility(View.VISIBLE);

            // Nested RecyclerView for schedule items
            ScheduleItemAdapter itemAdapter =
                    new ScheduleItemAdapter(context,
                            day.getItems(),
                            itemIndex -> deleteListener.onDelete(pos, itemIndex),
                            itemIndex -> itemClickListener.onClick(pos, itemIndex)
                    );
            holder.rvScheduleItems.setLayoutManager(
                    new LinearLayoutManager(context));
            holder.rvScheduleItems.setAdapter(itemAdapter);
            holder.rvScheduleItems.setNestedScrollingEnabled(false);
        }

        // Add item button for this day
        holder.btnAddItem.setOnClickListener(
                v -> addListener.onAdd(pos));

        // Expand/collapse day card
        holder.layoutDayContent.setVisibility(
                holder.isExpanded ? View.VISIBLE : View.GONE);
        holder.ivExpandIcon.setRotation(
                holder.isExpanded ? 180f : 0f);

        holder.layoutDayHeader.setOnClickListener(v -> {
            holder.isExpanded = !holder.isExpanded;
            holder.layoutDayContent.setVisibility(
                    holder.isExpanded ? View.VISIBLE : View.GONE);
            holder.ivExpandIcon.animate()
                    .rotation(holder.isExpanded ? 180f : 0f)
                    .setDuration(200).start();
        });
    }

    @Override
    public int getItemCount() { return dayPlans.size(); }

    public static class DayViewHolder
            extends RecyclerView.ViewHolder {
        TextView tvDayLabel, tvDayDate, tvDayNumber,
                tvItemCount, tvEmptyDay;
        ImageView ivExpandIcon;
        RecyclerView rvScheduleItems;
        LinearLayout layoutDayHeader, layoutDayContent;
        com.google.android.material.button.MaterialButton btnAddItem;
        boolean isExpanded = true;

        DayViewHolder(View v) {
            super(v);
            tvDayLabel      = v.findViewById(R.id.tv_day_label_plan);
            tvDayDate       = v.findViewById(R.id.tv_day_date);
            tvDayNumber     = v.findViewById(R.id.tv_day_number);
            tvItemCount     = v.findViewById(R.id.tv_item_count);
            tvEmptyDay      = v.findViewById(R.id.tv_empty_day);
            ivExpandIcon    = v.findViewById(R.id.iv_expand_icon);
            rvScheduleItems = v.findViewById(R.id.rv_schedule_items);
            layoutDayHeader = v.findViewById(R.id.layout_day_header);
            layoutDayContent= v.findViewById(R.id.layout_day_content);
            btnAddItem      = v.findViewById(R.id.btn_add_to_day);
        }
    }
}