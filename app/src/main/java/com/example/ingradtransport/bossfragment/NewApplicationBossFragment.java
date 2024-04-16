package com.example.ingradtransport.bossfragment;

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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ingradtransport.R;
import com.example.ingradtransport.adapter.ApplicationAdapter;
import com.example.ingradtransport.adapter.EarlyApplicationAdapter;
import com.example.ingradtransport.adapter.SortApplAdapter;
import com.example.ingradtransport.model.Application;
import com.example.ingradtransport.model.SortOption;
import com.example.ingradtransport.model.User;
import com.example.ingradtransport.preferences.SharedPreferences;
import com.example.ingradtransport.retrofit.MainApi;
import com.example.ingradtransport.retrofit.RetrofitClient;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewApplicationBossFragment extends Fragment implements EarlyApplicationAdapter.OnDetailsClickListener {
    private ApplicationAdapter adapter;
    private SortApplAdapter adapter_sort;
    private EarlyApplicationAdapter earlyApplicationAdapter;
    private RecyclerView r_view, r_sort;
    private MainApi mainApi;
    private Context mContext;
    private TextView textView;
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
        return inflater.inflate(R.layout.fragment_new_application_boss, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        earlyApplicationAdapter = new EarlyApplicationAdapter(getContext());

        r_view = view.findViewById(R.id.r_view);
        r_view.setLayoutManager(new LinearLayoutManager(getContext()));
        r_view.setAdapter(earlyApplicationAdapter);

        earlyApplicationAdapter.setOnDetailsClickListener(this);

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
        textView = view.findViewById(R.id.text_no);
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
        Spinner driver = inf_new_window.findViewById(R.id.appl_item_spin);

        purpose.setText(application.getPurpose());
        address.setText(application.getAddress());
        date.setText(application.getDate());
        client.setText(application.getUser_lastname() + " " + application.getUser_name() + " " + application.getUser_patronymic());
        start.setText(application.getStart_time());
        finish.setText(application.getFinish_time());
        comment.setText(application.getComment());
        status.setText(application.getStatus());

        // Здесь вы можете добавить код для загрузки списка водителей и установки его в Spinner

        dialog.setNeutralButton("ОК", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.setNegativeButton("Отклонить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Согласовать", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }

}