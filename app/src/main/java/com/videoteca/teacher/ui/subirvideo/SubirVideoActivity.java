package com.videoteca.teacher.ui.subirvideo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.videoteca.teacher.R;

import java.util.Objects;

public class SubirVideoActivity extends AppCompatActivity {
    private ActionBar actionBar;

    Button seleccionarVideo;

    private static final int CODIGO_VIDEO_GALERIA = 100;
    private static final int CODIGO_VIDEO_CAMARA = 101;
    private static final int CODIGO_REQUERIDO_CAMARA = 102;

    private String [] cameraPermissions;
    private Uri videoUri; // Uri del video seleccionado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_video);

        // Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Subir Video a Curso");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        seleccionarVideo = findViewById(R.id.btnUploadVideo);

        seleccionarVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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

            } else if (requestCode == CODIGO_VIDEO_CAMARA){
                videoUri = data.getData();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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