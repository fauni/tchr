package com.videoteca.teacher.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.videoteca.teacher.R;
import com.videoteca.teacher.data.model.realm.CursoData;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import io.realm.RealmList;

public class CursoAdapter extends RecyclerView.Adapter<CursoAdapter.CustomViewHolder> {

    private Context context;
    //private ArrayList<Curso> cursos;
    private RealmList<CursoData> cursos;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;


    public CursoAdapter(Context context, RealmList<CursoData> cursos) {
        this.context = context;
        this.cursos = cursos;
        this.inflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(R.layout.curso_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {


        //Curso curso = cursos.get(position);
        CursoData cursoData = cursos.get(position);
        //Picasso.get().load(preach.getImageSrc()).into(holder.imageVideo);

        //holder.textCourseName.setText(curso.getNombre());
        holder.textCourseName.setText(cursoData.getNombre());
        //holder.imageVideo.setImageDrawable();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(cursos.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cursos.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        public TextView textCourseName;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            textCourseName = itemView.findViewById(R.id.text_course_name);
        }
    }

    public interface OnItemClickListener {
        //void onItemClick(Curso curso);
        void onItemClick(CursoData cursoData);
    }
}

