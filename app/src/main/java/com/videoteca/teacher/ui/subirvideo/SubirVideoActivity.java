package com.videoteca.teacher.ui.subirvideo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.videoteca.teacher.R;
import com.videoteca.teacher.data.model.realm.CursoData;
import com.videoteca.teacher.data.model.realm.SessionData;
import com.videoteca.teacher.data.model.retrofit.Video;
import com.videoteca.teacher.managers.SessionManager;

import java.io.File;
import java.util.Objects;

import io.realm.Realm;

public class SubirVideoActivity extends AppCompatActivity {
    private Realm realm;
    private Context context;
    private String authorization;
    private ActionBar actionBar;
    private SessionManager sessionManager;
    private SubirVideoViewModel subirVideoViewModel;

    Video video;
    private String videoPath;
    private Uri videoUri = null; // Uri del video seleccionado

    TextView nombreCurso;
    Button seleccionarVideo;
    TextView textoUrlVideo;
    VideoView videoView;

    EditText tituloVideo;
    EditText descripcionVideo;

    Button registrarVideo;
    Button cancelarRegistro;

    CursoData curso = new CursoData();

    private File videoFile;
    File choosedFile;

    private static final int CODIGO_VIDEO_GALERIA = 100;
    private static final int CODIGO_VIDEO_CAMARA = 101;
    private static final int CODIGO_REQUERIDO_CAMARA = 102;

    private String [] cameraPermissions;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_video);
        context = this;
        // Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Subir Video");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        verifyStoragePermissions(this);
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        subirVideoViewModel = new ViewModelProvider(this, new SubirVideoViewModelFactory()).get(SubirVideoViewModel.class);


        nombreCurso = (TextView) findViewById(R.id.txtNombreCurso);
        seleccionarVideo = findViewById(R.id.btnUploadVideo);
        textoUrlVideo = findViewById(R.id.txtVideoPath);
        videoView = findViewById(R.id.videoSeleccionado);

        tituloVideo = findViewById(R.id.tituloVideo);
        descripcionVideo = findViewById(R.id.descripcionVideo);

        registrarVideo = findViewById(R.id.btnRegistrarVideo);
        cancelarRegistro = findViewById(R.id.btnCancelarRegistro);



        seleccionarVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPickDialog();
            }
        });

        registrarVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                video = new Video();
                video.setId(0);
                video.setTitulo(tituloVideo.getText().toString());
                video.setDescripcion(descripcionVideo.getText().toString());
                video.setEstado(1);
                video.setIdCurs(curso.getId());
                video.setPathVideo(videoPath);
                video.setVideo(videoFile);

                subirVideoViewModel.subirVideoPorCurso(SubirVideoActivity.this,authorization, video, videoUri);
            }
        });
        // Instancia de Session Manager
        sessionManager = new SessionManager(getApplicationContext());
        //Get realm instance
        realm = Realm.getDefaultInstance();
        //Get data session Realm
        SessionData sessionData = realm.where(SessionData.class).findFirst();
        authorization = sessionData.getToken();
        this.obtenerDataCurso();
    }

    private void obtenerDataCurso(){
        Bundle data = getIntent().getExtras();
        if (data != null) {
            Gson gson = new Gson();
            curso = gson.fromJson(data.getString("CURSO_JSON"), CursoData.class);
            actionBar.setTitle("Agregar nuevo video");

            this.nombreCurso.setText(curso.getNombre());
        }
    }

    private void videoPickDialog(){
        String [] options = {"Camara", "Galeria"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Subir video desde").setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0)
                {
                    // Si selecciono la camara
                    if (!checkCameraPermission()){
                        requestCameraPermission();
                    } else {
                        videoSeleccionadoCamara();
                    }
                } else if (which == 1){
                    // Si selecciono la galeria
                    videoSeleccionadoGaleria();
                }
            }
        }).show();
    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this, cameraPermissions, CODIGO_REQUERIDO_CAMARA);
    }

    private boolean checkCameraPermission(){
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK) == PackageManager.PERMISSION_GRANTED;
        return result1 && result2;
    }

    private void videoSeleccionadoGaleria(){
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccionar Video"), CODIGO_VIDEO_GALERIA);
    }

    private void videoSeleccionadoCamara(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, CODIGO_VIDEO_CAMARA);
    }

    private void setVideoToVideoView(){
        textoUrlVideo.setText(videoUri.getPath());
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        videoFile = new File(videoUri.getPath());
        // Colocamos media controller en el video view
        videoView.setMediaController(mediaController);
        // Colocamos la uri del video
        videoView.setVideoURI(videoUri);
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoView.pause();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CODIGO_REQUERIDO_CAMARA:
                if (grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted){
                        videoSeleccionadoCamara();
                    } else {
                        Toast.makeText(this, "Necesitamos permiso de acceso a la Camara y a la tarjeta SD", Toast.LENGTH_SHORT).show();
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == RESULT_OK){
            if (requestCode == CODIGO_VIDEO_GALERIA){
                videoUri = data.getData();
                videoPath = getPath(videoUri);
                setVideoToVideoView();

            } else if (requestCode == CODIGO_VIDEO_CAMARA){
                videoUri = data.getData();
                videoPath = getPath(videoUri);
                setVideoToVideoView();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    /*
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    */
}