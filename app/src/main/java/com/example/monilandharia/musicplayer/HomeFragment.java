package com.example.monilandharia.musicplayer;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.monilandharia.musicplayer.adapters.DataAdapter;
import com.example.monilandharia.musicplayer.models.SongInfo;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerRecentlyAdded, recyclerArtists, recyclerAlbums, recyclerTracks, recyclerGenres;
    private DataAdapter adapterRecentlyAdded, adapterArtists, adapterAlbums, adapterTracks, adapterGenres;
    private ArrayList myList;
    private Cursor musiccursor;
    private ArrayList recentlyAdded, albums, tracks, artists, genres;
    private RecyclerView.LayoutManager layoutManager;

    String[] proj = {MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM_ID};

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initView(view);
        initializeRecentlyAdded(view);
//        initArtists(view);
//        initAlbums(view);
//        initTracks(view);
//        initGenre(view);

        return view;
    }

    private void initView(View view) {

        musiccursor = getActivity().managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                proj, null, null, null);

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

    }

    private void initializeRecentlyAdded(View view) {
        recyclerRecentlyAdded = view.findViewById(R.id.recyclerRecentlyAdded);
        recyclerRecentlyAdded.setHasFixedSize(true);
        recyclerRecentlyAdded.setLayoutManager(layoutManager);
        recyclerRecentlyAdded.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recentlyAdded = prepareData();
        adapterRecentlyAdded = new DataAdapter(getActivity().getApplicationContext(), recentlyAdded, new DataAdapter.RecyclerItemClickListener() {
            @Override
            public void onClickListener(SongInfo song, int position) {
//                firstLaunch = false;
//                changeSelectedSong(position);
//                prepareSong(song);
            }
        });
        recyclerRecentlyAdded.setAdapter(adapterRecentlyAdded);
    }

//    private void prepareSong(SongInfo song)
//    {
//        current_song_duration = song.getSong_duration();
//        tv_title.setText(song.getSong_name());
//        iv_play.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.selector_play));
//        tv_duration.setText(Utility.getTime(current_song_duration));
//
//        mMediaPlayer.reset();
//        try{
//            mMediaPlayer.setDataSource(song.getData());
//            mMediaPlayer.prepareAsync();
//        }
//        catch (Exception e)
//        {
//
//        }
//    }

    private ArrayList prepareData() {
        int count = 0;
        ArrayList songs = new ArrayList();
        if (musiccursor.moveToLast()) {
            do {
                try {
                    if (count++<6) {
                        String n = musiccursor.getString(musiccursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                        String a = musiccursor.getString(musiccursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                        String d = musiccursor.getString(musiccursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                        long album_id = musiccursor.getInt(musiccursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                        String datas = musiccursor.getString(musiccursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                        SongInfo songInfo = new SongInfo(n, a, Integer.parseInt(d), album_id, datas);
                        songs.add(songInfo);
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            } while (musiccursor.moveToPrevious());
        }
        Toast.makeText(getActivity().getApplicationContext(), songs.size()+"", Toast.LENGTH_SHORT).show();
        return songs;
    }

}

