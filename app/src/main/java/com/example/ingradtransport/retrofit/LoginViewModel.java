package com.example.ingradtransport.retrofit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {
    public MutableLiveData<String> token = new MutableLiveData<>();

    public LiveData<String> getToken() {
        return token;
    }



//    private MutableLiveData<String> token = new MutableLiveData<>();
//
//    public MutableLiveData<String> getToken() {
//        return token;
//    }
//
//    public void setToken(MutableLiveData<String> token) {
//        this.token = token;
//    }

}
