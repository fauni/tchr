package com.videoteca.teacher.ui.subirvideo;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.videoteca.teacher.data.VideoRepository;

import org.jetbrains.annotations.NotNull;

public class SubirVideoViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SubirVideoViewModel.class)){
            return (T) new SubirVideoViewModel(VideoRepository.getInstance());
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
