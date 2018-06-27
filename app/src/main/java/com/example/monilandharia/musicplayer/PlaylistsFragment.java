package com.example.monilandharia.musicplayer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.monilandharia.musicplayer.adapters.AlbumsAdapter;
import com.example.monilandharia.musicplayer.adapters.PlaylistsAdapter;
import com.example.monilandharia.musicplayer.dataLoaders.AlbumLoader;
import com.example.monilandharia.musicplayer.database.model.Playlists;
import com.example.monilandharia.musicplayer.models.AlbumInfo;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistsFragment extends Fragment {

    public PlaylistsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playlists, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.playlistRecyclerView);
        recyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        PlaylistsAdapter playlistsAdapter = new PlaylistsAdapter(getActivity(), MainActivity.db.getPlaylists(), new PlaylistsAdapter.RecyclerItemClickListener() {
            @Override
            public void onClickListener(Playlists playlist, int position) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putInt("playlistId", playlist.getPlaylistId());
                bundle.putString("playlistName", playlist.getTitle());
//                Toast.makeText(getActivity(), playlist.getPlaylistId()+" this", Toast.LENGTH_SHORT).show();

                PlaylistDetailsFragment frag = new PlaylistDetailsFragment();
                frag.setArguments(bundle);
                //ImageView albumImage = view2.findViewById(R.id.realAlbumArt);
                //Uri albumArtUri = Utility.getAlbumArtUri(albumInfo.getId());
                fragmentTransaction.replace(R.id.fragment_home, frag).addToBackStack("PLAYLISTDETAILS").commit();
                //Picasso.with(getContext()).load(albumArtUri.toString()).placeholder(R.drawable.placeholder1).into(albumImage);
            }
        }, getActivity().getSupportFragmentManager());
        recyclerView.setAdapter(playlistsAdapter);

        return view;
    }



}
