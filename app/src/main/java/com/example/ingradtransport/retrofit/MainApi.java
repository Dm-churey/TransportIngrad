package com.example.ingradtransport.retrofit;

import com.example.ingradtransport.model.Application;
import com.example.ingradtransport.model.ApproveAppl;
import com.example.ingradtransport.model.AuthCheckRequest;
import com.example.ingradtransport.model.AuthRequest;
import com.example.ingradtransport.model.NewApplReq;
import com.example.ingradtransport.model.NotApproveAppl;
import com.example.ingradtransport.model.User;

import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MainApi {
//    @GET("appl/applications/{id}") //получение заявки по ее id
//    Call<Application> getApplicationById(@Path("id") int id);

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("appl/applications")
    Call<List<Application>> getAllApplication(@Header("Authorization") String token);

    @POST("api/user/register") //регистрация
    Call<User> register(@Body User user);

    @POST("api/user/login") //аутентификация
    Call<User> login(@Body AuthRequest authRequest);

    @Headers("Content-Type: application/json; charset=utf-8") //создание заявки
    @POST("appl/applications")
    Call<NewApplReq> createNewAppl(@Header("Authorization") String token, @Body NewApplReq newApplReq);

    @POST("api/user/checkauth") //проверка существующей сессии
    Call<JSONObject> checkAuth(@Body AuthCheckRequest authCheckRequest);

    @HTTP(method = "DELETE", path = "api/user/delsession", hasBody = true) //удаление сессии при выходе из аккаунта
    Call<Void> deleteSession(@Body AuthCheckRequest authCheckRequest);

    @GET("appl/applications/my/{id}") //получение согласованных заявок пользователем по его id
    Call<List<Application>> getApplicationByIdUser(@Path("id") int id);

    @GET("appl/applications/driver/{id}") //получение новых назначений водителем по его id
    Call<List<Application>> getApplicationByIdDriver(@Path("id") int id);

    @Headers("Content-Type: application/json; charset=utf-8") //получение всех Несогласованных заявок начальником
    @GET("appl/applications/new/boss")
    Call<List<Application>> getNewApplBoss(@Header("Authorization") String token);

    @Headers("Content-Type: application/json; charset=utf-8") //получение всех Согласованных заявок начальником
    @GET("appl/applications")
    Call<List<Application>> getAllApplBoss(@Header("Authorization") String token);

    @GET("api/user/drivers") // получение списка всех водителей
    Call<List<User>> getDrivers();

    @PATCH("appl/applications/approve/{id}")  // согласование заявки с обновлением информации о водителе
    Call<ResponseBody> approveApplication(@Path("id") int application_id,
                                          @Body ApproveAppl approveAppl);

    @PATCH("appl/applications/not_approve/{id}")  // отказ в согласовании заявки
    Call<ResponseBody> notApproveApplication(@Path("id") int application_id,
                                             @Body NotApproveAppl notApproveAppl);

    @HTTP(method = "DELETE", path = "appl/applications/{id}", hasBody = true) //удаление заявки при отказе
    Call<Void> deleteApplication(@Path("id") int id);
}
