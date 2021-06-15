package com.videoteca.teacher.data;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.videoteca.teacher.data.model.LoggedInUser;
import com.videoteca.teacher.data.model.retrofit.RespUserLogin;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<MutableLiveData<RespUserLogin>> login(String username, String password) {
        // handle login
        Result<MutableLiveData<RespUserLogin>> result = dataSource.login(username, password);
        if (result instanceof Result.Success) {
            RespUserLogin respUserLogin = ((Result.Success<MutableLiveData<RespUserLogin>>) result).getData().getValue();
            Log.e("MESSAGE REPO", respUserLogin.getMessage());

            //setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;
    }

    /*public Result<LoggedInUser> login(String username, String password) {
        // handle login
        Result<LoggedInUser> result = dataSource.login(username, password);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;
    }*/
}