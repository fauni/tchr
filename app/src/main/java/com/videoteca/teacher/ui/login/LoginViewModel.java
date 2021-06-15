package com.videoteca.teacher.ui.login;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.Patterns;

import com.videoteca.teacher.data.LoginRepository;
import com.videoteca.teacher.data.LoginRepositoryR2;
import com.videoteca.teacher.data.Result;
import com.videoteca.teacher.data.model.LoggedInUser;
import com.videoteca.teacher.R;
import com.videoteca.teacher.data.model.retrofit.RespUserLogin;
import com.videoteca.teacher.data.model.retrofit.RespUserLogout;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;
    private LoginRepositoryR2 loginRepositoryR2;
    private MutableLiveData<RespUserLogin> mutableLiveData = new MutableLiveData<>();


    //private MutableLiveData<RespUserLogin> mutableLiveData;
    /*LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }*/
    LoginViewModel(LoginRepositoryR2 loginRepositoryR2) {
        this.loginRepositoryR2 = loginRepositoryR2;
        //mutableLiveData = loginRepositoryR2.postUsuario("juan@gmail.com", "123123123");
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password, Context context) {
        // can be launched in a separate asynchronous job
        //Result<LoggedInUser> result = loginRepository.login(username, password);

        //mutableLiveData = loginRepositoryR2.postUsuario(username, password);
        loginRepositoryR2.postUsuario(username, password).observe((LifecycleOwner) context, new Observer<RespUserLogin>() {
            @Override
            public void onChanged(RespUserLogin respUserLogin) {
                mutableLiveData.setValue(respUserLogin);
            }
        });


        //Result<MutableLiveData<RespUserLogin>> result = loginRepository.login(username, password);

        /*if (result instanceof Result.Success) {
            //LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            //loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }*/
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    public LiveData<RespUserLogin> getUsuarioData() {
        return mutableLiveData;
    }

}