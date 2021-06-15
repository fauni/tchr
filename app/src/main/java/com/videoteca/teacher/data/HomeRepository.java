package com.videoteca.teacher.data;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.videoteca.teacher.data.model.retrofit.RespCursos;
import com.videoteca.teacher.data.model.retrofit.RespUserLogout;
import com.videoteca.teacher.data.rest.ApiClient;
import com.videoteca.teacher.data.rest.ApiInterface;
import com.videoteca.teacher.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeRepository {
    private static volatile HomeRepository instance;

    public static HomeRepository getInstance(){
        if (instance == null){
            instance = new HomeRepository();
        }
        return instance;
    }

    private ApiInterface apiInterface;

    public HomeRepository() {
        this.apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    public MutableLiveData<RespUserLogout> logoutUsuario(String authorization){
        MutableLiveData<RespUserLogout> userLogoutData = new MutableLiveData<>();
        apiInterface.postLogout(Constants.KEY.Flag.concat(" ").concat(authorization)).enqueue(new Callback<RespUserLogout>() {
            @Override
            public void onResponse(Call<RespUserLogout> call, Response<RespUserLogout> response) {
                int statusCode = response.code();
                Log.i("STATUS CODE", String.valueOf(statusCode));
                if (statusCode == 200) {
                    userLogoutData.setValue(response.body());
                }

            }

            @Override
            public void onFailure(Call<RespUserLogout> call, Throwable t) {
                userLogoutData.setValue(null);
            }
        });

        return userLogoutData;
    }
}
