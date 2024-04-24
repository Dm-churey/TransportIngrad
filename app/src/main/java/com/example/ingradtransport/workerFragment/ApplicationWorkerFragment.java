package com.example.ingradtransport.workerFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ingradtransport.R;
import com.example.ingradtransport.adapter.ApplicationAdapter;
import com.example.ingradtransport.adapter.SortApplAdapter;
import com.example.ingradtransport.model.NewApplReq;
import com.example.ingradtransport.model.SortOption;
import com.example.ingradtransport.preferences.SharedPreferences;
import com.example.ingradtransport.model.Application;
import com.example.ingradtransport.retrofit.MainApi;
import com.example.ingradtransport.retrofit.RetrofitClient;
import com.example.ingradtransport.model.User;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicationWorkerFragment extends Fragment {
    private ApplicationAdapter adapter;
    private SortApplAdapter adapter_sort;
    private RecyclerView r_view, r_sort;
    private MainApi mainApi;
    private Context mContext;
    private TextView textView;
    private Button createButton;
    RelativeLayout root;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_application_worker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        r_view = view.findViewById(R.id.r_view);
        r_sort = view.findViewById(R.id.sortRecycler);

        r_view.setLayoutManager(new LinearLayoutManager(mContext));
        r_sort.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        List<Application> applications = new ArrayList<>();
        adapter = new ApplicationAdapter(mContext);
        r_view.setAdapter(adapter);

        List<SortOption> sortOptions = Arrays.asList(
                new SortOption("Все", 1),
                new SortOption("Сегодня", 2),
                new SortOption("Завтра", 3),
                new SortOption("Неделя", 4),
                new SortOption("Месяц", 5)
        );

        adapter_sort = new SortApplAdapter(getContext(), sortOptions, sortOption -> {
            adapter.filterApplications(sortOption.getTitle());
        }, adapter);

        r_sort.setAdapter(adapter_sort);

        root = view.findViewById(R.id.r_elem);
        textView = view.findViewById(R.id.text_no);
        textView.setVisibility(View.GONE);
        createButton = view.findViewById(R.id.new_appl_button);

        createButton.setOnClickListener(view1 ->
                showCreateApplWindow()
        );

        mainApi = RetrofitClient.getInstance().getMainApi();
        SharedPreferences pref= new SharedPreferences(mContext);
        User user = pref.getUser();
        new Thread(() -> {
            try {
                retrofit2.Response<List<Application>> response = mainApi.getApplicationByIdUser(user.getId()).execute();
                if (response.isSuccessful() && response.body() != null) {
                    List<Application> application = response.body();
                    requireActivity().runOnUiThread(() -> {
                        application.sort(new Application.ApplicationComparator()); // сортировка по дате
                        adapter.submitList(application);
                        adapter.updateOriginalApplications(application);
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

    private void showCreateApplWindow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.custom_dialog);
        //dialog.getWindow().setBackgroundDrawableResource(R.color.yellow);
        builder.setTitle("Заказать транспорт");
        builder.setMessage("Введите все данные");

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View register_appl_window = inflater.inflate(R.layout.register_appl_window, null);
        builder.setView(register_appl_window);

        EditText purpose_et = register_appl_window.findViewById(R.id.purpose_field);
        EditText address_et = register_appl_window.findViewById(R.id.address_field);
        EditText date_et = register_appl_window.findViewById(R.id.date_field);
        EditText start_time_et = register_appl_window.findViewById(R.id.start_time_field);
        EditText finish_time_et = register_appl_window.findViewById(R.id.finish_time_field);
        EditText comment_et = register_appl_window.findViewById(R.id.comment_field);

        builder.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Здесь не делаем ничего, так как мы переопределим кнопку ниже для изменения поведения закрытия.
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = false;

                String purpose = purpose_et.getText().toString();
                String address = address_et.getText().toString();
                String date = date_et.getText().toString();
                String start_time = start_time_et.getText().toString();
                String finish_time = finish_time_et.getText().toString();
                String comment = comment_et.getText().toString();

                if(purpose.isEmpty() || address.isEmpty() || date.isEmpty() || start_time.isEmpty() || finish_time.isEmpty() || comment.isEmpty()) {
                    if(purpose.isEmpty()) {
                        Snackbar.make(register_appl_window, "Укажите цель поездки", Snackbar.LENGTH_LONG).setTextColor(0XFF101820).setBackgroundTint(0XFFFFFFFF).show();
                    }

                    if(address.isEmpty()) {
                        Snackbar.make(register_appl_window, "Введите адрес назначения", Snackbar.LENGTH_LONG).setTextColor(0XFF101820).setBackgroundTint(0XFFFFFFFF).show();
                    }

                    if(date.isEmpty()) {
                        Snackbar.make(register_appl_window, "Укажите дату поездки", Snackbar.LENGTH_LONG).setTextColor(0XFF101820).setBackgroundTint(0XFFFFFFFF).show();
                    }

                    if(start_time.isEmpty()) {
                        Snackbar.make(register_appl_window, "Выберете время начала поездки", Snackbar.LENGTH_LONG).setTextColor(0XFF101820).setBackgroundTint(0XFFFFFFFF).show();
                    }

                    if(finish_time.isEmpty()) {
                        Snackbar.make(register_appl_window, "Укажите приблизительное время окончания поездки", Snackbar.LENGTH_LONG).setTextColor(0XFF101820).setBackgroundTint(0XFFFFFFFF).show();
                    }

                    if(comment.isEmpty()) {
                        Snackbar.make(register_appl_window, "Введите комментарий", Snackbar.LENGTH_LONG).setTextColor(0XFF101820).setBackgroundTint(0XFFFFFFFF).show();
                    }
                } else {
                    isValid = true;
                }

                if(isValid) {
                    createAppl(purpose, address, date, start_time, finish_time, comment);
                    dialog.dismiss();
                }
            }
        });
    }

    private void createAppl(String purpose, String address, String date, String start_time, String finish_time, String comment) {
        mainApi = RetrofitClient.getInstance().getMainApi();
        SharedPreferences pref= new SharedPreferences(mContext);
        User user = pref.getUser();

        NewApplReq newApplReq = new NewApplReq(purpose, address, date, start_time, finish_time, comment);
        Call<NewApplReq> call = mainApi.createNewAppl(user.getToken(), newApplReq);;

        call.enqueue(new Callback<NewApplReq>() {
            @Override
            public void onResponse(Call<NewApplReq> call, Response<NewApplReq> response) {
                if (response.isSuccessful()) {
                    Snackbar.make(root, "Заявка подана успешно", Snackbar.LENGTH_LONG).setTextColor(0XFF101820).setBackgroundTint(0XFFFFFFFF).show();
                } else {
                    Snackbar.make(root, "Ошибка при создании", Snackbar.LENGTH_LONG).setTextColor(0XFF101820).setBackgroundTint(0XFFFFFFFF).show();
                }
            }

            @Override
            public void onFailure(Call<NewApplReq> call, Throwable t) {
                Snackbar.make(root, "Ошибка сети", Snackbar.LENGTH_LONG).setTextColor(0XFF101820).setBackgroundTint(0XFFFFFFFF).show();
            }
        });
    }

}