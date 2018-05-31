package com.example.monilandharia.musicplayer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.monilandharia.musicplayer.adapters.DataAdapter;
import com.example.monilandharia.musicplayer.models.SongInfo;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private DataAdapter dataAdapter;
    private ArrayList myList;

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerRecentlyAdded);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        dataAdapter = new DataAdapter(getActivity().getApplicationContext(), myList, new DataAdapter.RecyclerItemClickListener() {
            @Override
            public void onClickListener(SongInfo song, int position) {
                firstLaunch = false;
                changeSelectedSong(position);
                prepareSong(song);
            }
        });
        mRecyclerView.setAdapter(dataAdapter);
        return view;
    }

}

