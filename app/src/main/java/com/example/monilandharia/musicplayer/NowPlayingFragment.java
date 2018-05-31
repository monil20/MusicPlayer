package com.example.monilandharia.musicplayer;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cleveroad.audiovisualization.AudioVisualization;
import com.cleveroad.audiovisualization.DbmHandler;
import com.cleveroad.audiovisualization.GLAudioVisualizationView;
import com.ohoussein.playpause.PlayPauseView;

import java.util.Locale;

public class NowPlayingFragment extends Fragment {

    public static NowPlayingFragment newInstance() {
        return new NowPlayingFragment();
    }

    private AudioVisualization audioVisualization;
    private TextView album_track, album_artist_name, start_time, end_time;
    private SeekBar seekBar;
    private PlayPauseView playPauseView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_now_playing, container, false);
        init(view);
        playPauseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(),playPauseView.isPlay()+"",Toast.LENGTH_SHORT).show();
                playPauseView.toggle();
            }
        });
        return view;
    }

    private void init(View view) {
        audioVisualization = view.findViewById(R.id.visualizer_view);
        album_track = view.findViewById(R.id.album_track);
        album_artist_name = view.findViewById(R.id.album_artist_name);
        start_time = view.findViewById(R.id.start_time);
        end_time = view.findViewById(R.id.end_time);
        seekBar = view.findViewById(R.id.seekBar);
        playPauseView = view.findViewById(R.id.play_pause);
        formatViews();
    }

    private void formatViews() {
        seekBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
//        seekBar.getThumb().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().mutate().setAlpha(0);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        audioVisualization = (AudioVisualization) view;
        audioVisualization.linkTo(DbmHandler.Factory.newVisualizerHandler(getContext(), 0));
    }

    @Override
    public void onResume() {
        super.onResume();
        audioVisualization.onResume();
    }

    @Override
    public void onPause() {
        audioVisualization.onPause();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        audioVisualization.release();
        super.onDestroyView();
    }
}
