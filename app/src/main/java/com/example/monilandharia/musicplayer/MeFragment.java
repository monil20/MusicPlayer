package com.example.monilandharia.musicplayer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {

    private ImageView ivPlaylist, ivFavs, ivStats, ivSetings;
    private FragmentTransaction fragmentTransaction = MainActivity.fragmentTransaction;

    public MeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        initViews(view);

        ivPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = MainActivity.fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack("ME");
                fragmentTransaction.show(MainActivity.fragmentPlaylists);
                fragmentTransaction.hide(MainActivity.fragmentMe);
                fragmentTransaction.commit();
            }
        });

        ivSetings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = MainActivity.fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack("ME");
                fragmentTransaction.show(MainActivity.fragmentSettings);
                fragmentTransaction.hide(MainActivity.fragmentMe);
                fragmentTransaction.commit();
            }
        });

        ivStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = MainActivity.fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack("ME");
                fragmentTransaction.show(MainActivity.fragmentStats);
                fragmentTransaction.hide(MainActivity.fragmentMe);
                fragmentTransaction.commit();
            }
        });

        ivFavs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = MainActivity.fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack("ME");
                fragmentTransaction.show(MainActivity.fragmentFavorites);
                fragmentTransaction.hide(MainActivity.fragmentMe);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private void initViews(View view) {
        ivPlaylist = view.findViewById(R.id.imageViewPlaylist);
        ivFavs = view.findViewById(R.id.imageViewFavs);
        ivStats = view.findViewById(R.id.imageViewStats);
        ivSetings = view.findViewById(R.id.imageViewSettings);
    }

}
