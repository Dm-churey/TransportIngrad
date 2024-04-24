package com.example.ingradtransport.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ingradtransport.R;
import com.example.ingradtransport.model.Application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ApplicationAdapter extends ListAdapter<Application, ApplicationAdapter.Holder> {

    private List<Application> originalApplications;
    static class Holder extends RecyclerView.ViewHolder {
        private final TextView textPurpose;
        private final TextView textDate;
        private final Button button_details;

        public Holder(View itemView) {
            super(itemView);
            textPurpose = itemView.findViewById(R.id.appl_item_purpose);
            textDate = itemView.findViewById(R.id.appl_item_date);
            button_details = itemView.findViewById(R.id.button_details);
            button_details.setText("Подробнее");
        }

        public void bind(Application application) {
            textPurpose.setText(application.getPurpose());
            textDate.setText("Дата: " + application.getDate());
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
    public ApplicationAdapter(Context context) {
        super(new ApplicationAdapter.Comparator());
        this.context = context;
        this.originalApplications = new ArrayList<>(); // Инициализация пустым списком
    }

    @NonNull
    @Override
    public ApplicationAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.application_item, parent, false);
        return new ApplicationAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationAdapter.Holder holder, int position) {
        Application application = getItem(position);
        holder.bind(application);
        holder.itemView.setVisibility(View.VISIBLE);


        holder.button_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfWindow(application);
            }
        });
    }

    private void showInfWindow(Application application) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.custom_dialog);
        //dialog.getWindow().setBackgroundDrawableResource(R.color.yellow);
        dialog.setTitle("Заявка");
        dialog.setMessage("Информация о поздке");

        LayoutInflater inflater = LayoutInflater.from(context);
        View information_window = inflater.inflate(R.layout.information_window, null);
        dialog.setView(information_window);

        TextView purpose = information_window.findViewById(R.id.appl_item_purpose);
        TextView address = information_window.findViewById(R.id.appl_item_address);
        TextView date = information_window.findViewById(R.id.appl_item_date);
        TextView client = information_window.findViewById(R.id.appl_item_client);
        TextView start = information_window.findViewById(R.id.appl_item_start);
        TextView finish = information_window.findViewById(R.id.appl_item_finish);
        TextView comment = information_window.findViewById(R.id.appl_item_comment);
        TextView status = information_window.findViewById(R.id.appl_item_status);
        TextView driver = information_window.findViewById(R.id.appl_item_driver);

        purpose.setText(application.getPurpose());
        address.setText(application.getAddress());
        date.setText(application.getDate());
        client.setText(application.getUser_lastname() + " " + application.getUser_name() + " " + application.getUser_patronymic());
        start.setText(application.getStart_time());
        finish.setText(application.getFinish_time());
        comment.setText(application.getComment());
        status.setText(application.getStatus());
        driver.setText(application.getDriver_lastname() + " " + application.getDriver_name() + " " + application.getDriver_patronymic());

        dialog.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
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

}

//    private Map<Application, Boolean> itemVisibilityMap = new HashMap<>();
//
//    @Override
//    public void onBindViewHolder(@NonNull Holder holder, int position) {
//        Application application = getItem(position);
//        boolean isVisible = itemVisibilityMap.getOrDefault(application, true);
////        Boolean isVisible = itemVisibilityMap.get(application); // Извлекаем значение без использования getOrDefault
////        if (isVisible == null) {
////            isVisible = true; // Устанавливаем значение по умолчанию, если ключ не найден
////        }
//
//        if (isVisible) {
//        //if (Boolean.TRUE.equals(isVisible)) {
//            holder.bind(application);
//            holder.itemView.setVisibility(View.VISIBLE);
//        } else {
//            holder.itemView.setVisibility(View.GONE);
//        }
//
//        holder.button_details.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showInfWindow(application);
//            }
//        });
//
//    public void setItemVisibility(Application application, boolean isVisible) {
//        itemVisibilityMap.put(application, isVisible);
//        notifyDataSetChanged();
//    }
//
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
//
//}
