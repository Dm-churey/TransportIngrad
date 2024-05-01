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
import android.widget.TextView;

import com.example.ingradtransport.FirstPage;
import com.example.ingradtransport.R;

import com.example.ingradtransport.preferences.SharedPreferences;
import com.example.ingradtransport.model.AuthCheckRequest;
import com.example.ingradtransport.retrofit.MainApi;
import com.example.ingradtransport.retrofit.RetrofitClient;
import com.example.ingradtransport.model.User;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountWorkerFragment extends Fragment {
    private Button exit_button;
    private MainApi mainApi;
    private Context mContext;
    RelativeLayout root;
    CircleImageView circleImageView;
    TextView text_lastname, text_name, text_patronymic, text_post;

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
        circleImageView = view.findViewById(R.id.image_account);
        text_lastname = view.findViewById(R.id.account_lastname);
        text_name = view.findViewById(R.id.account_name);
        text_patronymic = view.findViewById(R.id.account_patronymic);
        text_post = view.findViewById(R.id.account_post);

        exit_button = view.findViewById(R.id.exit_button);

        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitSession();
            }
        });

        SharedPreferences pref= new SharedPreferences(mContext);
        User user = pref.getUser();

        if (user.getImage() != null && !user.getImage().isEmpty()) {

            Picasso.get()
                    .load("http://10.0.2.2:8080" + user.getImage())
                    .placeholder(R.drawable.fon_load)
                    .error(R.drawable.err_news)
                    .fit()
                    .centerCrop()
                    .into(circleImageView);
        }

        text_lastname.setText(user.getLastname());
        text_name.setText(user.getName());
        text_patronymic.setText(user.getPatronymic());
        text_post.setText(user.getPost());

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