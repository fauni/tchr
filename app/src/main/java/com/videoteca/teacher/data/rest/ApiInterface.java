package com.videoteca.teacher.data.rest;

import com.videoteca.teacher.data.model.retrofit.RespCursos;
import com.videoteca.teacher.data.model.retrofit.RespUserLogout;
import com.videoteca.teacher.data.model.retrofit.RespUserLogin;
import com.videoteca.teacher.data.model.retrofit.ResponseGeneric;
import com.videoteca.teacher.data.model.retrofit.UserLogin;
import com.videoteca.teacher.data.model.retrofit.Video;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {
    @POST("/api/login")
    Call<RespUserLogin> postLogin(@Body UserLogin userLogin);
    @POST("/api/logout")
    Call<RespUserLogout> postLogout(@Header("Authorization") String authorization);
    @GET("/api/v1/cursos")
    Call<RespCursos> getCursos(@Header("Authorization") String authorization);
    @GET("/api/v1/videos")
    Call<List<Video>> getVideos(@Header("Authorization") String authorization);

    @Multipart
    @POST("/api/v1/videos")
    Call<ResponseGeneric> postUploadVideo(
            @Part("titulo") String titulo,
            @Part("descripcion") String descripcion,
            @Part("estado") int estado,
            @Part("id_curs") int id_curs,
            @Part("video") File video,
            @Header("Authorization") String authorization);
}
