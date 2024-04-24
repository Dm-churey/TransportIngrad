//package com.example.ingradtransport.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.ingradtransport.R;
//import com.example.ingradtransport.model.SortOption;
//
//import java.util.List;
//
//public class SortApplAdapterNew extends RecyclerView.Adapter<SortApplAdapterNew.SortApplViewHolder> {
//
//    private Context context;
//    private List<SortOption> sortOptions;
//    private OnSortOptionClickListener listener;
//    private ApplicationAdapterNew applicationAdapterNew;
//
//    public SortApplAdapterNew(Context context, List<SortOption> sortOptions, OnSortOptionClickListener listener, ApplicationAdapterNew applicationAdapterNew) {
//        this.context = context;
//        this.sortOptions = sortOptions;
//        this.listener = listener;
//        this.applicationAdapterNew = applicationAdapterNew;
//    }
//
//    @NonNull
//    @Override
//    public SortApplViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sort_item, parent, false);
//        return new SortApplViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull SortApplViewHolder holder, int position) {
//        SortOption sortOption = sortOptions.get(position);
//        holder.sort_button.setText(sortOption.getTitle());
//        holder.sort_button.setOnClickListener(v -> {
//            listener.onSortOptionClick(sortOption);
//            applicationAdapterNew.filterApplications(sortOption.getTitle());
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return sortOptions.size();
//    }
//
//    public static class SortApplViewHolder extends RecyclerView.ViewHolder {
//        TextView sort_button;
//
//        public SortApplViewHolder(@NonNull View itemView) {
//            super(itemView);
//            sort_button = itemView.findViewById(R.id.sort_button);
//        }
//    }
//
//    public interface OnSortOptionClickListener {
//        void onSortOptionClick(SortOption sortOption);
//    }
//
//}
