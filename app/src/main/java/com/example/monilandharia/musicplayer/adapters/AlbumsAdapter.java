package com.example.monilandharia.musicplayer.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.monilandharia.musicplayer.R;
import com.example.monilandharia.musicplayer.models.AlbumInfo;
import com.example.monilandharia.musicplayer.utilities.Utility;
import com.ohoussein.playpause.PlayPauseView;
import com.squareup.picasso.Picasso;

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
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_all_albums, null);
        ViewHolder rcv = new ViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

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
            Uri albumArtUri = Utility.getAlbumArtUri(albumInfo.getId());
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
        CardView cardView;

        //        TextView sdur;
        public ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.card_view);
            tvAlbum = view.findViewById(R.id.playlistName);
            tvAlbum.setSelected(true);
            tvAlbumArtist = view.findViewById(R.id.albumArtist);
            ivAlbumArt = view.findViewById(R.id.playlistArt);
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

    public interface RecyclerItemClickListener {
        void onClickListener(AlbumInfo albumInfo, int position);
    }

}
