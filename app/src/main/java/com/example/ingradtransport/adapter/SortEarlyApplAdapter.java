package com.example.ingradtransport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ingradtransport.R;
import com.example.ingradtransport.model.SortOption;

import java.util.List;

public class SortEarlyApplAdapter extends RecyclerView.Adapter<SortEarlyApplAdapter.SortEarlyApplViewHolder> {

    private Context context;
    private List<SortOption> sortOptions;
    private SortEarlyApplAdapter.OnSortOptionClickListener listener;
    private EarlyApplicationAdapter earlyApplicationAdapter;

    public SortEarlyApplAdapter(Context context, List<SortOption> sortOptions, SortEarlyApplAdapter.OnSortOptionClickListener listener, EarlyApplicationAdapter earlyApplicationAdapter) {
        this.context = context;
        this.sortOptions = sortOptions;
        this.listener = listener;
        this.earlyApplicationAdapter = earlyApplicationAdapter;
    }

    @NonNull
    @Override
    public SortEarlyApplAdapter.SortEarlyApplViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sort_item, parent, false);
        return new SortEarlyApplAdapter.SortEarlyApplViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SortEarlyApplAdapter.SortEarlyApplViewHolder holder, int position) {
        SortOption sortOption = sortOptions.get(position);
        holder.sort_button.setText(sortOption.getTitle());
        holder.sort_button.setOnClickListener(v -> {
            listener.onSortOptionClick(sortOption);
            earlyApplicationAdapter.filterApplications(sortOption.getTitle());
        });
    }

    @Override
    public int getItemCount() {
        return sortOptions.size();
    }

    public static class SortEarlyApplViewHolder extends RecyclerView.ViewHolder {
        TextView sort_button;

        public SortEarlyApplViewHolder(@NonNull View itemView) {
            super(itemView);
            sort_button = itemView.findViewById(R.id.sort_button);
        }
    }

    public interface OnSortOptionClickListener {
        void onSortOptionClick(SortOption sortOption);
    }

}
