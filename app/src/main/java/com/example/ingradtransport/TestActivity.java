//package com.example.ingradtransport;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.lifecycle.LifecycleOwner;
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModel;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.ListAdapter;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.ingradtransport.adapter.ApplicationAdapter;
//import com.example.ingradtransport.model.Application;
//import com.example.ingradtransport.retrofit.LoginViewModel;
//import com.example.ingradtransport.retrofit.MainApi;
//
//
//import java.io.IOException;
//import java.util.List;
//import java.util.concurrent.Executor;
//import java.util.concurrent.Executors;
//
//import okhttp3.OkHttpClient;
//import okhttp3.logging.HttpLoggingInterceptor;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.HttpException;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class TestActivity extends AppCompatActivity {
//
//    private ApplicationAdapter adapter;
//    private RecyclerView r_view;
//    private MainApi mainApi;
//    private LoginViewModel viewModel;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test);
//
//        r_view = findViewById(R.id.r_view);
//        r_view.setLayoutManager(new LinearLayoutManager(this));
//
//        adapter = new ApplicationAdapter();
//        r_view.setAdapter(adapter);
//
////        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
////        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
////
////        OkHttpClient client = new OkHttpClient.Builder()
////                .addInterceptor(interceptor)
////                .build();
////
////        Retrofit retrofit = new Retrofit.Builder()
////                .baseUrl("http://10.0.2.2:8080")
////                .client(client)
////                .addConverterFactory(GsonConverterFactory.create())
////                .build();
////
////        MainApi mainApi = retrofit.create(MainApi.class);
//
//        initRetrofit();
//        initRcView();
////        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
////        viewModel.token.observe(this, token -> {
////            Executors.newSingleThreadExecutor().execute(() -> {
////                try {
////                    Response<List<Application>> response = mainApi.getAllApplication(token).execute();
////                    if (response.isSuccessful() && response.body() != null) {
////                        List<Application> application = response.body();
////                        runOnUiThread(() -> {
////                            application.sort(new Application.ApplicationComparator()); // сортировка по дате
////                            adapter.submitList(application);
////                        });
////                    }
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////            });
////        });
//        //viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
//        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
//        LiveData<String> token = viewModel.getToken();
//        token.observe(this, new Observer<String>() {
//        //viewModel.getToken().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(String token) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Response<List<Application>> response = mainApi.getAllApplication(token).execute();
//                            if (response.isSuccessful() && response.body() != null) {
//                                List<Application> application = response.body();
//                                runOnUiThread(() -> {
//                                    application.sort(new Application.ApplicationComparator()); // сортировка по дате
//                                    adapter.submitList(application);
//                                });
//                            }
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }).start();
//            }
//        });
//
////        Call<List<Application>> call = mainApi.getAllApplication();
////        call.enqueue(new Callback<List<Application>>() {
////            @Override
////            public void onResponse(Call<List<Application>> call, Response<List<Application>> response) {
////                if (response.isSuccessful() && response.body() != null) {
////                    List<Application> application = response.body();
////                    application.sort(new Application.ApplicationComparator()); // сортировка по дате
////                    adapter.submitList(application);
////                }
////            }
////
////            @Override
////            public void onFailure(Call<List<Application>> call, Throwable t) {
////                Log.e("Error", t.getMessage());
////            }
////        });
//    }
//
//    private void initRetrofit() {
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(interceptor)
//                .build();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:8080")
//                .client(client)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        mainApi = retrofit.create(MainApi.class);
//    }
//
//    private void initRcView() {
//        r_view = findViewById(R.id.r_view);
//        r_view.setLayoutManager(new LinearLayoutManager(this));
//        adapter = new ApplicationAdapter();
//        r_view.setAdapter(adapter);
//    }
//
//}