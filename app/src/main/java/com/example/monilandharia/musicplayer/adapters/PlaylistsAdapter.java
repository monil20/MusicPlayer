package com.example.monilandharia.musicplayer.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.monilandharia.musicplayer.MainActivity;
import com.example.monilandharia.musicplayer.R;
import com.example.monilandharia.musicplayer.database.DatabaseHelper;
import com.example.monilandharia.musicplayer.database.model.PlaylistSongs;
import com.example.monilandharia.musicplayer.database.model.Playlists;
import com.example.monilandharia.musicplayer.models.AlbumInfo;
import com.example.monilandharia.musicplayer.models.SongInfo;
import com.example.monilandharia.musicplayer.utilities.Utility;
import com.ohoussein.playpause.PlayPauseView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlaylistsAdapter extends RecyclerView.Adapter<PlaylistsAdapter.ViewHolder> {
    private ArrayList<Playlists> playlists;
    private Context context;
    private RecyclerItemClickListener listener;
    private FragmentManager fragmentManager;

    public PlaylistsAdapter(Context context, ArrayList<Playlists> playlists, RecyclerItemClickListener listener, FragmentManager fragmentManager) {
        this.context = context;
        this.playlists = playlists;
        this.listener = listener;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_playlist, null);
        ViewHolder rcv = new ViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        Playlists playlist = playlists.get(i);
        if (playlist != null) {
            viewHolder.tvPlaylistName.setText(playlist.getTitle());
            ArrayList<SongInfo> playlistSongs = MainActivity.db.getSongsForPlaylist(playlist.getPlaylistId());
            if (playlistSongs.size() != 0) {
                viewHolder.tvPlaylistSongCount.setText(playlistSongs.size() + " songs");
                Uri albumArtUri = Utility.getAlbumArtUri(playlistSongs.get(0).getAlbum_id());
//                String datatoplay = s.getData();
                Picasso.with(context).load(albumArtUri.toString()).placeholder(R.drawable.placeholder1).into(viewHolder.ivPlaylistArt);
//                Toast.makeText(context, playlists.get(i).getPlaylistId()+"", Toast.LENGTH_SHORT).show();

                viewHolder.ivPlaylistOptions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                viewHolder.bind(playlist, listener);
            } else {
                Toast.makeText(context, "No songs", Toast.LENGTH_SHORT).show();
                MainActivity.db.removePlaylist(playlist.getTitle());
            }
        }


    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPlaylistArt, ivPlaylistOptions;
        TextView tvPlaylistName, tvPlaylistSongCount;
        PlayPauseView playPauseView;
        CardView cardView;

        //        TextView sdur;
        public ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.card_view);
            tvPlaylistName = view.findViewById(R.id.playlistName);
            tvPlaylistName.setSelected(true);
            tvPlaylistSongCount = view.findViewById(R.id.playlistSongCount);
            ivPlaylistArt = view.findViewById(R.id.playlistArt);
            playPauseView = view.findViewById(R.id.playlistPlayPause);
            playPauseView.bringToFront();
            ivPlaylistOptions = view.findViewById(R.id.playlistOptions);
        }

        public void bind(final Playlists playlists, final RecyclerItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickListener(playlists, getLayoutPosition());
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_playlist;
    }

    public interface RecyclerItemClickListener {
        void onClickListener(Playlists playlists, int position);
    }

}
