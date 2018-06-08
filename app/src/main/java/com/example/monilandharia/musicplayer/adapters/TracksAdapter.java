package com.example.monilandharia.musicplayer.adapters;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.monilandharia.musicplayer.R;
import com.example.monilandharia.musicplayer.models.SongInfo;
import com.example.monilandharia.musicplayer.utilities.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import es.claucookie.miniequalizerlibrary.EqualizerView;

public class TracksAdapter extends RecyclerView.Adapter<TracksAdapter.ViewHolder>{

    private ArrayList<SongInfo> songs;
    private Context context;
    private RecyclerItemClickListener listener;

    public TracksAdapter(ArrayList<SongInfo> songs, Context context, RecyclerItemClickListener listener) {
        this.songs = songs;
        this.context = context;
        this.listener = listener;
    }

    public TracksAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_song,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder,int i)
    {
        SongInfo s = songs.get(i);
        if(s!=null) {
            viewHolder.sname.setText(s.getSong_name());
            viewHolder.sartistdur.setText(s.getSong_artist()+" \u2022 "+Utility.getTime(s.getSong_duration()));
            Uri albart = getAlbumArtUri(s.getAlbum_id());
            String datatoplay = s.getData();
            Picasso.with(context).load(albart.toString()).placeholder(R.drawable.placeholder).into(viewHolder.simg);
        }

        viewHolder.bind(s,listener);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView sname;
        ImageView simg;
        TextView sartistdur;
        EqualizerView equalizerView;
        public ViewHolder(View view)
        {
            super(view);
            sname = view.findViewById(R.id.songTitle);
            simg = view.findViewById(R.id.songImage);
            sartistdur = view.findViewById(R.id.songArtistDuration);
            equalizerView = view.findViewById(R.id.songEqualizer);
        }

        public void bind(final SongInfo song, final RecyclerItemClickListener listener)
        {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickListener(song,getLayoutPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount(){
        return songs.size();
    }

    public Uri getAlbumArtUri(long param)
    {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"),param);
    }

    public interface RecyclerItemClickListener{
        void onClickListener(SongInfo song,int position);
    }
}
