package com.example.monilandharia.musicplayer.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.monilandharia.musicplayer.R;
import com.example.monilandharia.musicplayer.models.ArtistInfo;
import com.example.monilandharia.musicplayer.utilities.Utility;
import com.ohoussein.playpause.PlayPauseView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder>{
    private ArrayList<ArtistInfo> artistInfos;
    private Context context;
    private RecyclerItemClickListener listener;
    private FragmentManager fragmentManager;

    public ArtistAdapter(Context context, ArrayList<ArtistInfo> artistInfos, RecyclerItemClickListener listener, FragmentManager fragmentManager) {
        this.context = context;
        this.artistInfos = artistInfos;
        this.listener = listener;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public ArtistAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_artist, null);
        ViewHolder rcv = new ViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ArtistInfo artist = artistInfos.get(i);
        if (artist != null) {
            viewHolder.tvArtist.setText(artist.getArtistName());
            viewHolder.tvArtistStats.setText(artist.getAlbumsCount() + " albums, "+artist.getTracksCount()+" songs");
            Uri albumArtUris[] = new Uri[4];
            for(int n=0;n<4;n++)
            {
                albumArtUris[n] = Utility.getAlbumArtUri(artist.getSongIds()[n]);
            }
            Picasso.with(context).load(albumArtUris[0]).placeholder(R.drawable.placeholder).into(viewHolder.ivAlbumArt1);
            Picasso.with(context).load(albumArtUris[1]).placeholder(R.drawable.placeholder).into(viewHolder.ivAlbumArt2);
            Picasso.with(context).load(albumArtUris[2]).placeholder(R.drawable.placeholder).into(viewHolder.ivAlbumArt3);
            Picasso.with(context).load(albumArtUris[3]).placeholder(R.drawable.placeholder).into(viewHolder.ivAlbumArt4);
//                Picasso.with(context).load(album.getAlbumArt()).placeholder1(R.mipmap.ic_launcher).into(viewHolder.ivAlbumArt);
        }

        viewHolder.bind(artist, listener);

    }

    @Override
    public int getItemCount() {
        return artistInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAlbumArt1, ivAlbumArt2, ivAlbumArt3, ivAlbumArt4, seemore;
        TextView tvArtist, tvArtistStats;
        PlayPauseView playPauseView;

        //        TextView sdur;
        public ViewHolder(View view) {
            super(view);
            tvArtist = view.findViewById(R.id.artitstName);
            tvArtist.setSelected(true);
            tvArtistStats = view.findViewById(R.id.songCount);
            ivAlbumArt1 = view.findViewById(R.id.albumArt1);
            ivAlbumArt2 = view.findViewById(R.id.albumArt2);
            ivAlbumArt3 = view.findViewById(R.id.albumArt3);
            ivAlbumArt4 = view.findViewById(R.id.albumArt4);
            playPauseView = view.findViewById(R.id.songPlayPause);
            playPauseView.bringToFront();
        }

        public void bind(final ArtistInfo artist, final RecyclerItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickListener(artist, getLayoutPosition());
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_all_albums;
    }

    public interface RecyclerItemClickListener {
        void onClickListener(ArtistInfo artistInfo, int position);
    }
}
