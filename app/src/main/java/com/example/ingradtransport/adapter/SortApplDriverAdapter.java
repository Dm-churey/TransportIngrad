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

public class SortApplDriverAdapter extends RecyclerView.Adapter<SortApplDriverAdapter.SortApplDriverViewHolder>{
    private Context context;
    private List<SortOption> sortOptions;
    private SortApplDriverAdapter.OnSortOptionClickListener listener;
    private ApplDriverAdapter applDriverAdapter;

    public SortApplDriverAdapter(Context context, List<SortOption> sortOptions, SortApplDriverAdapter.OnSortOptionClickListener listener, ApplDriverAdapter applDriverAdapter) {
        this.context = context;
        this.sortOptions = sortOptions;
        this.listener = listener;
        this.applDriverAdapter = applDriverAdapter;
    }

    @NonNull
    @Override
    public SortApplDriverAdapter.SortApplDriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sort_item, parent, false);
        return new SortApplDriverAdapter.SortApplDriverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SortApplDriverAdapter.SortApplDriverViewHolder holder, int position) {
        SortOption sortOption = sortOptions.get(position);
        holder.sort_button.setText(sortOption.getTitle());
        holder.sort_button.setOnClickListener(v -> {
            listener.onSortOptionClick(sortOption);
            applDriverAdapter.filterApplications(sortOption.getTitle());
        });
    }

    @Override
    public int getItemCount() {
        return sortOptions.size();
    }

    public static class SortApplDriverViewHolder extends RecyclerView.ViewHolder {
        TextView sort_button;

        public SortApplDriverViewHolder(@NonNull View itemView) {
            super(itemView);
            sort_button = itemView.findViewById(R.id.sort_button);
        }
    }

    public interface OnSortOptionClickListener {
        void onSortOptionClick(SortOption sortOption);
    }

}
