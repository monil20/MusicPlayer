package com.example.monilandharia.musicplayer.adapters;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.monilandharia.musicplayer.ArtistsFragment;
import com.example.monilandharia.musicplayer.R;
import com.example.monilandharia.musicplayer.models.ArtistInfo;
import com.ohoussein.playpause.PlayPauseView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ArtistsPreviewAdapter extends RecyclerView.Adapter<ArtistsPreviewAdapter.ViewHolder> {
    private ArrayList<ArtistInfo> artists;
    private Context context;
    private RecyclerItemClickListener listener;
    private FragmentManager fragmentManager;
    private boolean isHome;

    public ArtistsPreviewAdapter(Context context, ArrayList<ArtistInfo> artists, RecyclerItemClickListener listener,FragmentManager fragmentManager, boolean isHome) {
        this.context = context;
        this.artists = artists;
        this.listener = listener;
        this.fragmentManager = fragmentManager;
        this.isHome = isHome;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        if (isHome) {
            if (i == R.layout.item_artist) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_artist, viewGroup, false);
                return new ViewHolder(view);
            } else {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_seemore, viewGroup, false);
                return new ViewHolder(view, 1);
            }
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_artist, viewGroup, false);
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        if (isHome) {
            if (i == artists.size()) {
                viewHolder.seemore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_home, new ArtistsFragment()).addToBackStack("HOME").commit();
                    }
                });
            } else {
                ArtistInfo artist = artists.get(i);
                if (artist != null) {
                    viewHolder.tvArtistName.setText(artist.getArtistName());
                    viewHolder.tvArtistStats.setText(artist.getAlbumsCount() + " albums, "+artist.getTracksCount()+" songs");
                    Uri albumArtUri = getAlbumArtUri(artist.getSongId());
    //                String datatoplay = s.getData();
                    Picasso.with(context).load(albumArtUri.toString()).placeholder(R.drawable.placeholder1).into(viewHolder.ivArtistArt);

    //                Picasso.with(context).load(album.getAlbumArt()).placeholder1(R.mipmap.ic_launcher).into(viewHolder.ivAlbumArt);
                }

                viewHolder.bind(artist, listener);
            }
        } else {
            ArtistInfo artist = artists.get(i);
            if (artist != null) {
                viewHolder.tvArtistName.setText(artist.getArtistName());
                viewHolder.tvArtistStats.setText(artist.getAlbumsCount() + " albums, "+artist.getTracksCount()+" songs");
                Uri albumArtUri = getAlbumArtUri(artist.getSongId());
                //                String datatoplay = s.getData();
                Picasso.with(context).load(albumArtUri.toString()).placeholder(R.drawable.placeholder1).into(viewHolder.ivArtistArt);

                //                Picasso.with(context).load(album.getAlbumArt()).placeholder1(R.mipmap.ic_launcher).into(viewHolder.ivAlbumArt);
            }

            viewHolder.bind(artist, listener);
        }


    }

    @Override
    public int getItemCount() {
        if (isHome) {
            return artists.size() + 1;
        } else {
            return artists.size();
        }
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

        public ViewHolder(View view, int n) {
            super(view);
            seemore = view.findViewById(R.id.imageView2);
        }

        public void bind(final ArtistInfo song, final RecyclerItemClickListener listener) {
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
        if (isHome) {
            return (position == artists.size()) ? R.layout.layout_seemore : R.layout.item_artist;
        } else {
            return R.layout.item_artist;
        }
    }

    public Uri getAlbumArtUri(long param) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), param);
    }

    public interface RecyclerItemClickListener {
        void onClickListener(ArtistInfo album, int position);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void filterArtists(ArrayList<ArtistInfo> filteredArtists) {
        artists = filteredArtists;
        this.notifyDataSetChanged();
    }
}
