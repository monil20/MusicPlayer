package com.example.monilandharia.musicplayer;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.monilandharia.musicplayer.adapters.TracksAdapter;
import com.example.monilandharia.musicplayer.database.model.Playlists;
import com.example.monilandharia.musicplayer.models.SongInfo;
import com.example.monilandharia.musicplayer.utilities.Utility;
import com.ohoussein.playpause.PlayPauseView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistDetailsFragment extends Fragment {

    private ImageView playlistArt;
    private TextView playlistName;
    private PlayPauseView playPauseView;
    private RecyclerView playlistRecyclerView;
    private Bundle bundle;

    private int playlistId;
    private ArrayList<SongInfo> playlistSongs;

    private TracksAdapter adapter;

    public PlaylistDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playlist_details, container, false);
        initViews(view);

        bundle = getArguments();
        playlistId = bundle.getInt("playlistId");
        playlistSongs = MainActivity.db.getSongsForPlaylist(playlistId);
        for (SongInfo song : playlistSongs) {
            Log.i("HELL", song.getSong_id() + song.getSong_name());
        }
        Uri albumArtUri = Utility.getAlbumArtUri(playlistSongs.get(0).getAlbum_id());
        Picasso.with(getContext()).load(albumArtUri.toString()).placeholder(R.drawable.placeholder1).into(playlistArt);
        playlistName.setText(bundle.getString("playlistName"));

        adapter = new TracksAdapter(playlistSongs, getActivity(), new TracksAdapter.RecyclerItemClickListener() {
            @Override
            public void onClickListener(SongInfo song, int position) {

            }
        }, true, new Playlists(bundle.getInt("playlistId"),bundle.getString("playlistName")));

        playlistRecyclerView.setAdapter(adapter);

        playPauseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    private void initViews(View view) {
        playlistArt = view.findViewById(R.id.playlistArt);
        playlistName = view.findViewById(R.id.playlistName);
        playPauseView = view.findViewById(R.id.playPauseView);
        playlistRecyclerView = view.findViewById(R.id.playlistRecycler);

        playlistRecyclerView.setHasFixedSize(true);
        playlistRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        playlistRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

}
