package com.example.monilandharia.musicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cleveroad.audiovisualization.AudioVisualization;
import com.cleveroad.audiovisualization.DbmHandler;
import com.cleveroad.audiovisualization.GLAudioVisualizationView;
import com.example.monilandharia.musicplayer.models.SongInfo;
import com.example.monilandharia.musicplayer.utilities.Utility;
import com.ohoussein.playpause.PlayPauseView;

import java.util.Locale;

public class NowPlayingFragment extends Fragment {

    private AudioVisualization audioVisualization;
    public static TextView album_track, album_artist_name, start_time, end_time;
    public static SeekBar seekBar;
    public static PlayPauseView playPauseView;
    public static ImageView songArt, prev, next;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_now_playing, container, false);
        init(view);
        setListeners();
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
        songArt = view.findViewById(R.id.songArt);
        prev = view.findViewById(R.id.prev);
        next = view.findViewById(R.id.next);
        formatViews();

        IntentFilter filter = new IntentFilter();
        filter.addAction("pauseSong");
        filter.addAction("resumeSong");
        getActivity().registerReceiver(toggleBroadcast, filter);
    }

    private void setListeners() {
        playPauseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), playPauseView.isPlay() + "", Toast.LENGTH_SHORT).show();
                if (!playPauseView.isPlay()) {
                    playPauseView.toggle();
                    MainActivity.myService.pauseSong();
                    MainActivity.myService.togglePlayPauseNotification(1);
                } else {
                    playPauseView.toggle();
                    MainActivity.myService.resumeSong();
//                    if(MainActivity.myService.isNotifvisible()) {
                    MainActivity.myService.togglePlayPauseNotification(2);
//                    }
//                    else
//                    {
//                        Intent intent = new Intent(getActivity(),MyService.class);
//                        getActivity().startService(intent);
//                    }
                }

            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.myService.playPrevious();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.myService.playNext();
            }
        });
    }

    private void formatViews() {
        seekBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
//        seekBar.getThumb().mutate().setAlpha(0);

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

    BroadcastReceiver toggleBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("pauseSong".equals(action)) {
                playPauseView.toggle(true);
            } else if ("resumeSong".equals(action)) {
                playPauseView.toggle(true);
            }
        }
    };
}
