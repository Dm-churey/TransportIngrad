package com.example.ingradtransport.workerFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.ingradtransport.FirstPage;
import com.example.ingradtransport.R;

import com.example.ingradtransport.preferences.SharedPreferences;
import com.example.ingradtransport.model.AuthCheckRequest;
import com.example.ingradtransport.retrofit.MainApi;
import com.example.ingradtransport.retrofit.RetrofitClient;
import com.example.ingradtransport.model.User;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

public class AccountWorkerFragment extends Fragment {
    private Button exit_button;
    private MainApi mainApi;
    private Context mContext;
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
        View view = inflater.inflate(R.layout.fragment_account_worker, container, false);

        root = view.findViewById(R.id.account_worker);
        exit_button = view.findViewById(R.id.exit_button);

        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitSession();
            }
        });

        return view;
    }

    private void exitSession() {
        mainApi = RetrofitClient.getInstance().getMainApi();
        new Thread(() -> {
            try{
                SharedPreferences pref= new SharedPreferences(mContext);
                User user = pref.getUser();
                AuthCheckRequest authCheckRequest = new AuthCheckRequest(user.getToken(), 0);
                retrofit2.Response<Void> response = mainApi.deleteSession(authCheckRequest).execute();
                if (response.code() == 200) {
                    user = new User();
                    pref.setUser(user);
                    Intent intent = new Intent(mContext, FirstPage.class);
                    startActivity(intent);
                } else {
                    Snackbar.make(root, "Не удалось выйти из аккаунта", Snackbar.LENGTH_SHORT).setTextColor(0XFF101820).setBackgroundTint(0XD8D8D8).show();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

}