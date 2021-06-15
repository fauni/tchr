package com.videoteca.teacher.data;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.videoteca.teacher.data.model.retrofit.RespVideos;
import com.videoteca.teacher.data.model.retrofit.Video;
import com.videoteca.teacher.data.rest.ApiClient;
import com.videoteca.teacher.data.rest.ApiInterface;
import com.videoteca.teacher.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CursoRepository {
    private static volatile CursoRepository instance;

    public static CursoRepository getInstance(){
        if (instance == null){
            instance = new CursoRepository();
        }
        return instance;
    }

    private ApiInterface apiInterface;

    public CursoRepository() {
        this.apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    public MutableLiveData<List<Video>> getVideos(String authorization){
        //MutableLiveData<RespVideos> respVideosMutableLiveData = new MutableLiveData<>();
        MutableLiveData<List<Video>> listVideoData = new MutableLiveData<>();
        apiInterface.getVideos(Constants.KEY.Flag.concat(" ").concat(authorization)).enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                int statusCode = response.code();
                Log.i("STATUS CODE", String.valueOf(statusCode));
                if (statusCode == 200) {
                    listVideoData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                listVideoData.setValue(null);
            }
        });

        return listVideoData;
    }
}
