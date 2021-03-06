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

import com.example.monilandharia.musicplayer.AlbumsFragment;
import com.example.monilandharia.musicplayer.MainActivity;
import com.example.monilandharia.musicplayer.R;
import com.example.monilandharia.musicplayer.models.AlbumInfo;
import com.ohoussein.playpause.PlayPauseView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class AlbumsPreviewAdapter extends RecyclerView.Adapter<AlbumsPreviewAdapter.ViewHolder> {
    private ArrayList<AlbumInfo> albumInfos;
    private Context context;
    private RecyclerItemClickListener listener;
    private FragmentTransaction fragmentTransaction;
    private boolean isHome;

    public AlbumsPreviewAdapter(Context context, ArrayList<AlbumInfo> albumInfos, RecyclerItemClickListener listener, boolean isHome) {
        this.context = context;
        this.albumInfos = albumInfos;
        this.listener = listener;
        this.isHome = isHome;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        if (isHome) {
            if (i == R.layout.item_album) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_album, viewGroup, false);
                return new ViewHolder(view);
            } else {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_seemore, viewGroup, false);
                return new ViewHolder(view, 1);
            }
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_album, viewGroup, false);
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        if (isHome) {
            if (i == albumInfos.size()) {
                viewHolder.seemore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragmentTransaction = MainActivity.fragmentManager.beginTransaction();
                        fragmentTransaction.addToBackStack("HOME");
                        fragmentTransaction.show(MainActivity.fragmentAlbums);
                        fragmentTransaction.hide(MainActivity.fragmentHome);
                        fragmentTransaction.commit();
                    }
                });
            } else {
                AlbumInfo albumInfo = albumInfos.get(i);
                if (albumInfo != null) {
                    viewHolder.tvAlbum.setText(albumInfo.getTitle());
                    int songCount = albumInfo.getSongCount();
                    String s = songCount > 1 ? " songs" : " song";
                    viewHolder.tvSongCount.setText(songCount + s);
                    Uri albumArtUri = getAlbumArtUri(albumInfo.getId());
                    //                String datatoplay = s.getData();
                    Picasso.with(context).load(albumArtUri.toString()).placeholder(R.drawable.placeholder1).into(viewHolder.ivAlbumArt);

                    //                Picasso.with(context).load(albumInfo.getAlbumArt()).placeholder1(R.mipmap.ic_launcher).into(viewHolder.ivAlbumArt);
                    //                viewHolder.ivAlbumArt.setImageBitmap(BitmapFactory.decodeFile(albumInfo.getAlbumArt()));
                }

                viewHolder.bind(albumInfo, listener);
            }
        } else {
            AlbumInfo albumInfo = albumInfos.get(i);
            if (albumInfo != null) {
                viewHolder.tvAlbum.setText(albumInfo.getTitle());
                int songCount = albumInfo.getSongCount();
                String s = songCount > 1 ? " songs" : " song";
                viewHolder.tvSongCount.setText(songCount + s);
                Uri albumArtUri = getAlbumArtUri(albumInfo.getId());
                //                String datatoplay = s.getData();
                Picasso.with(context).load(albumArtUri.toString()).placeholder(R.drawable.placeholder1).into(viewHolder.ivAlbumArt);

                //                Picasso.with(context).load(albumInfo.getAlbumArt()).placeholder1(R.mipmap.ic_launcher).into(viewHolder.ivAlbumArt);
                //                viewHolder.ivAlbumArt.setImageBitmap(BitmapFactory.decodeFile(albumInfo.getAlbumArt()));
            }

            viewHolder.bind(albumInfo, listener);
        }


    }

    @Override
    public int getItemCount() {
        if (isHome) {
            return albumInfos.size() + 1;
        } else {
            return albumInfos.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAlbumArt, seemore;
        TextView tvAlbum, tvSongCount;
        PlayPauseView playPauseView;

        //        TextView sdur;
        public ViewHolder(View view) {
            super(view);
            tvAlbum = view.findViewById(R.id.playlistName);
            tvAlbum.setSelected(true);
            tvSongCount = view.findViewById(R.id.albumStats);
            ivAlbumArt = view.findViewById(R.id.playlistArt);
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
        if (isHome) {
            return (position == albumInfos.size()) ? R.layout.layout_seemore : R.layout.item_album;
        } else {
            return R.layout.item_album;
        }
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

    public void filterAlbums(ArrayList<AlbumInfo> filteredAlbums) {
        albumInfos = filteredAlbums;
        this.notifyDataSetChanged();
    }

}
