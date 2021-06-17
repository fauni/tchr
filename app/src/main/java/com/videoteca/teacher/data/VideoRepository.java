package com.videoteca.teacher.data;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.videoteca.teacher.data.model.retrofit.ResponseGeneric;
import com.videoteca.teacher.data.model.retrofit.Video;
import com.videoteca.teacher.data.rest.ApiClient;
import com.videoteca.teacher.data.rest.ApiInterface;
import com.videoteca.teacher.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoRepository {

    private static volatile VideoRepository instance;
    public static VideoRepository getInstance(){
        if (instance == null){
            instance = new VideoRepository();
        }
        return instance;
    }

    private ApiInterface apiInterface;

    public VideoRepository(){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    public MutableLiveData<ResponseGeneric> saveUploadVideo(Video video, String authorization) {
        MutableLiveData<ResponseGeneric> videoData = new MutableLiveData<>();
        apiInterface.postUploadVideo(video.getTitulo(), video.getDescripcion(), video.getEstado(), video.getIdCurs(), video.getVideo(), Constants.KEY.Flag.concat(" ").concat(authorization)).enqueue(new Callback<ResponseGeneric>() {
            @Override
            public void onResponse(Call<ResponseGeneric> call, Response<ResponseGeneric> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    Log.i("****** SE SUBIO CORRECTAMENTE EL VIDEO", response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseGeneric> call, Throwable t) {
                Log.e("FRAM", "No se pudo subir el video");
            }
        });
        return videoData;
    }
}
