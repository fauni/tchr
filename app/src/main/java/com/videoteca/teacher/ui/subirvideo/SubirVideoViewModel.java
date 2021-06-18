package com.videoteca.teacher.ui.subirvideo;

import android.app.Activity;
import android.net.Uri;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.videoteca.teacher.data.VideoRepository;
import com.videoteca.teacher.data.model.retrofit.ResponseGeneric;
import com.videoteca.teacher.data.model.retrofit.Video;


public class SubirVideoViewModel extends ViewModel {

    private MutableLiveData<ResponseGeneric> videoResult = new MutableLiveData<>();
    private VideoRepository videoRepository;


    SubirVideoViewModel(VideoRepository videoRepository){
        this.videoRepository = videoRepository;
    }

    LiveData<ResponseGeneric> getVideoResult() {
        return videoResult;
    }

    public void subirVideoPorCurso(Activity activity, String authorization, Video video, Uri uri){
        videoRepository.saveUploadVideo(video, uri, authorization).observe((LifecycleOwner) activity, new Observer<ResponseGeneric>() {
            @Override
            public void onChanged(ResponseGeneric responseGeneric) {
                videoResult.setValue(responseGeneric);
            }
        });
    }
}
