package com.example.ingradtransport.driverFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ingradtransport.R;
import com.example.ingradtransport.adapter.ApplDriverAdapter;
import com.example.ingradtransport.adapter.SortApplDriverAdapter;
import com.example.ingradtransport.model.SortOption;
import com.example.ingradtransport.preferences.SharedPreferences;
import com.example.ingradtransport.model.Application;
import com.example.ingradtransport.retrofit.MainApi;
import com.example.ingradtransport.retrofit.RetrofitClient;
import com.example.ingradtransport.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicationDriverFragment extends Fragment {
    //private ApplicationAdapter adapter;
    private ApplDriverAdapter adapter;
    //private SortEarlyApplAdapter adapter_sort;
    private SortApplDriverAdapter adapter_sort;
    private RecyclerView r_view, r_sort;
    private MainApi mainApi;
    private Context mContext;
    private TextView textView, textNew, textConf;
    List<Application> originalApplications = new ArrayList<>();
    List<Application> statusApplications = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_application_driver, container, false);

        r_view = view.findViewById(R.id.r_view);
        r_sort = view.findViewById(R.id.sortRecycler);

        r_view.setLayoutManager(new LinearLayoutManager(mContext));
        r_sort.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        List<Application> applications = new ArrayList<>();
        adapter = new ApplDriverAdapter(mContext, this);////
        //adapter = new ApplicationAdapter(mContext);
        r_view.setAdapter(adapter);

        List<SortOption> sortOptions = Arrays.asList(
                new SortOption("Все", 1),
                new SortOption("Сегодня", 2),
                new SortOption("Завтра", 3),
                new SortOption("Неделя", 4),
                new SortOption("Месяц", 5)
        );

        //adapter_sort = new SortEarlyApplAdapter(getContext(), sortOptions, sortOption -> {
        adapter_sort = new SortApplDriverAdapter(getContext(), sortOptions, sortOption -> {
            adapter.filterApplications(sortOption.getTitle());
        }, adapter);

        r_sort.setAdapter(adapter_sort);

        textView = view.findViewById(R.id.text_no);
        textView.setVisibility(View.GONE);

        textNew = view.findViewById(R.id.new_appl_drver);
        textNew.setTypeface(Typeface.DEFAULT_BOLD);
        textConf = view.findViewById(R.id.conf_appl_drver);
        textConf.setTypeface(Typeface.DEFAULT);

        textNew.setOnClickListener(view1 -> {
            textNew.setTypeface(Typeface.DEFAULT_BOLD);
            textConf.setTypeface(Typeface.DEFAULT);
            filterApplicationsByStatus("Согласовано");
        });

        textConf.setOnClickListener(view1 -> {
            textNew.setTypeface(Typeface.DEFAULT);
            textConf.setTypeface(Typeface.DEFAULT_BOLD);
            filterApplicationsByStatus("Подтверждено");
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainApi = RetrofitClient.getInstance().getMainApi();
        SharedPreferences pref= new SharedPreferences(mContext);
        User user = pref.getUser();
        new Thread(() -> {
            try {
                retrofit2.Response<List<Application>> response = mainApi.getApplicationByIdDriver(user.getId()).execute();
                if (response.isSuccessful() && response.body() != null) {
                    originalApplications =  response.body();
                    requireActivity().runOnUiThread(() -> {
                        filterApplicationsByStatus("Согласовано");
                    });
                }
                if (response.code() != 200) {
                    requireActivity().runOnUiThread(() -> {
                        textView.setVisibility(View.VISIBLE);
                    });
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private void filterApplicationsByStatus(String status) {
        List<Application> filterApplications = new ArrayList<>();
        for (Application application : originalApplications) {
            if (application.getStatus().equals(status)) {
                filterApplications.add(application);
            }
        }
        statusApplications = filterApplications;
        adapter.submitList(statusApplications);
        adapter.updateOriginalApplications(statusApplications);
        //adapter.filterApplicationsByStatusAndDate(filterApplications);
    }

    public void showDialogForApplication(Application application) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.custom_dialog);
        builder.setTitle("Информация о заявке");
        builder.setMessage("Информация о поздке");

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View information_window = inflater.inflate(R.layout.information_window, null);
        builder.setView(information_window);

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

        String positiveButtonText = application.getStatus().equals("Согласовано")? "Подтвердить" : "ОК";
        if (positiveButtonText.equals("Подтвердить")) {
            builder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    acceptApplication(application.getId());
                    dialog.dismiss();
                }
            });
            builder.setNeutralButton("Назад", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else {
            builder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        builder.create().show();
    }

    private void acceptApplication(int application_id) {
        mainApi = RetrofitClient.getInstance().getMainApi();
        Call<Void> call = mainApi.acceptApplDriver(application_id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Обработка успешного согласования
                    Toast.makeText(mContext, "Заявка подтверждена успешно", Toast.LENGTH_SHORT).show();
                } else {
                    // Обработка ошибки обновления
                    Toast.makeText(mContext, "Ошибка подтверждения заявки", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Обработка ошибки сети
                Toast.makeText(mContext, "Ошибка сети", Toast.LENGTH_SHORT).show();
            }
        });
    }

}