package com.example.ingradtransport.bossfragment;

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
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.ingradtransport.R;
import com.example.ingradtransport.adapter.NewsAdapter;
import com.example.ingradtransport.model.Application;
import com.example.ingradtransport.model.News;
import com.example.ingradtransport.model.User;
import com.example.ingradtransport.preferences.SharedPreferences;
import com.example.ingradtransport.retrofit.MainApi;
import com.example.ingradtransport.retrofit.RetrofitClient;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeBossFragment extends Fragment {

    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private Context mContext;
    private MainApi mainApi;
    ScrollView root;
    TextView textView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_boss, container, false);

        recyclerView = view.findViewById(R.id.home_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        newsAdapter = new NewsAdapter(mContext, new ArrayList<>());
        recyclerView.setAdapter(newsAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainApi = RetrofitClient.getInstance().getMainApi();
        SharedPreferences pref= new SharedPreferences(mContext);
        User user = pref.getUser();

        Call<List<News>> call = mainApi.getAllNews(user.getToken());
        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<News> news = response.body();
                    //news.sort(new News.NewsComparator()); // сортировка по дате
                    //originalApplications = new ArrayList<>(application);
                    newsAdapter.updateNewsList(news);
                    //adapter.updateOriginalApplications(application);
                } else {
                    textView.setText("Нет новостей");
                }
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                //textView.setVisibility(View.VISIBLE);
                Snackbar.make(root, "Ошибка сети", Snackbar.LENGTH_LONG).setTextColor(0XFF101820).setBackgroundTint(0XFFFFFFFF).show();
            }
        });
    }
}