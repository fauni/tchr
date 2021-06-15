package com.videoteca.teacher.ui.video;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.videoteca.teacher.R;
import com.videoteca.teacher.data.model.realm.VideoData;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class VideoActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    MediaController mediaController;
    VideoView videoView;
    private int mCurrentPosition = 0;
    private static final String PLAYBACK_TIME = "play_time";
    Uri uri;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        Bundle data = intent.getExtras();

        if (data != null) {
            Gson gson = new Gson();
            VideoData videoData = gson.fromJson(data.getString("VIDEO_JSON"), VideoData.class);
            getSupportActionBar().setTitle(videoData.getTitulo());
        }
        context = this;

        final TextView textView = findViewById(R.id.text_video_play);

        progressBar = findViewById(R.id.progressBar2);
        videoView = findViewById(R.id.videoView2);

        //uri = Uri.parse("https://www.youtube.com/watch?v=kHxQTqIJeeA");

        //initializePlayer(uri);

        //progressBar.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        }

        mediaController = new MediaController(context);
        mediaController.setMediaPlayer(videoView);
        videoView.setMediaController(mediaController);

        /*final Handler handler = new Handler();
        Runnable runnable = () -> {
            if (videoView.isPlaying()) {
                progressBar.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.VISIBLE);
            }
            //handler.postDelayed((Runnable) this, 1000);
        };
        handler.postDelayed(runnable, 0);*/

    }

    @Override
    protected void onStart() {
        super.onStart();
        initializePlayer();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //this.finish();
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            videoView.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    protected void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PLAYBACK_TIME, videoView.getCurrentPosition());
    }

    private void initializePlayer() {
        //uri = Uri.parse("https://www.youtube.com/watch?v=HexFqifusOk&list=RDHexFqifusOk&start_radio=1");
        uri = Uri.parse("https://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4");
        Log.e("Inicia","Aca");


        videoView.setVideoURI(uri);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                if (mCurrentPosition > 0) {
                    videoView.seekTo(mCurrentPosition);
                } else {
                    videoView.seekTo(1);
                }

                videoView.start();
            }
        });

        videoView.setOnCompletionListener((mediaPlayer) -> {
            videoView.seekTo(0);

        });

    }

    private void releasePlayer() {
        videoView.stopPlayback();
    }
}