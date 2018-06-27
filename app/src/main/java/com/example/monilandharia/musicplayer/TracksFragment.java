package com.example.monilandharia.musicplayer;


import android.content.Intent;
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
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.monilandharia.musicplayer.adapters.TracksAdapter;
import com.example.monilandharia.musicplayer.customViews.RalewayTextView;
import com.example.monilandharia.musicplayer.dataLoaders.TrackLoader;
import com.example.monilandharia.musicplayer.models.SongInfo;
import com.example.monilandharia.musicplayer.services.MyService;
import com.example.monilandharia.musicplayer.utilities.Utility;
import com.ohoussein.playpause.PlayPauseView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TracksFragment extends Fragment {

    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerTracks;
    private TracksAdapter tracksAdapter;

    public TracksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tracks, container, false);
        initView(view);
        if (getActivity() != null) {
            new loadTracks().execute("");
        }
        return view;
    }

    private void initView(View view) {

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        recyclerTracks = view.findViewById(R.id.recyclerTracks);
        recyclerTracks.setHasFixedSize(true);
        recyclerTracks.setLayoutManager(layoutManager);
        recyclerTracks.setOverScrollMode(View.OVER_SCROLL_NEVER);

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerTracks.getContext(),
//                layoutManager.getOrientation());
//        recyclerTracks.addItemDecoration(dividerItemDecoration);
    }

    private class loadTracks extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            if (getActivity() != null)
                tracksAdapter = new TracksAdapter(MainActivity.songs, getActivity(), new TracksAdapter.RecyclerItemClickListener() {
                    @Override
                    public void onClickListener(SongInfo song, int position) {
                        if (MainActivity.songs.size() != 0) {
                            Log.i("SONG", "Yes songs");
                            Utility.playSong(song,MainActivity.songs,position,getActivity());
                        } else {
                            Log.i("SONG", "No songs");
                        }
                    }
                }, false, null);
            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {
            if (tracksAdapter != null) {
                recyclerTracks.setAdapter(tracksAdapter);
            }
        }
    }


}
