package com.example.ingradtransport.bossfragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ingradtransport.R;
import com.example.ingradtransport.adapter.ApplicationAdapter;
import com.example.ingradtransport.adapter.DriverAdapter;
import com.example.ingradtransport.adapter.EarlyApplicationAdapter;
import com.example.ingradtransport.adapter.SortApplAdapter;
import com.example.ingradtransport.adapter.SortEarlyApplAdapter;
import com.example.ingradtransport.model.Application;
import com.example.ingradtransport.model.ApproveAppl;
import com.example.ingradtransport.model.NotApproveAppl;
import com.example.ingradtransport.model.SortOption;
import com.example.ingradtransport.model.User;
import com.example.ingradtransport.preferences.SharedPreferences;
import com.example.ingradtransport.retrofit.MainApi;
import com.example.ingradtransport.retrofit.RetrofitClient;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewApplicationBossFragment extends Fragment implements EarlyApplicationAdapter.OnDetailsClickListener {
    //private ApplicationAdapter adapter;
    //private SortApplAdapter adapter_sort;
    private SortEarlyApplAdapter adapter_sort;
    private EarlyApplicationAdapter earlyApplicationAdapter;
    private RecyclerView r_view, r_sort;
    private MainApi mainApi;
    private Context mContext;
    private TextView textView;
    RelativeLayout root;
    CardView inf_new_window;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_application_boss, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        r_view = view.findViewById(R.id.r_view);
        r_sort = view.findViewById(R.id.sortRecycler);

        r_view.setLayoutManager(new LinearLayoutManager(getContext()));
        r_sort.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        List<Application> applications = new ArrayList<>();
        earlyApplicationAdapter = new EarlyApplicationAdapter(getContext());
        r_view.setAdapter(earlyApplicationAdapter);

        earlyApplicationAdapter.setOnDetailsClickListener(this);

        List<SortOption> sortOptions = Arrays.asList(
                new SortOption("Все", 1),
                new SortOption("Сегодня", 2),
                new SortOption("Завтра", 3),
                new SortOption("Неделя", 4),
                new SortOption("Месяц", 5)
        );

        adapter_sort = new SortEarlyApplAdapter(getContext(), sortOptions, sortOption -> {
            earlyApplicationAdapter.filterApplications(sortOption.getTitle());
        }, earlyApplicationAdapter);

        r_sort.setAdapter(adapter_sort);

        //adapter = new ApplicationAdapter(mContext);
        //r_view.setAdapter(adapter);

//        r_sort = view.findViewById(R.id.sortRecycler);
//        r_sort.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
//
//        List<SortOption> sortOptions = Arrays.asList(
//                new SortOption("Все", 1),
//                new SortOption("Сегодня", 2),
//                new SortOption("Завтра", 3),
//                new SortOption("Неделя", 4),
//                new SortOption("Месяц", 5)
//        );
//
//        adapter_sort = new SortApplAdapter(getContext(), sortOptions, sortOption -> {
//            adapter.filterApplications(adapter.getCurrentList(), sortOption.getTitle());
//        }, adapter);
//
//        r_sort.setAdapter(adapter_sort);

        root = view.findViewById(R.id.rt_elem);
        textView = view.findViewById(R.id.text_no_new);
        textView.setVisibility(View.GONE);


        mainApi = RetrofitClient.getInstance().getMainApi();
        SharedPreferences pref= new SharedPreferences(mContext);
        User user = pref.getUser();
        new Thread(() -> {
            try {
                retrofit2.Response<List<Application>> response = mainApi.getNewApplBoss(user.getToken()).execute();
                if (response.isSuccessful() && response.body() != null) {
                    List<Application> application = response.body();
                    requireActivity().runOnUiThread(() -> {
                        application.sort(new Application.ApplicationComparator()); // сортировка по дате
                        earlyApplicationAdapter.submitList(application);
                        earlyApplicationAdapter.updateOriginalApplications(application);
                        //adapter.submitList(application);
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

    @Override
    public void onDetailsClick(Application application) {
        showInfWindow(application);
    }

    private void showInfWindow(Application application) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext, R.style.custom_dialog);
        dialog.setTitle("Заявка");
        dialog.setMessage("Информация о поздке");

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View inf_new_window = inflater.inflate(R.layout.inf_new_window, null);
        dialog.setView(inf_new_window);

        TextView purpose = inf_new_window.findViewById(R.id.appl_item_purpose);
        TextView address = inf_new_window.findViewById(R.id.appl_item_address);
        TextView date = inf_new_window.findViewById(R.id.appl_item_date);
        TextView client = inf_new_window.findViewById(R.id.appl_item_client);
        TextView start = inf_new_window.findViewById(R.id.appl_item_start);
        TextView finish = inf_new_window.findViewById(R.id.appl_item_finish);
        TextView comment = inf_new_window.findViewById(R.id.appl_item_comment);
        TextView status = inf_new_window.findViewById(R.id.appl_item_status);
        Spinner driverSpinner = inf_new_window.findViewById(R.id.appl_item_spin);

        purpose.setText(application.getPurpose());
        address.setText(application.getAddress());
        date.setText(application.getDate());
        client.setText(application.getUser_lastname() + " " + application.getUser_name() + " " + application.getUser_patronymic());
        start.setText(application.getStart_time());
        finish.setText(application.getFinish_time());
        comment.setText(application.getComment());
        status.setText(application.getStatus());
        // Загрузка списка водителей
        loadDrivers(driverSpinner);

        dialog.setNeutralButton("ОК", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.setNegativeButton("Отклонить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext(), R.style.custom_dialog_1);
                dialog.setTitle("Отклонение заявки");
                dialog.setMessage("Укажите причину отказа");

                LayoutInflater inflater = LayoutInflater.from(mContext);
                View not_approve_window = inflater.inflate(R.layout.not_approve_window, null);
                dialog.setView(not_approve_window);

                RadioGroup radioGroup = not_approve_window.findViewById(R.id.not_approve_radio);

                //deleteApplication(application.getId());
                //dialogInterface.dismiss();

                dialog.setNeutralButton("Назад", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                dialog.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        RadioButton radioButton = not_approve_window.findViewById(selectedId);
                        String selectedReason = radioButton.getText().toString();
                        if (selectedReason != null) {
                            notApproveApplication(application.getId(), selectedReason);
                        }
                        dialogInterface.dismiss();
                    }
                });
                dialog.show();
            }
        });

        dialog.setPositiveButton("Утвердить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                User selectedDriver = (User) driverSpinner.getSelectedItem();
                if (selectedDriver != null) {
                    // Сетевой запрос для обновления заявки
                    approveApplication(application.getId(), selectedDriver.getId());
                }
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }

    private void loadDrivers(Spinner spinner) {
        mainApi = RetrofitClient.getInstance().getMainApi();
        Call<List<User>> call = mainApi.getDrivers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<User> drivers = response.body();
                    // Заполнение Spinner данными c помощью кастомного адаптера
                    //Typeface typeface = Typeface.create("sans-serif-light", Typeface.NORMAL);
                    float textSize = 20;
                    DriverAdapter adapter_driver = new DriverAdapter(mContext, drivers, textSize);
                    spinner.setAdapter(adapter_driver);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Snackbar.make(inf_new_window, "Ошибка сети", Snackbar.LENGTH_LONG).setTextColor(0XFF101820).setBackgroundTint(0XFFFFFFFF).show();
            }
        });
    }

    private void approveApplication(int application_id, int driver_id) {
        mainApi = RetrofitClient.getInstance().getMainApi();
        ApproveAppl approveAppl = new ApproveAppl(driver_id);
        Call<ResponseBody> call = mainApi.approveApplication(application_id, approveAppl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Обработка успешного согласования
                    Toast.makeText(mContext, "Заявка одобрена успешно", Toast.LENGTH_SHORT).show();
                } else {
                    // Обработка ошибки обновления
                    Toast.makeText(mContext, "Ошибка обновления заявки", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Обработка ошибки сети
                Toast.makeText(mContext, "Ошибка сети", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void notApproveApplication(int application_id, String fail_comment) {
        mainApi = RetrofitClient.getInstance().getMainApi();
        NotApproveAppl notApproveAppl = new NotApproveAppl(fail_comment);
        Call<ResponseBody> call = mainApi.notApproveApplication(application_id, notApproveAppl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Обработка успешного согласования
                    Toast.makeText(mContext, "Заявка отклонена успешно", Toast.LENGTH_SHORT).show();
                } else {
                    // Обработка ошибки обновления
                    Toast.makeText(mContext, "Ошибка обновления заявки", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Обработка ошибки сети
                Toast.makeText(mContext, "Ошибка сети", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void deleteApplication(int id) {
//        Call<Void> call = mainApi.deleteApplication(id);
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(mContext, "Заявка отклонена успешно", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(mContext, "Ошибка отклонения заявки", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(mContext, "Ошибка сети", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

}