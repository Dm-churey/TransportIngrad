package com.example.ingradtransport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ingradtransport.R;
import com.example.ingradtransport.model.Application;
import com.example.ingradtransport.model.SortOption;

import java.util.List;

public class SortApplAdapter extends RecyclerView.Adapter<SortApplAdapter.SortApplViewHolder> {

    private List<SortOption> sortOptions;;
    private Context context;
    private OnSortOptionClickListener listener;
    private ApplicationAdapter applicationAdapter;

    public SortApplAdapter(Context context, List<SortOption> sortOptions, OnSortOptionClickListener listener, ApplicationAdapter applicationAdapter) {
        this.context = context;
        this.sortOptions = sortOptions;
        this.listener = listener;
        this.applicationAdapter = applicationAdapter;
    }

    @NonNull
    @Override
    public SortApplViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sort_item, parent, false);
        return new SortApplViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SortApplViewHolder holder, int position) {
        SortOption sortOption = sortOptions.get(position);
        holder.sort_button.setText(sortOption.getTitle());
        holder.sort_button.setOnClickListener(v -> {
            listener.onSortOptionClick(sortOption);
            applicationAdapter.filterApplications(applicationAdapter.getCurrentList(), sortOption.getTitle());
        });
    }

    @Override
    public int getItemCount() {
        return sortOptions.size();
    }

    public static class SortApplViewHolder extends RecyclerView.ViewHolder {
        Button sort_button;

        public SortApplViewHolder(@NonNull View itemView) {
            super(itemView);
            sort_button = itemView.findViewById(R.id.sort_button);
        }
    }

    public interface OnSortOptionClickListener {
        void onSortOptionClick(SortOption sortOption);
    }

}
