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

import com.example.monilandharia.musicplayer.adapters.TracksAdapter;
import com.example.monilandharia.musicplayer.dataLoaders.AlbumSongLoader;
import com.example.monilandharia.musicplayer.models.SongInfo;
import com.example.monilandharia.musicplayer.utilities.Utility;
import com.ohoussein.playpause.PlayPauseView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumDetailsFragment extends Fragment {

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView albumSongsRecycler;
    private TracksAdapter adapter;
    private long albumId;
    private String albumName;
    private ImageView ivAlbumImage;
    private TextView tvAlbumName;
    private PlayPauseView playPauseView;
    private ArrayList<SongInfo> songList;

    public AlbumDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_details, container, false);
        initView(view);
        Bundle bundle = getArguments();
        albumId = bundle.getLong("albumId");
        albumName = bundle.getString("albumName");

        Uri albumArtUri = Utility.getAlbumArtUri(albumId);
        Picasso.with(getContext()).load(albumArtUri.toString()).placeholder(R.drawable.placeholder1).into(ivAlbumImage);
        tvAlbumName.setText(albumName);
        if(getActivity()!=null)
        {
            new loadAlbumTracks().execute("");
        }
        playPauseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    private void initView(View view)
    {

        ivAlbumImage = view.findViewById(R.id.artistImage);
        tvAlbumName = view.findViewById(R.id.albumName);

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false);

        albumSongsRecycler = view.findViewById(R.id.recyclerAlbumSongs);
        albumSongsRecycler.setHasFixedSize(true);
        albumSongsRecycler.setLayoutManager(layoutManager);
        albumSongsRecycler.setOverScrollMode(View.OVER_SCROLL_NEVER);
        playPauseView = view.findViewById(R.id.playPauseView);

    }

    private class loadAlbumTracks extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            if (getActivity() != null)
                adapter = new TracksAdapter(songList = AlbumSongLoader.getSongsForAlbum(getActivity().getApplicationContext(),albumId),getActivity(), new TracksAdapter.RecyclerItemClickListener(){
                    @Override
                    public void onClickListener(SongInfo song, int position) {

                    }
                });
            return "Executed";        }

        @Override
        protected void onPostExecute(String s) {
            if(adapter !=null) {
                albumSongsRecycler.setAdapter(adapter);
            }
        }
    }
}
