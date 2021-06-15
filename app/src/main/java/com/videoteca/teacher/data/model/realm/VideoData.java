package com.videoteca.teacher.data.model.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class VideoData extends RealmObject {
    @PrimaryKey
    private int id;
    @Required
    private String titulo;
    @Required
    private String descripcion;
    @Required
    private String path_video;
    @Required
    private String name_video;
    @Required
    private String duracion;
    @Required
    private String peso;
    private int estado;
    private int id_usu;
    private int id_curs;
    @Required
    private String created_at;
    @Required
    private String updated_at;

    public VideoData() {
    }

    public VideoData(int id, String titulo, String descripcion, String path_video, String name_video, String duracion, String peso, int estado, int id_usu, int id_curs, String created_at, String updated_at) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.path_video = path_video;
        this.name_video = name_video;
        this.duracion = duracion;
        this.peso = peso;
        this.estado = estado;
        this.id_usu = id_usu;
        this.id_curs = id_curs;
        this.created_at = created_at;
        this.updated_at = updated_at;
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

    public String getPath_video() {
        return path_video;
    }

    public void setPath_video(String path_video) {
        this.path_video = path_video;
    }

    public String getName_video() {
        return name_video;
    }

    public void setName_video(String name_video) {
        this.name_video = name_video;
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

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getId_usu() {
        return id_usu;
    }

    public void setId_usu(int id_usu) {
        this.id_usu = id_usu;
    }

    public int getId_curs() {
        return id_curs;
    }

    public void setId_curs(int id_curs) {
        this.id_curs = id_curs;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
