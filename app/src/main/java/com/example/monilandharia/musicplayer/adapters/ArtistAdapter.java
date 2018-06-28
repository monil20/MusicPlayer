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
import com.l4digital.fastscroll.FastScroller;
import com.ohoussein.playpause.PlayPauseView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> implements FastScroller.SectionIndexer{
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
        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_all_artists, null);
        ViewHolder rcv = new ViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ArtistInfo artist = artistInfos.get(i);
        if (artist != null) {
            viewHolder.tvArtistName.setText(artist.getArtistName());
            viewHolder.tvArtistStats.setText(artist.getAlbumsCount() + " albums, "+artist.getTracksCount()+" songs");
            Uri albumArtUri = Utility.getAlbumArtUri(artist.getSongId());
//                String datatoplay = s.getData();
            Picasso.with(context).load(albumArtUri.toString()).placeholder(R.drawable.placeholder1).into(viewHolder.ivArtistArt);
//                Picasso.with(context).load(album.getAlbumArt()).placeholder1(R.mipmap.ic_launcher).into(viewHolder.ivAlbumArt);
        }

        viewHolder.bind(artist, listener);

    }

    @Override
    public int getItemCount() {
        return artistInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivArtistArt, seemore;
        TextView tvArtistName, tvArtistStats;
        PlayPauseView playPauseView;
        //        TextView sdur;

        public ViewHolder(View view) {
            super(view);
            tvArtistName = view.findViewById(R.id.playlistName);
            tvArtistName.setSelected(true);
            tvArtistStats = view.findViewById(R.id.artistStats);
            ivArtistArt = view.findViewById(R.id.playlistArt);
            playPauseView = view.findViewById(R.id.artistPlayPause);
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

    @Override
    public String getSectionText(int position) {
        return String.valueOf(artistInfos.get(position).getArtistName().charAt(0));
    }


    public interface RecyclerItemClickListener {

        void onClickListener(ArtistInfo artistInfo, int position);
    }
}
