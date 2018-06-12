package com.example.monilandharia.musicplayer;


import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.monilandharia.musicplayer.adapters.AlbumsAdapter;
import com.example.monilandharia.musicplayer.adapters.AlbumsPreviewAdapter;
import com.example.monilandharia.musicplayer.dataLoaders.AlbumLoader;
import com.example.monilandharia.musicplayer.models.AlbumInfo;
import com.example.monilandharia.musicplayer.utilities.Utility;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumsFragment extends Fragment {

    private GridLayoutManager gridLayoutManager;

    public AlbumsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_albums, container, false);
        //final View view2 = inflater.inflate(R.layout.fragment_album_details,container,false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerAlbums   );
        recyclerView.setHasFixedSize(true);

        gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlbumsAdapter rcAdapter = new AlbumsAdapter(getActivity(),AlbumLoader.getAllAlbums(getActivity()), new AlbumsAdapter.RecyclerItemClickListener() {
            @Override
            public void onClickListener(AlbumInfo albumInfo, int position) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putLong("albumId",albumInfo.getId());
                bundle.putString("albumName",albumInfo.getTitle());
                bundle.putString("artistName",albumInfo.getArtistName());
                AlbumDetailsFragment frag = new AlbumDetailsFragment();
                frag.setArguments(bundle);
                //ImageView albumImage = view2.findViewById(R.id.realAlbumArt);
                //Uri albumArtUri = Utility.getAlbumArtUri(albumInfo.getId());
                fragmentTransaction.replace(R.id.fragment_home ,frag).addToBackStack("ALBUMS").commit();
                //Picasso.with(getContext()).load(albumArtUri.toString()).placeholder(R.drawable.placeholder1).into(albumImage);
            }
        }, getActivity().getSupportFragmentManager());
        recyclerView.setAdapter(rcAdapter);

        return view;
    }

}
