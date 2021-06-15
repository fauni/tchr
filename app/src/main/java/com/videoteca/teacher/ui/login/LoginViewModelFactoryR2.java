package com.videoteca.teacher.ui.login;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.videoteca.teacher.data.LoginDataSource;
import com.videoteca.teacher.data.LoginRepository;
import com.videoteca.teacher.data.LoginRepositoryR2;

import org.jetbrains.annotations.NotNull;

public class LoginViewModelFactoryR2 implements ViewModelProvider.Factory{
    @NonNull
    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            Log.e("Entra", "Factory R2");
            return (T) new LoginViewModel(LoginRepositoryR2.getInstance());
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
