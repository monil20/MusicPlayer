package com.example.monilandharia.musicplayer.adapters;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.monilandharia.musicplayer.MainActivity;
import com.example.monilandharia.musicplayer.R;
import com.example.monilandharia.musicplayer.TracksFragment;
import com.example.monilandharia.musicplayer.models.ArtistInfo;
import com.example.monilandharia.musicplayer.models.SongInfo;
import com.ohoussein.playpause.PlayPauseView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TracksPreviewAdapter extends RecyclerView.Adapter<TracksPreviewAdapter.ViewHolder> {
    private ArrayList<SongInfo> songs;
    private Context context;
    private RecyclerItemClickListener listener;
    private FragmentTransaction fragmentTransaction;

    public TracksPreviewAdapter(Context context, ArrayList<SongInfo> songs, RecyclerItemClickListener listener) {
        this.context = context;
        this.songs = songs;
        this.listener = listener;
    }

    @Override
    public TracksPreviewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        if (i == R.layout.item_recently_added) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recently_added, viewGroup, false);
            return new ViewHolder(view);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_seemore, viewGroup, false);
            return new ViewHolder(view, 1);
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        if (i == songs.size()) {
            viewHolder.seemore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmentTransaction = MainActivity.fragmentManager.beginTransaction();
                    fragmentTransaction.addToBackStack("HOME");
                    fragmentTransaction.show(MainActivity.fragmentTracks);
                    fragmentTransaction.hide(MainActivity.fragmentHome);
                    fragmentTransaction.commit();
                }
            });
        } else {
            SongInfo s = songs.get(i);
            if (s != null) {
                viewHolder.sname.setText(s.getSong_name());
                viewHolder.sartist.setText(s.getSong_artist());
//            viewHolder.sdur.setText(Utility.getTime(s.getSong_duration()));
                Uri albart = getAlbumArtUri(s.getAlbum_id());
                String datatoplay = s.getData();
                Picasso.with(context).load(albart.toString()).placeholder(R.drawable.placeholder1).into(viewHolder.simg);
            }

            viewHolder.bind(s, listener);
        }


    }

    @Override
    public int getItemCount() {
        return songs.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sname;
        ImageView simg, seemore;
        TextView sartist;
        PlayPauseView playPauseView;

        //        TextView sdur;
        public ViewHolder(View view) {
            super(view);
            sname = view.findViewById(R.id.playlistName);
            sname.setSelected(true);
            simg = view.findViewById(R.id.playlistArt);
            sartist = view.findViewById(R.id.songArtist);
            playPauseView = view.findViewById(R.id.songPlayPause);
            playPauseView.bringToFront();

//            sdur = view.findViewById(R.id.duration);
        }

        public ViewHolder(View view, int n) {
            super(view);
            seemore = view.findViewById(R.id.imageView2);
        }

        public void bind(final SongInfo song, final RecyclerItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickListener(song, getLayoutPosition());
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == songs.size()) ? R.layout.layout_seemore : R.layout.item_recently_added;
    }

    public Uri getAlbumArtUri(long param) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), param);
    }

    public interface RecyclerItemClickListener {
        void onClickListener(SongInfo song, int position);
    }

    public void filterTracks(ArrayList<SongInfo> filteredSongs) {
        songs = filteredSongs;
        this.notifyDataSetChanged();
    }
}
