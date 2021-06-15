package com.videoteca.teacher.ui.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.videoteca.teacher.data.HomeRepository;
import com.videoteca.teacher.data.LoginRepositoryR2;
import com.videoteca.teacher.ui.login.LoginViewModel;

import org.jetbrains.annotations.NotNull;

public class HomeViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            Log.e("Entra", "Factory Home");
            return (T) new HomeViewModel(HomeRepository.getInstance());
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
