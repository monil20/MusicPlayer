package com.example.monilandharia.musicplayer;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.monilandharia.musicplayer.adapters.TracksAdapter;
import com.example.monilandharia.musicplayer.adapters.TracksPreviewAdapter;
import com.example.monilandharia.musicplayer.dataLoaders.TrackLoader;
import com.example.monilandharia.musicplayer.models.SongInfo;


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
        if(getActivity()!=null)
        {
            new loadTracks().execute("");
        }
        return view;
    }

    private void initView(View view) {
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false);

        recyclerTracks = view.findViewById(R.id.recyclerTracks);
        recyclerTracks.setHasFixedSize(true);
        recyclerTracks.setLayoutManager(layoutManager);
        recyclerTracks.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    private class loadTracks extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            if (getActivity() != null)
                tracksAdapter = new TracksAdapter(TrackLoader.getAllTracks(getActivity().getApplicationContext()),getActivity(), new TracksAdapter.RecyclerItemClickListener(){
                    @Override
                    public void onClickListener(SongInfo albumInfo, int position) {

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
