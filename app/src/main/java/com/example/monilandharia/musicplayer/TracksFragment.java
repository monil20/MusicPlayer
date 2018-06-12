package com.example.monilandharia.musicplayer;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.monilandharia.musicplayer.adapters.TracksAdapter;
import com.example.monilandharia.musicplayer.adapters.TracksPreviewAdapter;
import com.example.monilandharia.musicplayer.customViews.RalewayTextView;
import com.example.monilandharia.musicplayer.dataLoaders.TrackLoader;
import com.example.monilandharia.musicplayer.models.SongInfo;
import com.ohoussein.playpause.PlayPauseView;


/**
 * A simple {@link Fragment} subclass.
 */
public class TracksFragment extends Fragment {


    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerTracks;
    private TracksAdapter tracksAdapter;

    //instances of toolbar
    private SeekBar seekBar;
    private ImageView songArt,prev,next,repeat,shuffle;
    private RalewayTextView album_track,start_time,end_time,album_artist_name;
    private PlayPauseView play_pause;

    public TracksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tracks, container, false);
        initView(view);
        if(getActivity()!=null)
        {
            new loadTracks().execute("");
        }
        return view;
    }

    private void initView(View view) {

        seekBar = view.findViewById(R.id.seekBar);
        songArt = view.findViewById(R.id.songArt);
        prev = view.findViewById(R.id.prev);
        next = view.findViewById(R.id.next);
        repeat = view.findViewById(R.id.repeat);
        shuffle = view.findViewById(R.id.shuffle);
        album_track = view.findViewById(R.id.album_track);
        start_time = view.findViewById(R.id.start_time);
        end_time = view.findViewById(R.id.end_time);
        album_artist_name = view.findViewById(R.id.album_artist_name);
        play_pause = view.findViewById(R.id.play_pause);

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false);

        recyclerTracks = view.findViewById(R.id.recyclerTracks);
        recyclerTracks.setHasFixedSize(true);
        recyclerTracks.setLayoutManager(layoutManager);
        recyclerTracks.setOverScrollMode(View.OVER_SCROLL_NEVER);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerTracks.getContext(),
                layoutManager.getOrientation());
        recyclerTracks.addItemDecoration(dividerItemDecoration);
    }

    private class loadTracks extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            if (getActivity() != null)
                tracksAdapter = new TracksAdapter(TrackLoader.getAllTracks(getActivity().getApplicationContext()),getActivity(), new TracksAdapter.RecyclerItemClickListener(){
                    @Override
                    public void onClickListener(SongInfo song, int position) {

                    }
                });
            return "Executed";        }

        @Override
        protected void onPostExecute(String s) {
            if(tracksAdapter !=null) {
                recyclerTracks.setAdapter(tracksAdapter);
            }
        }
    }

}
