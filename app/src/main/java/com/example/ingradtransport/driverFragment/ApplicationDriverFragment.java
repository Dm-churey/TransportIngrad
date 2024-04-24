package com.example.ingradtransport.driverFragment;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.ingradtransport.R;
import com.example.ingradtransport.adapter.ApplicationAdapter;
import com.example.ingradtransport.adapter.SortApplAdapter;
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

public class ApplicationDriverFragment extends Fragment {
    private ApplicationAdapter adapter;
    private SortApplAdapter adapter_sort;
    private RecyclerView r_view, r_sort;
    private MainApi mainApi;
    private Context mContext;
    private TextView textView;
    private Button createButton;

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

        textView = view.findViewById(R.id.text_no);
        textView.setVisibility(View.GONE);
        createButton = view.findViewById(R.id.new_appl_button);



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

}