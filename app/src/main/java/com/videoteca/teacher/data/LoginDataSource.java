package com.videoteca.teacher.data;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.videoteca.teacher.data.model.LoggedInUser;
import com.videoteca.teacher.data.model.retrofit.RespUserLogin;
import com.videoteca.teacher.data.model.retrofit.UserLogin;
import com.videoteca.teacher.data.rest.ApiClient;
import com.videoteca.teacher.data.rest.ApiInterface;
import com.videoteca.teacher.utils.Door;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    RespUserLogin respUserLogin = new RespUserLogin();
    private Door door;

    public Result<MutableLiveData<RespUserLogin>> login(String username, String password) {


        Log.e("PASASASASASASA", "LLEGA");

        try {
            // TODO: handle loggedInUser authentication
            MutableLiveData<RespUserLogin> UserData = postUsuario(username, password);
            return new Result.Success<>(UserData);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
        /*try {
            // TODO: handle loggedInUser authentication
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }*/
    }

    public void logout() {
        // TODO: revoke authentication
    }

    public MutableLiveData<RespUserLogin> postUsuario(String username, String password){
        MutableLiveData<RespUserLogin> loginData = new MutableLiveData<>();

        UserLogin userLogin = new UserLogin(username, password, "T");
        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<RespUserLogin> call = apiService.postLogin(userLogin);
        call.enqueue(new Callback<RespUserLogin>() {
            @Override
            public void onResponse(Call<RespUserLogin> call, Response<RespUserLogin> response) {
                int statusCode = response.code();
                Log.i("STATUS CODE", String.valueOf(statusCode));
                if (statusCode == 200) {
                    respUserLogin = response.body();
                    Log.e("token", respUserLogin.getToken());
                    Log.e("message", respUserLogin.getMessage());
                    loginData.setValue(response.body());

                }

            }

            @Override
            public void onFailure(Call<RespUserLogin> call, Throwable t) {
                loginData.setValue(null);

            }
        });

        /*newsApi.getNewsList(source, key).enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call,
                                   Response<NewsResponse> response) {
                if (response.isSuccessful()){
                    newsData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                newsData.setValue(null);
            }
        });
        return newsData;*/

        return loginData;
    }
}