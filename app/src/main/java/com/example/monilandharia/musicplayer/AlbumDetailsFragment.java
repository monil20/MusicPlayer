package com.example.monilandharia.musicplayer;


import android.graphics.Path;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.shapeofview.ShapeOfView;
import com.github.florent37.shapeofview.manager.ClipPathManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumDetailsFragment extends Fragment {


    public AlbumDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_album_details, container, false);
        ShapeOfView shapeOfView = view.findViewById(R.id.albumArt);
//        shapeOfView.setClipPathCreator(new ClipPathManager.ClipPathCreator() {
//            @Override
//            public Path createClipPath(int width, int height) {
//                Path path = new Path();
//                retrn
//            }
//        });
        return view;
    }

}
