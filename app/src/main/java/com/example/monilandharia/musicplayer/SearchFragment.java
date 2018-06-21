package com.example.monilandharia.musicplayer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.monilandharia.musicplayer.adapters.AlbumsPreviewAdapter;
import com.example.monilandharia.musicplayer.adapters.ArtistAdapter;
import com.example.monilandharia.musicplayer.adapters.ArtistsPreviewAdapter;
import com.example.monilandharia.musicplayer.adapters.TracksAdapter;
import com.example.monilandharia.musicplayer.dataLoaders.AlbumLoader;
import com.example.monilandharia.musicplayer.dataLoaders.ArtistLoader;
import com.example.monilandharia.musicplayer.dataLoaders.TrackLoader;
import com.example.monilandharia.musicplayer.models.AlbumInfo;
import com.example.monilandharia.musicplayer.models.ArtistInfo;
import com.example.monilandharia.musicplayer.models.SongInfo;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private EditText searchEditText;
    private TextView songsTextView, albumsTextView, artistsTextView;
    private RecyclerView songsRecyclerView, albumsRecyclerView, artistsRecyclerView;
    private TracksAdapter tracksAdapter;
    private AlbumsPreviewAdapter albumsPreviewAdapter;
    private ArtistsPreviewAdapter artistsPreviewAdapter;
    private LinearLayoutManager layoutManager, layoutManager2, layoutManager3;
    private boolean hasSongs, hasAlbums, hasArtists;
    private ArrayList<SongInfo> filteredSongs = new ArrayList<>();
    private ArrayList<AlbumInfo> filteredAlbums = new ArrayList<>();
    private ArrayList<ArtistInfo> filteredArtists = new ArrayList<>();

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initViews(view);
        initAdapters();

        searchEditText.requestFocus();

        songsRecyclerView.setAdapter(tracksAdapter);
        albumsRecyclerView.setAdapter(albumsPreviewAdapter);
        artistsRecyclerView.setAdapter(artistsPreviewAdapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!searchEditText.getText().toString().trim().matches("")) {
                    filteredSongs.clear();
                    filteredAlbums.clear();
                    filteredArtists.clear();
                    hasSongs = false;
                    hasAlbums = false;
                    hasArtists = false;
                    filter(s.toString());
                }else{
                    artistsTextView.setVisibility(View.GONE);
                    albumsTextView.setVisibility(View.GONE);
                    songsTextView.setVisibility(View.GONE);

                    artistsRecyclerView.setVisibility(View.GONE);
                    albumsRecyclerView.setVisibility(View.GONE);
                    songsRecyclerView.setVisibility(View.GONE);
                }

            }
        });

        return view;
    }

    private void initAdapters() {
        tracksAdapter = new TracksAdapter(MainActivity.songs, getActivity(), new TracksAdapter.RecyclerItemClickListener() {
            @Override
            public void onClickListener(SongInfo song, int position) {

            }
        });

        albumsPreviewAdapter = new AlbumsPreviewAdapter(getActivity(), MainActivity.albums,
                new AlbumsPreviewAdapter.RecyclerItemClickListener() {
                    @Override
                    public void onClickListener(AlbumInfo albumInfo, int position) {

                    }
                }, getActivity().getSupportFragmentManager(), false);

        artistsPreviewAdapter = new ArtistsPreviewAdapter(getActivity(), MainActivity.artists, new ArtistsPreviewAdapter.RecyclerItemClickListener() {
            @Override
            public void onClickListener(ArtistInfo artistInfo, int position) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putInt("artistAlbumId", artistInfo.getSongId());
                bundle.putLong("artistId", artistInfo.get_id());
                bundle.putString("artistName", artistInfo.getArtistName());
                ArtistDetailsFragment frag = new ArtistDetailsFragment();
                frag.setArguments(bundle);
                //ImageView albumImage = view2.findViewById(R.id.realAlbumArt);
                //Uri albumArtUri = Utility.getAlbumArtUri(albumInfo.getId());
                fragmentTransaction.replace(R.id.fragment_home, frag).addToBackStack("ALBUMS").commit();
                //Picasso.with(getContext()).load(albumArtUri.toString()).placeholder(R.drawable.placeholder1).into(albumImage);
            }
        }, getActivity().getSupportFragmentManager(), false);
    }

    private void filter(String text) {

        artistsTextView.setVisibility(View.GONE);
        albumsTextView.setVisibility(View.GONE);
        songsTextView.setVisibility(View.GONE);

        artistsRecyclerView.setVisibility(View.GONE);
        albumsRecyclerView.setVisibility(View.GONE);
        songsRecyclerView.setVisibility(View.GONE);

        //looping through existing elements
        for (SongInfo s : MainActivity.songs) {
            //if the existing elements contains the search input
            if (s.getSong_name().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filteredSongs.add(s);
                hasSongs = true;
            }
        }

        for (AlbumInfo a : MainActivity.albums) {
            if (a.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredAlbums.add(a);
                hasAlbums = true;
            }
        }

        for (ArtistInfo a : MainActivity.artists) {
            if (a.getArtistName().toLowerCase().contains(text.toLowerCase())) {
                filteredArtists.add(a);
                hasArtists = true;
            }
        }

        //calling a method of the adapter class and passing the filtered list
        tracksAdapter.filterSongs(filteredSongs);
        albumsPreviewAdapter.filterAlbums(filteredAlbums);
        artistsPreviewAdapter.filterArtists(filteredArtists);

        if (hasSongs) {
            songsTextView.setVisibility(View.VISIBLE);
            songsRecyclerView.setVisibility(View.VISIBLE);
        }
        if (hasAlbums) {
            albumsTextView.setVisibility(View.VISIBLE);
            albumsRecyclerView.setVisibility(View.VISIBLE);
        }
        if (hasArtists) {
            artistsTextView.setVisibility(View.VISIBLE);
            artistsRecyclerView.setVisibility(View.VISIBLE);
        }

    }


    private void initViews(View view) {
        searchEditText = view.findViewById(R.id.searchEditText);

        songsTextView = view.findViewById(R.id.songsTextView);
        albumsTextView = view.findViewById(R.id.albumsTextView);
        artistsTextView = view.findViewById(R.id.artistsTextView);

        songsRecyclerView = view.findViewById(R.id.songsRecyclerView);
        albumsRecyclerView = view.findViewById(R.id.albumsRecyclerView);
        artistsRecyclerView = view.findViewById(R.id.artistsRecyclerView);

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager2 = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManager3 = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        songsRecyclerView.setHasFixedSize(true);
        songsRecyclerView.setLayoutManager(layoutManager);
        songsRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        albumsRecyclerView.setHasFixedSize(true);
        albumsRecyclerView.setLayoutManager(layoutManager2);
        albumsRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        artistsRecyclerView.setHasFixedSize(true);
        artistsRecyclerView.setLayoutManager(layoutManager3);
        artistsRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        artistsTextView.setVisibility(View.INVISIBLE);
        albumsTextView.setVisibility(View.INVISIBLE);
        songsTextView.setVisibility(View.INVISIBLE);

        artistsRecyclerView.setVisibility(View.INVISIBLE);
        albumsRecyclerView.setVisibility(View.INVISIBLE);
        songsRecyclerView.setVisibility(View.INVISIBLE);

    }

}
