package com.videoteca.teacher.data.model.retrofit;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RespCursos {

    @SerializedName("data")
    @Expose
    private List<Curso> data = null;

    public List<Curso> getData() {
        return data;
    }

    public void setData(List<Curso> data) {
        this.data = data;
    }

}
