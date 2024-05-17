package com.example.ingradtransport.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ingradtransport.R;
import com.example.ingradtransport.driverFragment.ApplicationDriverFragment;
import com.example.ingradtransport.model.Application;
import com.example.ingradtransport.model.SortOption;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ApplDriverAdapter extends ListAdapter<Application, ApplDriverAdapter.Holder> {

    private ApplicationDriverFragment fragment;
    private List<Application> statusApplications;
    static class Holder extends RecyclerView.ViewHolder {
        private final TextView textPurpose;
        private final TextView textDate;
        private final TextView textStatus;
        private final FrameLayout frameLayout;
        private final Button button_details;

        public Holder(View itemView) {
            super(itemView);
            textPurpose = itemView.findViewById(R.id.appl_item_purpose);
            textDate = itemView.findViewById(R.id.appl_item_date);
            frameLayout = itemView.findViewById(R.id.fr_l);
            textStatus = itemView.findViewById(R.id.appl_item_status);
            button_details = itemView.findViewById(R.id.button_details);
            button_details.setText("Подробнее");
        }

        public void bind(Application application, Context context) {
            textPurpose.setText(application.getPurpose());
            textDate.setText("Дата: " + application.getDate());
            textStatus.setText(application.getStatus());
            if ("В обработке".equals(application.getStatus())) {
                frameLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.card_ripple));
            } else if ("Согласовано".equals(application.getStatus())) {
                frameLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.approve));
            } else if ("Отказано".equals(application.getStatus())) {
                frameLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.denied));
            } else if ("Подтверждено".equals(application.getStatus())) {
                frameLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.teal_700));
            }
        }
    }

    static class Comparator extends DiffUtil.ItemCallback<Application> {
        @Override
        public boolean areItemsTheSame(Application oldItem, Application newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Application oldItem, @NonNull Application newItem) {
            return oldItem.equals(newItem);
        }
    }

    private Context context;
    public ApplDriverAdapter(Context context, ApplicationDriverFragment fragment) {
        super(new ApplDriverAdapter.Comparator());
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ApplDriverAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.application_item, parent, false);
        return new ApplDriverAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplDriverAdapter.Holder holder, int position) {
        Application application = getItem(position);
        holder.bind(application, context);
        holder.itemView.setVisibility(View.VISIBLE);


        holder.button_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.showDialogForApplication(application);
            }
        });
    }

    public void updateOriginalApplications(List<Application> statusApplications) {
        this.statusApplications = new ArrayList<>(statusApplications);
    }

    public void filterApplications(String filter) {
        List<Application> filteredApplications = new ArrayList<>();
        switch (filter) {
            case "Все":
                //filteredApplications.addAll(filteredApplications);
                filteredApplications.addAll(statusApplications);
                break;
            case "Сегодня":
                filterByDate(filteredApplications, LocalDate.now());
                break;
            case "Завтра":
                filterByDate(filteredApplications, LocalDate.now().plusDays(1));
                break;
            case "Неделя":
                filterByDateRange(filteredApplications, LocalDate.now(), LocalDate.now().plusWeeks(1));
                break;
            case "Месяц":
                filterByDateRange(filteredApplications, LocalDate.now(), LocalDate.now().plusMonths(1));
                break;
            default:
        }
        submitList(filteredApplications);
    }

    private void filterByDate(List<Application> filteredApplications, LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for (Application application : statusApplications) {
            LocalDate appDate = LocalDate.parse(application.getDate(), formatter);
            if (appDate.isEqual(date)) {
                filteredApplications.add(application);
            }
        }
    }

    private void filterByDateRange(List<Application> filteredApplications, LocalDate startDate, LocalDate endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for (Application application : statusApplications) { // Используем filteredApplications, а не originalApplications
            LocalDate appDate = LocalDate.parse(application.getDate(), formatter);
            if (appDate.isAfter(startDate) && appDate.isBefore(endDate)) {
                filteredApplications.add(application);
            }
        }
    }

}