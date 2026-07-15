package com.example.travel.planner.adapters;

import android.content.Context;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travel.planner.R;
import com.example.travel.planner.models.ScheduleItem;
import java.util.List;

public class ScheduleItemAdapter extends
        RecyclerView.Adapter<ScheduleItemAdapter.ItemViewHolder> {

    public interface OnDeleteClick { void onDelete(int pos); }
    public interface OnItemClick   { void onClick(int pos);  }

    private final Context context;
    private final List<ScheduleItem> items;
    private final OnDeleteClick deleteListener;
    private final OnItemClick   clickListener;

    public ScheduleItemAdapter(Context ctx,
                               List<ScheduleItem> items,
                               OnDeleteClick del, OnItemClick click) {
        this.context        = ctx;
        this.items          = items;
        this.deleteListener = del;
        this.clickListener  = click;
    }

    @NonNull @Override
    public ItemViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_schedule_entry, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ItemViewHolder holder, int pos) {
        ScheduleItem item = items.get(pos);

        holder.tvTime.setText(item.getTime());
        holder.tvPlace.setText(item.getPlaceName());
        holder.tvCategory.setText(item.getCategory());
        holder.tvNotes.setText(item.getNotes());

        // Show notes only if not empty
        holder.tvNotes.setVisibility(
                item.getNotes().isEmpty() ?
                        View.GONE : View.VISIBLE);

        // Timeline dot color by category
        holder.viewDot.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(
                        getCategoryColor(item.getCategory())));

        // Hide timeline line for last item
        holder.viewLine.setVisibility(
                pos == items.size() - 1 ? View.INVISIBLE : View.VISIBLE);

        holder.btnDelete.setOnClickListener(
                v -> deleteListener.onDelete(pos));
        holder.itemView.setOnClickListener(
                v -> clickListener.onClick(pos));
    }

    private int getCategoryColor(String category) {
        if (category.contains("Sightseeing")) return 0xFFFF6B35;
        if (category.contains("Food"))        return 0xFFE74C3C;
        if (category.contains("Hotel"))       return 0xFF3498DB;
        if (category.contains("Transport"))   return 0xFF2ECC71;
        if (category.contains("Shopping"))    return 0xFF9B59B6;
        if (category.contains("Entertainment"))return 0xFFF39C12;
        if (category.contains("Beach"))       return 0xFF1ABC9C;
        if (category.contains("Photography")) return 0xFFE91E63;
        return 0xFFFF6B35;
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvPlace, tvCategory, tvNotes;
        View viewDot, viewLine;
        ImageButton btnDelete;

        ItemViewHolder(View v) {
            super(v);
            tvTime    = v.findViewById(R.id.tv_item_time);
            tvPlace   = v.findViewById(R.id.tv_item_place);
            tvCategory= v.findViewById(R.id.tv_item_category);
            tvNotes   = v.findViewById(R.id.tv_item_notes);
            viewDot   = v.findViewById(R.id.view_timeline_dot);
            viewLine  = v.findViewById(R.id.view_timeline_line);
            btnDelete = v.findViewById(R.id.btn_delete_item);
        }
    }
}