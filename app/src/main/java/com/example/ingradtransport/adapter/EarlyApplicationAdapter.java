package com.example.ingradtransport.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ingradtransport.R;
import com.example.ingradtransport.model.Application;
import com.example.ingradtransport.model.User;
import com.example.ingradtransport.retrofit.MainApi;
import com.example.ingradtransport.retrofit.RetrofitClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EarlyApplicationAdapter extends ListAdapter<Application, EarlyApplicationAdapter.Holder> {

    private Map<Application, Boolean> itemVisibilityMap = new HashMap<>();
    private List<Application> originalApplications;
    private OnDetailsClickListener onDetailsClickListener;

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
            button_details.setText("Рассмотреть");
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
    public EarlyApplicationAdapter(Context context) {
        super(new Comparator());
        this.context = context;
        this.originalApplications = new ArrayList<>(); // Инициализация пустым списком
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.application_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Application application = getItem(position);
        holder.bind(application, context);
        holder.itemView.setVisibility(View.VISIBLE);
//        boolean isVisible = itemVisibilityMap.getOrDefault(application, true);
//
//        if (isVisible) {
//            holder.bind(application);
//            holder.itemView.setVisibility(View.VISIBLE);
//        } else {
//            holder.itemView.setVisibility(View.GONE);
//        }

        holder.button_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDetailsClickListener != null) {
                    onDetailsClickListener.onDetailsClick(application);
                }
            }
        });
    }

    public interface OnDetailsClickListener {
        void onDetailsClick(Application application);
    }

    public void setOnDetailsClickListener(OnDetailsClickListener listener) {
        this.onDetailsClickListener = listener;
    }

    public void updateOriginalApplications(List<Application> applications) {
        this.originalApplications = new ArrayList<>(applications);
    }

    public void filterApplications(String filter) {
        List<Application> filteredApplications = new ArrayList<>();
        switch (filter) {
            case "Все":
                filteredApplications.addAll(originalApplications);
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
                // Если выбран неизвестный фильтр, не применяем фильтрацию
        }
        submitList(filteredApplications); // Обновляем адаптер с отфильтрованным списком
    }

    private void filterByDate(List<Application> filteredApplications, LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for (Application application : originalApplications) {
            LocalDate appDate = LocalDate.parse(application.getDate(), formatter);
            if (appDate.isEqual(date)) {
                filteredApplications.add(application);
            }
        }
    }

    private void filterByDateRange(List<Application> filteredApplications, LocalDate startDate, LocalDate endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for (Application application : originalApplications) {
            LocalDate appDate = LocalDate.parse(application.getDate(), formatter);
            if (appDate.isAfter(startDate) && appDate.isBefore(endDate)) {
                filteredApplications.add(application);
            }
        }
    }

//    public void setItemVisibility(Application application, boolean isVisible) {
//        itemVisibilityMap.put(application, isVisible);
//        notifyDataSetChanged();
//    }

//    public void filterApplications(List<Application> applications, String filter) { // Скрытие ненужних заявок в списке при фильтрации по выбранной опции
//        for (Application application : applications) {
//            setItemVisibility(application, true);
//        }
//
//        switch (filter) {
//            case "Все":
//                break;
//            case "Сегодня":
//                filterByDate(applications, LocalDate.now());
//                break;
//            case "Завтра":
//                filterByDate(applications, LocalDate.now().plusDays(1));
//                break;
//            case "Неделя":
//                filterByDateRange(applications, LocalDate.now(), LocalDate.now().plusWeeks(1));
//                break;
//            case "Месяц":
//                filterByDateRange(applications, LocalDate.now(), LocalDate.now().plusMonths(1));
//                break;
//            default:
//                // Если выбран неизвестный фильтр, не применяем фильтрацию
//        }
//
//    }
//
//    private void filterByDate(List<Application> applications, LocalDate date) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//        for (Application application : applications) {
//            LocalDate appDate = LocalDate.parse(application.getDate(), formatter);
//            setItemVisibility(application, appDate.isEqual(date));
//        }
//    }
//    private void filterByDateRange(List<Application> applications, LocalDate startDate, LocalDate endDate) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//        for (Application application : applications) {
//            LocalDate appDate = LocalDate.parse(application.getDate(), formatter);
//            setItemVisibility(application, appDate.isAfter(startDate) && appDate.isBefore(endDate));
//        }
//    }

}
