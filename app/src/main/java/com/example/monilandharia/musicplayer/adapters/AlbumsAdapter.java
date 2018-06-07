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
import com.example.monilandharia.musicplayer.models.AlbumInfo;
import com.ohoussein.playpause.PlayPauseView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.ViewHolder> {
    private ArrayList<AlbumInfo> albumInfos;
    private Context context;
    private RecyclerItemClickListener listener;

    public AlbumsAdapter(Context context, ArrayList<AlbumInfo> albumInfos, RecyclerItemClickListener listener) {
        this.context = context;
        this.albumInfos = albumInfos;
        this.listener = listener;
    }

    @Override
    public AlbumsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        if (i == R.layout.item_album) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_album, viewGroup, false);
            return new ViewHolder(view);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_seemore, viewGroup, false);
            return new ViewHolder(view, 1);
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        if (i == albumInfos.size()) {
            viewHolder.seemore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "See more albumInfos", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            AlbumInfo albumInfo = albumInfos.get(i);
            if (albumInfo != null) {
                viewHolder.tvAlbum.setText(albumInfo.getTitle());
                int songCount = albumInfo.getSongCount();
                String s = songCount>1?" songs":" song";
                viewHolder.tvSongCount.setText(songCount+s);
                Uri albumArtUri = getAlbumArtUri(albumInfo.getId());
//                String datatoplay = s.getData();
                Picasso.with(context).load(albumArtUri.toString()).placeholder(R.mipmap.ic_launcher).into(viewHolder.ivAlbumArt);

//                Picasso.with(context).load(albumInfo.getAlbumArt()).placeholder(R.mipmap.ic_launcher).into(viewHolder.ivAlbumArt);
//                viewHolder.ivAlbumArt.setImageBitmap(BitmapFactory.decodeFile(albumInfo.getAlbumArt()));
            }

            viewHolder.bind(albumInfo, listener);
        }


    }

    @Override
    public int getItemCount() {
        return albumInfos.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAlbumArt, seemore;
        TextView tvAlbum, tvSongCount;
        PlayPauseView playPauseView;

        //        TextView sdur;
        public ViewHolder(View view) {
            super(view);
            tvAlbum = view.findViewById(R.id.genreName);
            tvAlbum.setSelected(true);
            tvSongCount = view.findViewById(R.id.albumSongCount);
            ivAlbumArt = view.findViewById(R.id.albumArt);
            playPauseView = view.findViewById(R.id.albumPlayPause);
            playPauseView.bringToFront();
        }

        public ViewHolder(View view, int n) {
            super(view);
            seemore = view.findViewById(R.id.imageView2);
        }

        public void bind(final AlbumInfo song, final RecyclerItemClickListener listener) {
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
        return (position == albumInfos.size()) ? R.layout.layout_seemore : R.layout.item_album;
    }

    public Uri getAlbumArtUri(long param) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), param);
    }

    public interface RecyclerItemClickListener {
        void onClickListener(AlbumInfo albumInfo, int position);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
