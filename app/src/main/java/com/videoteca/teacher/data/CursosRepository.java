package com.videoteca.teacher.data;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.videoteca.teacher.data.model.retrofit.RespCursos;
import com.videoteca.teacher.data.rest.ApiClient;
import com.videoteca.teacher.data.rest.ApiInterface;
import com.videoteca.teacher.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CursosRepository {
    private static volatile CursosRepository instance;

    public static CursosRepository getInstance(){
        if (instance == null){
            instance = new CursosRepository();
        }
        return instance;
    }

    private ApiInterface apiInterface;

    public CursosRepository() {
        this.apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    public MutableLiveData<RespCursos> getCursos(String authorization){
        MutableLiveData<RespCursos> cursosData = new MutableLiveData<>();
        apiInterface.getCursos(Constants.KEY.Flag.concat(" ").concat(authorization)).enqueue(new Callback<RespCursos>() {
            @Override
            public void onResponse(Call<RespCursos> call, Response<RespCursos> response) {
                int statusCode = response.code();
                Log.i("STATUS CODE", String.valueOf(statusCode));
                if (statusCode == 200) {
                    cursosData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespCursos> call, Throwable t) {
                cursosData.setValue(null);
            }
        });
        return cursosData;
    }
}
