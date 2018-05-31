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

import Utility.Utility;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private ArrayList<SongInfo> songs;
    private Context context;
    private RecyclerItemClickListener listener;

    public DataAdapter(Context context, ArrayList<SongInfo> songs, RecyclerItemClickListener listener)
    {
        this.context = context;
        this.songs = songs;
        this.listener = listener;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup,int i)
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
            viewHolder.sartist.setText(s.getSong_artist());
            viewHolder.sdur.setText(Utility.getTime(s.getSong_duration()));
            Uri albart = getAlbumArtUri(s.getAlbum_id());
            String datatoplay = s.getData();
            Picasso.with(context).load(albart.toString()).placeholder(R.drawable.placeholder).into(viewHolder.simg);
        }

        viewHolder.bind(s,listener);
    }

    @Override
    public int getItemCount(){
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView sname;
        ImageView simg;
        TextView sartist;
        TextView sdur;
        public ViewHolder(View view)
        {
            super(view);
            sname = view.findViewById(R.id.songname);
            simg = view.findViewById(R.id.thumb);
            sartist = view.findViewById(R.id.Author);
            sdur = view.findViewById(R.id.duration);
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

    public Uri getAlbumArtUri(long param)
    {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"),param);
    }

    public interface RecyclerItemClickListener{
        void onClickListener(SongInfo song, int position);
    }
}
