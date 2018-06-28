package com.example.monilandharia.musicplayer;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.monilandharia.musicplayer.adapters.AlbumsPreviewAdapter;
import com.example.monilandharia.musicplayer.adapters.TracksAdapter;
import com.example.monilandharia.musicplayer.dataLoaders.ArtistAlbumLoader;
import com.example.monilandharia.musicplayer.dataLoaders.ArtistSongLoader;
import com.example.monilandharia.musicplayer.models.AlbumInfo;
import com.example.monilandharia.musicplayer.models.SongInfo;
import com.example.monilandharia.musicplayer.utilities.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArtistDetailsFragment extends Fragment {

    private int artistAlbumId;
    private long artistId;
    private String artistName;
    private RecyclerView artistAlbumsRecycler, artistSongsRecycler;
    private TracksAdapter tracksAdapter;
    private AlbumsPreviewAdapter albumsPreviewAdapter;
    private ImageView artistImage;
    private TextView artistNameTextview;
    private RecyclerView.LayoutManager songLayoutManager, albumLayoutManager;
    private ArrayList<SongInfo> songList;

    public ArtistDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_artist_details, container, false);
        initViews(view);
        Bundle bundle = getArguments();
        artistAlbumId = bundle.getInt("artistAlbumId");
        artistName = bundle.getString("artistName");
        artistId = bundle.getLong("artistId");

        Uri albumArtUri = Utility.getAlbumArtUri(artistAlbumId);
        Picasso.with(getContext()).load(albumArtUri.toString()).placeholder(R.drawable.placeholder1).into(artistImage);
        artistNameTextview.setText(artistName);
        if (getActivity() != null) {
            new loadArtistAlbumsAndSongs().execute("");
        }
        return view;
    }

    private void initViews(View view) {
        artistNameTextview = view.findViewById(R.id.playlistName);
        artistImage = view.findViewById(R.id.playlistArt);
        songLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        albumLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        artistAlbumsRecycler = view.findViewById(R.id.recyclerArtistAlbums);
        artistAlbumsRecycler.setHasFixedSize(true);
        artistAlbumsRecycler.setLayoutManager(albumLayoutManager);
        artistAlbumsRecycler.setOverScrollMode(View.OVER_SCROLL_NEVER);

        artistSongsRecycler = view.findViewById(R.id.recyclerArtistSongs);
        artistSongsRecycler.setHasFixedSize(true);
        artistSongsRecycler.setLayoutManager(songLayoutManager);
        artistSongsRecycler.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    private class loadArtistAlbumsAndSongs extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            if (getActivity() != null) {
                albumsPreviewAdapter = new AlbumsPreviewAdapter(getActivity(), ArtistAlbumLoader.getAlbumsForArtist(getActivity(), artistId), new AlbumsPreviewAdapter.RecyclerItemClickListener() {
                    @Override
                    public void onClickListener(AlbumInfo albumInfo, int position) {
                        FragmentTransaction fragmentTransaction = MainActivity.fragmentManager.beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putLong("albumId", albumInfo.getId());
                        bundle.putString("albumName", albumInfo.getTitle());
                        bundle.putString("artistName", albumInfo.getArtistName());
                        AlbumDetailsFragment frag = new AlbumDetailsFragment();
                        frag.setArguments(bundle);
                        //ImageView albumImage = view2.findViewById(R.id.realAlbumArt);
                        //Uri albumArtUri = Utility.getAlbumArtUri(albumInfo.getId());
                        fragmentTransaction.replace(R.id.fragment_home, frag).addToBackStack("ALBUMS").commit();
                    }
                }, false);

                tracksAdapter = new TracksAdapter(songList = ArtistSongLoader.getSongsForArtist(getActivity().getApplicationContext(), artistId), getActivity(), new TracksAdapter.RecyclerItemClickListener() {
                    @Override
                    public void onClickListener(SongInfo song, int position) {
                        Utility.playSong(song, songList, position, getActivity());
                    }
                }, false, null);
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {
            if (albumsPreviewAdapter != null) {
                artistAlbumsRecycler.setAdapter(albumsPreviewAdapter);
            }
            if (tracksAdapter != null) {
                artistSongsRecycler.setAdapter(tracksAdapter);
            }
        }
    }
}
