package com.videoteca.teacher.data;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;


import com.videoteca.teacher.data.model.retrofit.RespUserLogout;
import com.videoteca.teacher.data.model.retrofit.RespUserLogin;
import com.videoteca.teacher.data.model.retrofit.UserLogin;
import com.videoteca.teacher.data.rest.ApiClient;
import com.videoteca.teacher.data.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepositoryR2 {

    private static volatile LoginRepositoryR2 instance;

    public static LoginRepositoryR2 getInstance(){
        if (instance == null){
            instance = new LoginRepositoryR2();
        }
        return instance;
    }

    private ApiInterface apiInterface;

    public LoginRepositoryR2(){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    public MutableLiveData<RespUserLogin> postUsuario(String username, String password){
        UserLogin userLogin = new UserLogin(username, password, "T");
        MutableLiveData<RespUserLogin> userLoginData = new MutableLiveData<>();


        apiInterface.postLogin(userLogin).enqueue(new Callback<RespUserLogin>() {
            @Override
            public void onResponse(Call<RespUserLogin> call, Response<RespUserLogin> response) {
                int statusCode = response.code();
                Log.i("STATUS CODE", String.valueOf(statusCode));
                if (statusCode == 200) {
                    userLoginData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespUserLogin> call, Throwable t) {
                userLoginData.setValue(null);
            }
        });

        return userLoginData;
    }

}
