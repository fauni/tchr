package com.videoteca.teacher.ui.home;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.videoteca.teacher.data.HomeRepository;
import com.videoteca.teacher.data.model.retrofit.RespCursos;
import com.videoteca.teacher.data.model.retrofit.RespUserLogout;

public class HomeViewModel extends ViewModel {
    private HomeRepository homeRepository;
    private MutableLiveData<RespUserLogout> respUserLogoutMutableLiveData = new MutableLiveData<>();

    public HomeViewModel(HomeRepository homeRepository) {
        this.homeRepository = homeRepository;
    }

    public LiveData<RespUserLogout> getLogoutData() {
        return respUserLogoutMutableLiveData;
    }

    public void logout(Context context, String authorization) {
        homeRepository.logoutUsuario(authorization).observe((LifecycleOwner) context, new Observer<RespUserLogout>() {
            @Override
            public void onChanged(RespUserLogout respUserLogout) {
                respUserLogoutMutableLiveData.setValue(respUserLogout);
            }
        });
    }

}
