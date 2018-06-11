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
    private FragmentManager fragmentManager;

    public AlbumsAdapter(Context context, ArrayList<AlbumInfo> albumInfos, RecyclerItemClickListener listener, FragmentManager fragmentManager) {
        this.context = context;
        this.albumInfos = albumInfos;
        this.listener = listener;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public AlbumsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_all_albums, null);
        ViewHolder rcv = new ViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

//        if (i == albumInfos.size()) {
//            viewHolder.seemore.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.fragment_home, new AlbumsFragment());
//                    fragmentTransaction.commit();
//                }
//            });
//        } else {
//            AlbumInfo albumInfo = albumInfos.get(i);
//            if (albumInfo != null) {
//                viewHolder.tvAlbum.setText(albumInfo.getTitle());
//                int songCount = albumInfo.getSongCount();
//                String s = songCount > 1 ? " songs" : " song";
//                viewHolder.tvSongCount.setText(songCount + s);
//                Uri albumArtUri = getAlbumArtUri(albumInfo.getId());
////                String datatoplay = s.getData();
//                Picasso.with(context).load(albumArtUri.toString()).placeholder(R.drawable.placeholder1).into(viewHolder.ivAlbumArt);
//
////                Picasso.with(context).load(albumInfo.getAlbumArt()).placeholder1(R.mipmap.ic_launcher).into(viewHolder.ivAlbumArt);
////                viewHolder.ivAlbumArt.setImageBitmap(BitmapFactory.decodeFile(albumInfo.getAlbumArt()));
//            }
//
//            viewHolder.bind(albumInfo, listener);
//        }

        AlbumInfo albumInfo = albumInfos.get(i);
        if (albumInfo != null) {
            viewHolder.tvAlbum.setText(albumInfo.getTitle());
            viewHolder.tvAlbumArtist.setText(albumInfo.getArtistName());
            Uri albumArtUri = getAlbumArtUri(albumInfo.getId());
//                String datatoplay = s.getData();
            Picasso.with(context).load(albumArtUri.toString()).placeholder(R.drawable.placeholder1).into(viewHolder.ivAlbumArt);

//                Picasso.with(context).load(albumInfo.getAlbumArt()).placeholder1(R.mipmap.ic_launcher).into(viewHolder.ivAlbumArt);
//                viewHolder.ivAlbumArt.setImageBitmap(BitmapFactory.decodeFile(albumInfo.getAlbumArt()));
        }

        viewHolder.bind(albumInfo, listener);

    }

    @Override
    public int getItemCount() {
        return albumInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAlbumArt;
        TextView tvAlbum, tvAlbumArtist;
        PlayPauseView playPauseView;

        //        TextView sdur;
        public ViewHolder(View view) {
            super(view);
            tvAlbum = view.findViewById(R.id.albumName);
            tvAlbum.setSelected(true);
            tvAlbumArtist = view.findViewById(R.id.albumArtist);
            ivAlbumArt = view.findViewById(R.id.albumArt);
            playPauseView = view.findViewById(R.id.albumPlayPause);
            playPauseView.bringToFront();
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
        return R.layout.item_all_albums;
    }

    public Uri getAlbumArtUri(long param) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), param);
    }

    public interface RecyclerItemClickListener {
        void onClickListener(AlbumInfo albumInfo, int position);
    }

}
