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

import com.example.monilandharia.musicplayer.adapters.AlbumsAdapter;
import com.example.monilandharia.musicplayer.adapters.ArtistAdapter;
import com.example.monilandharia.musicplayer.dataLoaders.AlbumLoader;
import com.example.monilandharia.musicplayer.dataLoaders.ArtistLoader;
import com.example.monilandharia.musicplayer.models.AlbumInfo;
import com.example.monilandharia.musicplayer.models.ArtistInfo;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArtistsFragment extends Fragment {

    private RecyclerView artistRecycler;
    private GridLayoutManager gridLayoutManager;
    private ArtistAdapter artistAdapter;

    public ArtistsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_artists, container, false);
        artistRecycler = view.findViewById(R.id.recyclerArtists);
        artistRecycler.setHasFixedSize(true);

        gridLayoutManager = new GridLayoutManager(getActivity(),2);
        artistRecycler.setLayoutManager(gridLayoutManager);

        artistAdapter = new ArtistAdapter(getActivity(), ArtistLoader.getAllArtists(getActivity()), new ArtistAdapter.RecyclerItemClickListener() {
            @Override
            public void onClickListener(ArtistInfo artistInfo, int position) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putInt("artistAlbumId",artistInfo.getSongIds()[0]);
                bundle.putLong("artistId",artistInfo.get_id());
                bundle.putString("artistName",artistInfo.getArtistName());
                ArtistDetailsFragment frag = new ArtistDetailsFragment();
                frag.setArguments(bundle);
                //ImageView albumImage = view2.findViewById(R.id.realAlbumArt);
                //Uri albumArtUri = Utility.getAlbumArtUri(albumInfo.getId());
                fragmentTransaction.replace(R.id.fragment_home ,frag).addToBackStack("ALBUMS").commit();
                //Picasso.with(getContext()).load(albumArtUri.toString()).placeholder(R.drawable.placeholder1).into(albumImage);
            }
        }, getActivity().getSupportFragmentManager());
        artistRecycler.setAdapter(artistAdapter);

        return view;
    }

}
