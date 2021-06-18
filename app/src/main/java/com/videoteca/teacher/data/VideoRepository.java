package com.videoteca.teacher.data;

import android.net.Uri;
import android.os.FileUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.videoteca.teacher.data.model.retrofit.ResponseGeneric;
import com.videoteca.teacher.data.model.retrofit.Video;
import com.videoteca.teacher.data.rest.ApiClient;
import com.videoteca.teacher.data.rest.ApiInterface;
import com.videoteca.teacher.utils.Constants;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
        apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    }

    public MutableLiveData<ResponseGeneric> saveUploadVideo(Video video, Uri fileUri, String authorization) {


/*
        File file = new File(fileUri.getPath());
        RequestBody requestFile = RequestBody.create(MediaType.parse("video/*"), file);
        MultipartBody.Part videoSeleccionado = MultipartBody.Part.createFormData("video", file.getName(), requestFile);
*/
        Map<String, RequestBody> map = new HashMap<>();

        File file = new File(video.getPathVideo());
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part videoSeleccionado = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        map.put("file\"; filename=\"" + file.getName() + "\"", requestBody);

        RequestBody titulo = RequestBody.create(MediaType.parse("multipart/form-data"), video.getTitulo().toString());
        RequestBody descripcion = RequestBody.create(MediaType.parse("multipart/form-data"), video.getDescripcion().toString());
        RequestBody estado = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(video.getEstado()));
        RequestBody idCurs = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(video.getIdCurs()));

        MutableLiveData<ResponseGeneric> videoData = new MutableLiveData<>();
        apiInterface.postUploadVideo(titulo, descripcion, estado, idCurs, videoSeleccionado, Constants.KEY.Flag.concat(" ").concat(authorization)).enqueue(new Callback<ResponseGeneric>() {
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
