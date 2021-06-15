package com.videoteca.teacher.data.model.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class SessionData extends RealmObject {
    @PrimaryKey
    @Required
    private String token;
    @Required
    private String message;
    @Required
    private String ci;
    @Required
    private String nombre;
    @Required
    private String primer_ap;
    @Required
    private String segundo_ap;
    @Required
    private String email;
    private int tipo;

    public SessionData(){}

    public SessionData(String token, String message, String ci, String nombre, String primer_ap, String segundo_ap, String email, int tipo) {
        this.token = token;
        this.message = message;
        this.ci = ci;
        this.nombre = nombre;
        this.primer_ap = primer_ap;
        this.segundo_ap = segundo_ap;
        this.email = email;
        this.tipo = tipo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrimer_ap() {
        return primer_ap;
    }

    public void setPrimer_ap(String primer_ap) {
        this.primer_ap = primer_ap;
    }

    public String getSegundo_ap() {
        return segundo_ap;
    }

    public void setSegundo_ap(String segundo_ap) {
        this.segundo_ap = segundo_ap;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
