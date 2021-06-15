package com.videoteca.teacher.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Video {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("titulo")
    @Expose
    private String titulo;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;
    @SerializedName("path_video")
    @Expose
    private String pathVideo;
    @SerializedName("duracion")
    @Expose
    private String duracion;
    @SerializedName("peso")
    @Expose
    private String peso;
    @SerializedName("estado")
    @Expose
    private boolean estado;
    @SerializedName("id_usuario")
    @Expose
    private int idUsuario;
    @SerializedName("id_curso")
    @Expose
    private int idCurso;

    public Video(int id, String titulo, String descripcion, String pathVideo, String duracion, String peso, boolean estado, int idUsuario, int idCurso) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.pathVideo = pathVideo;
        this.duracion = duracion;
        this.peso = peso;
        this.estado = estado;
        this.idUsuario = idUsuario;
        this.idCurso = idCurso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPathVideo() {
        return pathVideo;
    }

    public void setPathVideo(String pathVideo) {
        this.pathVideo = pathVideo;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

}
