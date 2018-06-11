package com.example.monilandharia.musicplayer;


import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.monilandharia.musicplayer.utilities.Utility;
import com.github.florent37.shapeofview.ShapeOfView;
import com.github.florent37.shapeofview.manager.ClipPathManager;
import com.squareup.picasso.Picasso;


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
        Bundle bundle = getArguments();
        Long albumId = bundle.getLong("albumId");
        ImageView albumImage = view.findViewById(R.id.realAlbumArt);
        Uri albumArtUri = Utility.getAlbumArtUri(albumId);
        Picasso.with(getContext()).load(albumArtUri.toString()).placeholder(R.drawable.placeholder1).into(albumImage);
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
