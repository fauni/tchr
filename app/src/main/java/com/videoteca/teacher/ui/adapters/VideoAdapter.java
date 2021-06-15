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

import com.videoteca.teacher.data.model.realm.VideoData;


import io.realm.RealmList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.CustomViewHolder> {

    private Context context;
    //private ArrayList<Video> videos;
    private RealmList<VideoData> videos;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;


    public VideoAdapter(Context context, RealmList<VideoData> videos) {
        this.context = context;
        this.videos = videos;
        this.inflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(R.layout.video_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {


        VideoData videoData = videos.get(position);
        //Video video = videos.get(position);
        //Picasso.get().load(preach.getImageSrc()).into(holder.imageVideo);
        holder.imageVideoPre.setImageResource(R.drawable.download_video);
        //holder.textVideoPre.setText(video.getTitulo());
        holder.textVideoPre.setText(videoData.getTitulo());
        //holder.imageVideo.setImageDrawable();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(videos.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageVideoPre;
        public TextView textVideoPre;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            imageVideoPre = itemView.findViewById(R.id.image_video_pre);
            textVideoPre = itemView.findViewById(R.id.text_video_pre);
        }
    }

    public interface OnItemClickListener {
        //void onItemClick(Video video);
        void onItemClick(VideoData videoData);
    }
}
