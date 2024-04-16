package com.example.ingradtransport;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ingradtransport.bossfragment.BossActivity;
import com.example.ingradtransport.driverFragment.DriverActivity;
import com.example.ingradtransport.preferences.SharedPreferences;
import com.example.ingradtransport.model.AuthRequest;
import com.example.ingradtransport.retrofit.LoginViewModel;
import com.example.ingradtransport.retrofit.MainApi;
import com.example.ingradtransport.retrofit.RetrofitClient;
import com.example.ingradtransport.model.User;
import com.example.ingradtransport.workerFragment.WorkerActivity;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class EntranceActivity extends AppCompatActivity {

    private ImageButton back_button;
    private Button vxod_button;
    private EditText edit_login, edit_password;
    private TextView authorization_text, podskaz2_text;
    RelativeLayout root;
    private MainApi mainApi;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);

        back_button = findViewById(R.id.back_button);
        vxod_button = findViewById(R.id.vxod_button);
        edit_login = findViewById(R.id.edit_login);
        edit_password = findViewById(R.id.edit_password);
        authorization_text = findViewById(R.id.authorization_text);
        podskaz2_text = findViewById(R.id.podskaz2_text);
        root = findViewById(R.id.root_entrance);
        EditText login = edit_login;
        EditText password = edit_password;

        mainApi = RetrofitClient.getInstance().getMainApi();
        //viewModel = new ViewModelProvider(MainActivity()).get(LoginViewModel.class);
        vxod_button.setOnClickListener(v ->
                        login(new AuthRequest(
                                login.getText().toString(),
                                password.getText().toString()
                        ))
        );

        back_button.setOnClickListener(v -> {
            Intent intent = new Intent(EntranceActivity.this, FirstPage.class);
            startActivity(intent);
        });
    }

    private void login(AuthRequest authRequest) {
        new Thread(() -> {
            try {
                retrofit2.Response<User> response = mainApi.login(authRequest).execute();
                String message = null;
                if (response.errorBody() != null) {
                    message = new JSONObject(response.errorBody().string()).getString("message" );
                }
                String finalMessage = message;
                runOnUiThread(() -> {
                    Snackbar.make(root, finalMessage, Snackbar.LENGTH_LONG).setTextColor(0XFF101820).setBackgroundTint(0XFFFFFFFF).show();
                    User user = response.body();
                    if (user != null) {
                        SharedPreferences pref= new SharedPreferences(this);
                        pref.setUser(user);

                        if (user.getPost().equals("начальник транспортного отдела")) {

                            Intent intent = new Intent(EntranceActivity.this, BossActivity.class);
                            startActivity(intent);
                            //viewModel.token.setValue(user.getToken());
                            Snackbar.make(root, user.getMessage(), Snackbar.LENGTH_SHORT).setTextColor(0XFF101820).setBackgroundTint(0XFFFFFFFF).show();
                        }
                        if (user.getPost().equals("водитель")) {

                            Intent intent = new Intent(EntranceActivity.this, DriverActivity.class);
                            startActivity(intent);
                            //viewModel.token.setValue(user.getToken());
                            Snackbar.make(root, user.getMessage(), Snackbar.LENGTH_SHORT).setTextColor(0XFF101820).setBackgroundTint(0XFFFFFFFF).show();
                        }
                        if (user.getPost().equals("сотрудник")) {

                            Intent intent = new Intent(EntranceActivity.this, WorkerActivity.class);
                            startActivity(intent);
                            //viewModel.token.setValue(user.getToken());
                            Snackbar.make(root, user.getMessage(), Snackbar.LENGTH_SHORT).setTextColor(0XFF101820).setBackgroundTint(0XFFFFFFFF).show();
                        }
                    } //else {
                        //Snackbar.make(root, "Ошибка вохда", Snackbar.LENGTH_SHORT).setTextColor(0XFF101820).setBackgroundTint(0XFFFFFFFF).show();
                    //}
                });
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }

}