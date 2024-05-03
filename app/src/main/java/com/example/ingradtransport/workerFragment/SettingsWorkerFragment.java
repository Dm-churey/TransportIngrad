package com.example.ingradtransport.workerFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ingradtransport.R;
import com.example.ingradtransport.driverFragment.DriverActivity;
import com.example.ingradtransport.model.User;
import com.example.ingradtransport.preferences.SharedPreferences;
import com.example.ingradtransport.retrofit.MainApi;
import com.example.ingradtransport.retrofit.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsWorkerFragment extends Fragment {

    private Context mContext;
    private MainApi mainApi;
    private CircleImageView settingsImage;
    private TextView settings_back, settings_go;
    private Uri filePath;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings_worker, container, false);

        settingsImage = view.findViewById(R.id.settings_image);

        SharedPreferences pref= new SharedPreferences(mContext);
        User user = pref.getUser();

        if (user.getImage()!= null &&!user.getImage().isEmpty()) {
            Picasso.get()
                    .load("http://10.0.2.2:8080" + user.getImage())
                    .placeholder(R.drawable.fon_load)
                    .error(R.drawable.err_news)
                    .fit()
                    .centerCrop()
                    .into(settingsImage);
        }

        settings_back = view.findViewById(R.id.settings_back);
        settings_go = view.findViewById(R.id.settings_go);

        settings_back.setOnClickListener(view1 -> {
            Intent intent = new Intent(mContext, WorkerActivity.class);
            startActivity(intent);
        });

        settings_go.setOnClickListener(view1 -> {
            if (filePath!= null) {
                uploadImage();
            } else {
                Toast.makeText(mContext, "Выберите изображение", Toast.LENGTH_SHORT).show();
            }
            //uploadImage();
            //selectImage();
        });

        settingsImage.setOnClickListener(view1 -> {
            selectImage();
        });

        return view;
    }

    ActivityResultLauncher<Intent> pickImageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData()!= null && result.getData().getData()!= null) {
                        filePath = result.getData().getData();

                        Picasso.get()
                                .load(filePath)
                                .placeholder(R.drawable.fon_load)
                                .error(R.drawable.err_news)
                                .fit()
                                .centerCrop()
                                .into(settingsImage);

//                        Bitmap image = null;
//                        try {
//                            image = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(filePath));
//                        } catch (FileNotFoundException e) {
//                            throw new RuntimeException(e);
//                        }
//                        uploadImage(image);
                    }
                }
            }
    );

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        pickImageActivityResultLauncher.launch(intent);
    }

    private void uploadImage() {
        Bitmap image = null;
        try {
            image = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(filePath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] byteArray = stream.toByteArray();
        RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
        String imageUrl = UUID.randomUUID().toString() + ".jpg";
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageUrl, body);

        mainApi = RetrofitClient.getInstance().getMainApi();
        SharedPreferences pref= new SharedPreferences(mContext);
        User user = pref.getUser();

        Call<ResponseBody> call = mainApi.uploadImg(user.getToken(), imagePart);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    pref.updateImage("/uploads/" + imageUrl);
                    Toast.makeText(mContext, "Изображение успешно сохранено", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Не получилось загрузить изображение", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(mContext, "Ошибка сети", Toast.LENGTH_SHORT).show();
            }
        });
    }
}