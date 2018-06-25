package com.example.monilandharia.musicplayer.adapters;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.monilandharia.musicplayer.MainActivity;
import com.example.monilandharia.musicplayer.R;
import com.example.monilandharia.musicplayer.database.DatabaseHelper;
import com.example.monilandharia.musicplayer.database.model.Playlists;
import com.example.monilandharia.musicplayer.models.SongInfo;
import com.example.monilandharia.musicplayer.utilities.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import es.claucookie.miniequalizerlibrary.EqualizerView;

public class TracksAdapter extends RecyclerView.Adapter<TracksAdapter.ViewHolder> {

    private ArrayList<SongInfo> songs;
    private Context context;
    private RecyclerItemClickListener listener;
    private SongInfo s;
    private boolean isInPlaylist;
    private Playlists playlist;

    public TracksAdapter(ArrayList<SongInfo> songs, Context context, RecyclerItemClickListener listener, boolean isInPlaylist, Playlists playlist) {
        this.songs = songs;
        this.context = context;
        this.listener = listener;
        this.isInPlaylist = isInPlaylist;
        this.playlist = playlist;
    }

    public TracksAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_song, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        s = songs.get(i);
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
                        s = songs.get(i);
                        final PopupMenu popupMenu;
                        popupMenu = new PopupMenu(context, v);
                        popupMenu.getMenuInflater().inflate(R.menu.menu_song, popupMenu.getMenu());

                        if (!isInPlaylist) {
                            popupMenu.getMenu().add("Play next").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                            });
                            popupMenu.getMenu().add("Add to queue").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                            });
                            popupMenu.getMenu().add("Add to favorites").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    MainActivity.db.addFav(s.getSong_id());
                                    Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                            });
                            SubMenu subMenu = (popupMenu.getMenu().addSubMenu("Add to playlist"));
                            ArrayList<Playlists> playlists = MainActivity.db.getPlaylists();
                            int itemId = Menu.FIRST;
                            int itemPos = 0;
                            for (Playlists playlist :
                                    playlists) {
                                subMenu.add(1, itemId, itemPos, playlist.getTitle()).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        MainActivity.db.addSongToPlaylist(item.getTitle().toString(), s.getSong_id());
                                        Toast.makeText(context, "Added to " + item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                        Log.i("HELL", s.getSong_id() + s.getSong_name() + item.getTitle().toString());
                                        return false;
                                    }
                                });
                                itemId++;
                                itemPos++;
                            }
                            subMenu.add(1, itemId, itemPos, "Add new").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(final MenuItem item) {
                                    final Dialog dialog = new Dialog(context);
                                    dialog.setContentView(R.layout.dialog_layout);
                                    dialog.setCanceledOnTouchOutside(true);
                                    final EditText editText = dialog.findViewById(R.id.addPlaylistEditText);
                                    Button button = dialog.findViewById(R.id.addPlaylistButton);
                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            double res = MainActivity.db.addPlaylist(editText.getText().toString().trim());
                                            if (res != -1) {
                                                MainActivity.db.addSongToPlaylist(editText.getText().toString().trim(), s.getSong_id());
                                                Log.i("HELL", s.getSong_id() + s.getSong_name() + item.getTitle().toString());
                                                Toast.makeText(context, "Added to " + editText.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            } else {
                                                Toast.makeText(context, "Playlist already exists", Toast.LENGTH_SHORT).show();
                                                editText.setText("");
                                                editText.requestFocus();
                                            }
                                        }
                                    });
                                    dialog.show();
                                    Window window = dialog.getWindow();
                                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                    window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                    return false;
                                }
                            });
                        } else {
                            popupMenu.getMenu().add("Play next").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                            });
                            popupMenu.getMenu().add("Add to queue").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                            });
                            popupMenu.getMenu().add("Go to album").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                            });
                            popupMenu.getMenu().add("Go to artist").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                            });
                            popupMenu.getMenu().add("Remove from playlist").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    MainActivity.db.removeSongFromPlaylist(s.getSong_id(), playlist.getTitle());
                                    filterSongs(MainActivity.db.getSongsForPlaylist(playlist.getPlaylistId()));
                                    return false;
                                }
                            });
                        }
                        popupMenu.show();
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

    public void filterSongs(ArrayList<SongInfo> filteredSongs) {
        songs = filteredSongs;
        this.notifyDataSetChanged();
    }
}
