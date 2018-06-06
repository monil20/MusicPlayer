package com.example.monilandharia.musicplayer.adapters;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.monilandharia.musicplayer.R;
import com.example.monilandharia.musicplayer.models.ArtistInfo;
import com.ohoussein.playpause.PlayPauseView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ViewHolder> {
    private ArrayList<ArtistInfo> artists;
    private Context context;
    private RecyclerItemClickListener listener;

    public ArtistsAdapter(Context context, ArrayList<ArtistInfo> artists, RecyclerItemClickListener listener) {
        this.context = context;
        this.artists = artists;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        if (i == R.layout.item_artist) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_artist, viewGroup, false);
            return new ViewHolder(view);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_seemore, viewGroup, false);
            return new ViewHolder(view, 1);
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        if (i == artists.size()) {
            viewHolder.seemore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "See more artists", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            ArtistInfo artist = artists.get(i);
            if (artist != null) {
                viewHolder.tvArtist.setText(artist.getArtistName());
                viewHolder.tvArtistStats.setText(artist.getAlbumsCount() + " albums, "+artist.getTracksCount()+" songs");
//                Uri albumArtUri = getAlbumArtUri(album.getAlbumId());
//                String datatoplay = s.getData();
//                Picasso.with(context).load(album.getAlbumArt()).placeholder(R.mipmap.ic_launcher).into(viewHolder.ivAlbumArt);
//                viewHolder.ivAlbumArt.setImageBitmap(BitmapFactory.decodeFile(artist.getAlbumArt()));
            }

            viewHolder.bind(artist, listener);
        }


    }

    @Override
    public int getItemCount() {
        return artists.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAlbumArt1, ivAlbumArt2, ivAlbumArt3, ivAlbumArt4, seemore;
        TextView tvArtist, tvArtistStats;
        PlayPauseView playPauseView;

        //        TextView sdur;
        public ViewHolder(View view) {
            super(view);
            tvArtist = view.findViewById(R.id.genreName);
            tvArtist.setSelected(true);
            tvArtistStats = view.findViewById(R.id.songCount);
            ivAlbumArt1 = view.findViewById(R.id.albumArt1);
            ivAlbumArt2 = view.findViewById(R.id.albumArt2);
            ivAlbumArt3 = view.findViewById(R.id.albumArt3);
            ivAlbumArt4 = view.findViewById(R.id.albumArt4);
            playPauseView = view.findViewById(R.id.songPlayPause);
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
        return (position == artists.size()) ? R.layout.layout_seemore : R.layout.item_artist;
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
}
