package com.example.monilandharia.musicplayer;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.monilandharia.musicplayer.adapters.AlbumsAdapter;
import com.example.monilandharia.musicplayer.adapters.TracksAdapter;
import com.example.monilandharia.musicplayer.dataLoaders.ArtistAlbumLoader;
import com.example.monilandharia.musicplayer.dataLoaders.ArtistSongLoader;
import com.example.monilandharia.musicplayer.models.AlbumInfo;
import com.example.monilandharia.musicplayer.models.SongInfo;
import com.example.monilandharia.musicplayer.utilities.Utility;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArtistDetailsFragment extends Fragment {

    private int artistAlbumId;
    private long artistId;
    private String artistName;
    private RecyclerView artistAlbumsRecycler,artistSongsRecycler;
    private TracksAdapter tracksAdapter;
    private AlbumsAdapter albumsAdapter;
    private ImageView artistImage;
    private TextView artistNameTextview;
    private RecyclerView.LayoutManager songLayoutManager,albumLayoutManager;

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
        if(getActivity()!=null)
        {
            new loadArtistAlbumsAndSongs().execute("");
        }
        return view;
    }

    private void initViews(View view)
    {
        artistNameTextview = view.findViewById(R.id.artitstName);
        artistImage = view.findViewById(R.id.artistImageArt);
        songLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        albumLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);

        artistAlbumsRecycler = view.findViewById(R.id.recyclerArtistAlbums);
        artistAlbumsRecycler.setHasFixedSize(true);
        artistAlbumsRecycler.setLayoutManager(albumLayoutManager);
        artistAlbumsRecycler.setOverScrollMode(View.OVER_SCROLL_NEVER);

        artistSongsRecycler = view.findViewById(R.id.recyclerArtistSongs);
        artistSongsRecycler.setHasFixedSize(true);
        artistSongsRecycler.setLayoutManager(songLayoutManager);
        artistSongsRecycler.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    private class loadArtistAlbumsAndSongs extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            if (getActivity() != null) {
                albumsAdapter = new AlbumsAdapter(getActivity(), ArtistAlbumLoader.getAlbumsForArtist(getActivity(), artistId), new AlbumsAdapter.RecyclerItemClickListener() {
                    @Override
                    public void onClickListener(AlbumInfo albumInfo, int position) {

                    }
                }, getActivity().getSupportFragmentManager());

                tracksAdapter = new TracksAdapter(ArtistSongLoader.getSongsForArtist(getActivity().getApplicationContext(),artistId),getActivity(), new TracksAdapter.RecyclerItemClickListener(){
                    @Override
                    public void onClickListener(SongInfo song, int position) {

                    }
                });
                }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {
            if(albumsAdapter !=null) {
                artistAlbumsRecycler.setAdapter(albumsAdapter);
            }
            if(tracksAdapter !=null) {
                artistSongsRecycler.setAdapter(tracksAdapter);
            }
        }
    }
}
