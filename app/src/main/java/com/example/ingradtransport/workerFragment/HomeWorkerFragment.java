package com.example.ingradtransport.workerFragment;

import android.content.Context;
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
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.ingradtransport.R;
import com.example.ingradtransport.adapter.NewsAdapter;
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

public class HomeWorkerFragment extends Fragment {

    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private Context mContext;
    private MainApi mainApi;
    ScrollView root;
    TextView textView, textView_1, textView_2;
    List<News> originalNewsList = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_worker, container, false);

        recyclerView = view.findViewById(R.id.home_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        newsAdapter = new NewsAdapter(mContext, new ArrayList<>());
        recyclerView.setAdapter(newsAdapter);

        textView_1 = view.findViewById(R.id.sort_news);
        textView_1.setTypeface(Typeface.DEFAULT_BOLD);
        textView_2 = view.findViewById(R.id.sort_work);
        textView_2.setTypeface(Typeface.DEFAULT);

        textView_1.setOnClickListener(view1 -> {
            textView_1.setTypeface(Typeface.DEFAULT_BOLD);
            textView_2.setTypeface(Typeface.DEFAULT);
            filterNews("новости");
        });

        textView_2.setOnClickListener(view1 -> {
            textView_1.setTypeface(Typeface.DEFAULT);
            textView_2.setTypeface(Typeface.DEFAULT_BOLD);
            filterNews("для сотрудников");
        });

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
                    originalNewsList =  response.body();
                    List<News> filteredNewsList = new ArrayList<>();
                    for (News news : originalNewsList) {
                        if (news.getSection().equals("новости")) {
                            filteredNewsList.add(news);
                        }
                    }
                    newsAdapter.updateNewsList(filteredNewsList);
                } else {
                    textView.setText("Нет новостей");
                }
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                Snackbar.make(root, "Ошибка сети", Snackbar.LENGTH_LONG).setTextColor(0XFF101820).setBackgroundTint(0XFFFFFFFF).show();
            }
        });
    }

    private void filterNews(String section) {
        List<News> filteredNewsList = new ArrayList<>();
        for (News news : originalNewsList) {
            if (news.getSection().equals(section)) {
                filteredNewsList.add(news);
            }
        }
        newsAdapter.updateNewsList(filteredNewsList);
    }

}