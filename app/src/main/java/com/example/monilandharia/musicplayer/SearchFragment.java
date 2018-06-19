package com.example.monilandharia.musicplayer;


import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.monilandharia.musicplayer.adapters.TracksAdapter;
import com.example.monilandharia.musicplayer.models.SongInfo;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private EditText searchEditText;
    private TextView searchTextView;
    private RecyclerView searchRecyclerView;
    private TracksAdapter tracksAdapter;
    private Cursor songCursor;
    private LinearLayoutManager layoutManager;
    private ArrayList<SongInfo> songs = new ArrayList<>();

    String[] trackProjection = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM,
    };

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initViews(view);

        songCursor = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, trackProjection, null, null, MediaStore.Audio.Media.TITLE);
        while (songCursor.moveToNext()) {
            songs.add(new SongInfo(songCursor.getInt(0), songCursor.getString(1), songCursor.getString(2), songCursor.getInt(3), songCursor.getInt(4), songCursor.getString(5), songCursor.getString(6)));
        }

        tracksAdapter = new TracksAdapter(songs, getActivity(), new TracksAdapter.RecyclerItemClickListener() {
            @Override
            public void onClickListener(SongInfo song, int position) {

            }
        });

        searchRecyclerView.setAdapter(tracksAdapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                SearchFragment.this.arrayAdapter.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        return view;
    }

    private void filter(String text) {
        ArrayList<SongInfo> filteredSongs = new ArrayList<>();

        //looping through existing elements
        for (SongInfo s : songs  ) {
            //if the existing elements contains the search input
            if (s.getSong_name().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                Log.i("XXX",s.getSong_name()+" "+text.toLowerCase());
                filteredSongs.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        tracksAdapter.filterSongs(filteredSongs);
    }



    private void initViews(View view) {
        searchEditText = view.findViewById(R.id.searchEditText);
        searchTextView = view.findViewById(R.id.searchTextView);

        searchRecyclerView = view.findViewById(R.id.searchRecyclerView);
        layoutManager = new LinearLayoutManager(getActivity());

        searchRecyclerView.setHasFixedSize(true);
        searchRecyclerView.setLayoutManager(layoutManager);
        searchRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//        songs.clear();
//        songCursor = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, trackProjection, null, null, MediaStore.Audio.Media.TITLE);
//        while (songCursor.moveToNext()) {
//            songs.add(new SongInfo(songCursor.getInt(0), songCursor.getString(1), songCursor.getString(2), songCursor.getInt(3), songCursor.getInt(4), songCursor.getString(5), songCursor.getString(6)));
//        }
//
//        tracksAdapter = new TracksAdapter(songs, getActivity(), new TracksAdapter.RecyclerItemClickListener() {
//            @Override
//            public void onClickListener(SongInfo song, int position) {
//
//            }
//        });
//
//        searchRecyclerView.setAdapter(tracksAdapter);
//
//
//    }
}
