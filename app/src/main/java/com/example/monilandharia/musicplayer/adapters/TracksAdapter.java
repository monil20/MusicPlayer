package com.example.monilandharia.musicplayer.adapters;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.monilandharia.musicplayer.R;
import com.example.monilandharia.musicplayer.models.SongInfo;
import com.example.monilandharia.musicplayer.utilities.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import es.claucookie.miniequalizerlibrary.EqualizerView;

public class TracksAdapter extends RecyclerView.Adapter<TracksAdapter.ViewHolder> {

    private ArrayList<SongInfo> songs;
    private Context context;
    private RecyclerItemClickListener listener;

    public TracksAdapter(ArrayList<SongInfo> songs, Context context, RecyclerItemClickListener listener) {
        this.songs = songs;
        this.context = context;
        this.listener = listener;
    }

    public TracksAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_song, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        SongInfo s = songs.get(i);
        if (s != null) {
            viewHolder.sname.setText(s.getSong_name());
            viewHolder.sartist.setText(s.getSong_artist() + " ");
            viewHolder.sdur.setText("\u2022 " + Utility.getTime(s.getSong_duration()));
            Uri albart = getAlbumArtUri(s.getAlbum_id());
            String datatoplay = s.getData();
            Picasso.with(context).load(albart.toString()).placeholder(R.drawable.placeholder1).into(viewHolder.simg);

            try {
                viewHolder.soptions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popupMenu = new PopupMenu(context, v);
                        popupMenu.getMenuInflater().inflate(R.menu.menu_song, popupMenu.getMenu());
                        popupMenu.show();
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.item1:
                                        Toast.makeText(context, "Item 1 clikced", Toast.LENGTH_SHORT).show();
                                    case R.id.item2:
                                        Toast.makeText(context, "Item 2 clikced", Toast.LENGTH_SHORT).show();
                                    case R.id.item3:
                                        Toast.makeText(context, "Item 3 clikced", Toast.LENGTH_SHORT).show();
                                }
                                return true;
                            }
                        });
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        viewHolder.bind(s, listener);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sname;
        ImageView simg, soptions;
        TextView sartist, sdur;

        //        EqualizerView equalizerView;
        public ViewHolder(View view) {
            super(view);
            sname = view.findViewById(R.id.songTitle);
            simg = view.findViewById(R.id.songImage);
            sartist = view.findViewById(R.id.songArtist);
            sdur = view.findViewById(R.id.songDuration);
            soptions = view.findViewById(R.id.ivOptions);
//            equalizerView = view.findViewById(R.id.songEqualizer);
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
    public int getItemCount() {
        return songs.size();
    }

    public Uri getAlbumArtUri(long param) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), param);
    }

    public interface RecyclerItemClickListener {
        void onClickListener(SongInfo song, int position);
    }
}
