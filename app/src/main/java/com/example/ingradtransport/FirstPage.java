package com.example.ingradtransport;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ingradtransport.bossfragment.BossActivity;
import com.example.ingradtransport.driverFragment.DriverActivity;
import com.example.ingradtransport.preferences.SharedPreferences;
import com.example.ingradtransport.model.AuthCheckRequest;
import com.example.ingradtransport.retrofit.MainApi;
import com.example.ingradtransport.retrofit.RetrofitClient;
import com.example.ingradtransport.model.User;
import com.example.ingradtransport.workerFragment.WorkerActivity;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstPage extends AppCompatActivity {

    private TextView text_name;
    private Button vx_button, reg_button;
    private MainApi mainApi;
    RelativeLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initRetrofit();
        mainApi = RetrofitClient.getInstance().getMainApi();
        new Thread(() -> {
            try {
                SharedPreferences pref= new SharedPreferences(this);
                User user = pref.getUser();
                AuthCheckRequest authCheckRequest = new AuthCheckRequest(user.getToken(), user.getId());
                retrofit2.Response<JSONObject> response = mainApi.checkAuth(authCheckRequest).execute();
                runOnUiThread(() -> {
                    if (response.code() == 200) {
                        if (user.getPost().equals("начальник транспортного отдела")) {
                            Intent intent = new Intent(FirstPage.this, BossActivity.class);
                            startActivity(intent);
                        }
                        if (user.getPost().equals("водитель")) {
                            Intent intent = new Intent(FirstPage.this, DriverActivity.class);
                            startActivity(intent);
                        }
                        if (user.getPost().equals("сотрудник")) {
                            Intent intent = new Intent(FirstPage.this, WorkerActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        setContentView(R.layout.activity_first_page);

        text_name = findViewById(R.id.text_name);
        vx_button = findViewById(R.id.vx_button);
        reg_button = findViewById(R.id.reg_button);

        root = findViewById(R.id.root_element);

        vx_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstPage.this, EntranceActivity.class);
                startActivity(intent);
            }
        });

        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {showRegisterWindow();}
        });
    }

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


//   public void startNewActivity(View v) {
//       Intent intent = new Intent(this, EntranceActivity.class);
//       startActivity(intent);
//   }

    private void showRegisterWindow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.custom_dialog);
        builder.setTitle("Зарегистрироваться");
        builder.setMessage("Введите все данные для регистрации");

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_window = inflater.inflate(R.layout.register_window, null);
        builder.setView(register_window);

        EditText name_et = register_window.findViewById(R.id.name_field);
        EditText lastname_et = register_window.findViewById(R.id.lastname_field);
        EditText patronymic_et = register_window.findViewById(R.id.patronymic_field);
        EditText age_et = register_window.findViewById(R.id.age_field);
        EditText phone_et = register_window.findViewById(R.id.phone_field);
        EditText login_et = register_window.findViewById(R.id.login_field);
        EditText password_et = register_window.findViewById(R.id.password_field);
        Spinner post_et = register_window.findViewById(R.id.post_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.post_spin, android.R.layout.simple_spinner_dropdown_item);
        post_et.setAdapter(adapter);
        post_et.setSelection(0);

        builder.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
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

                String name = name_et.getText().toString();
                String lastname = lastname_et.getText().toString();
                String patronymic = patronymic_et.getText().toString();
                String age1 = age_et.getText().toString();
                int age = 0;
                //int age = Integer.parseInt(age_et.getText().toString());
                String phone = phone_et.getText().toString();
                String login = login_et.getText().toString();
                String password = password_et.getText().toString();
                String post = post_et.getSelectedItem().toString();

                if(name.isEmpty() || lastname.isEmpty() || patronymic.isEmpty() || TextUtils.isEmpty(age1) || phone.length() < 11 || login.isEmpty() || password.length() < 5 || post_et.getSelectedItemPosition() == 0) {
                    if(TextUtils.isEmpty(name)) {
                        Snackbar.make(register_window, "Введите ваше имя", Snackbar.LENGTH_LONG).setTextColor(0XFF101820).setBackgroundTintList(ColorStateList.valueOf(Color.WHITE)).show();
                    }

                    if(TextUtils.isEmpty(lastname)) {
                        Snackbar.make(register_window, "Введите вашу фамилию", Snackbar.LENGTH_LONG).setTextColor(0XFF101820).setBackgroundTintList(ColorStateList.valueOf(Color.WHITE)).show();
                    }

                    if(TextUtils.isEmpty(patronymic)) {
                        Snackbar.make(register_window, "Введите ваше отчетсво", Snackbar.LENGTH_LONG).setTextColor(0XFF101820).setBackgroundTintList(ColorStateList.valueOf(Color.WHITE)).show();
                    }

                    if (TextUtils.isEmpty(age1)) {
                        Snackbar.make(register_window, "Введите ваш возраст", Snackbar.LENGTH_LONG).setTextColor(0XFF101820).setBackgroundTintList(ColorStateList.valueOf(Color.WHITE)).show();
                    }

                    if(phone.length() < 11) {
                        Snackbar.make(register_window, "Введите ваш номер телефона", Snackbar.LENGTH_LONG).setTextColor(0XFF101820).setBackgroundTintList(ColorStateList.valueOf(Color.WHITE)).show();
                    }

                    if(TextUtils.isEmpty(login)) {
                        Snackbar.make(register_window, "Введите логин для личного кабинета", Snackbar.LENGTH_LONG).setTextColor(0XFF101820).setBackgroundTintList(ColorStateList.valueOf(Color.WHITE)).show();
                    }

                    if(password.length() < 5) {
                        Snackbar.make(register_window, "Введите пароль, который больше 5 символов", Snackbar.LENGTH_LONG).setTextColor(0XFF101820).setBackgroundTintList(ColorStateList.valueOf(Color.WHITE)).show();
                    }

                    if(post_et.getSelectedItemPosition() == 0) {
                       Snackbar.make(register_window, "Выберите Вашу должность из списка", Snackbar.LENGTH_LONG).setTextColor(0XFF101820).setBackgroundTintList(ColorStateList.valueOf(Color.WHITE)).show();
                    }
                } else {
                    try {
                        age = Integer.parseInt(age1); // Попытка преобразования строки в число
                        if(age < 18) {
                            Snackbar.make(register_window, "Введите корректный возраст", Snackbar.LENGTH_LONG).setTextColor(0XFF101820).setBackgroundTintList(ColorStateList.valueOf(Color.WHITE)).show();
                        } else {
                            isValid = true;
                        }
                    } catch (NumberFormatException e) {
                        // Обработка ошибки преобразования, если строка содержит нечисловые символы
                        Snackbar.make(register_window, "Введите возраст", Snackbar.LENGTH_SHORT).show();
                    }
                }

                if(isValid) {
                    createUser(name, lastname, patronymic, age, phone, login, password, post);
                    dialog.dismiss();
                }
            }
        });
    }

    private void createUser(String name, String lastname, String patronymic, int age, String phone, String login, String password, String post) {
        mainApi = RetrofitClient.getInstance().getMainApi();

        User user = new User(name, lastname, patronymic, age, phone, login, password, post);
        Call<User> call = mainApi.register(user);

        final Context context = this;
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    Snackbar.make(root, "Пользователь зарегистрирован", Snackbar.LENGTH_LONG).setTextColor(0XFF101820).setBackgroundTint(0XFFFFFFFF).show();
                    User user = response.body();
                    if (user != null) {
                        SharedPreferences pref = new SharedPreferences(context);
                        pref.setUser(user);

                        if (user.getPost().equals("начальник транспортного отдела")) {

                            Intent intent = new Intent(FirstPage.this, BossActivity.class);
                            startActivity(intent);
                            //viewModel.token.setValue(user.getToken());
                            Snackbar.make(root, user.getMessage(), Snackbar.LENGTH_SHORT).setTextColor(0XFF101820).setBackgroundTint(0XFFFFFFFF).show();
                        }
                        if (user.getPost().equals("водитель")) {

                            Intent intent = new Intent(FirstPage.this, DriverActivity.class);
                            startActivity(intent);
                            //viewModel.token.setValue(user.getToken());
                            Snackbar.make(root, user.getMessage(), Snackbar.LENGTH_SHORT).setTextColor(0XFF101820).setBackgroundTint(0XFFFFFFFF).show();
                        }
                        if (user.getPost().equals("сотрудник")) {

                            Intent intent = new Intent(FirstPage.this, WorkerActivity.class);
                            startActivity(intent);
                            //viewModel.token.setValue(user.getToken());
                            Snackbar.make(root, user.getMessage(), Snackbar.LENGTH_SHORT).setTextColor(0XFF101820).setBackgroundTint(0XFFFFFFFF).show();
                        }
                    }
                } else {
                    Snackbar.make(root, "Ошибка при регистрации", Snackbar.LENGTH_LONG).setTextColor(0XFF101820).setBackgroundTint(0XFFFFFFFF).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Snackbar.make(root, "Ошибка сети", Snackbar.LENGTH_LONG).setTextColor(0XFF101820).setBackgroundTint(0XFFFFFFFF).show();
            }
        });
    }

}