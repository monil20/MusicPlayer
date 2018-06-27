package com.example.monilandharia.musicplayer.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.monilandharia.musicplayer.MainActivity;
import com.example.monilandharia.musicplayer.NowPlayingFragment;
import com.example.monilandharia.musicplayer.R;
import com.example.monilandharia.musicplayer.models.SongInfo;
import com.example.monilandharia.musicplayer.utilities.ObjectSerializer;
import com.example.monilandharia.musicplayer.utilities.Utility;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

public class MyService extends Service {

    private static MediaPlayer mediaPlayer;
    private ArrayList queueToPlay;
    private MediaSessionCompat mediaSessionCompat;
    private NotificationChannel mChannel;
    private NotificationManager notificationManager;
    private final IBinder musicBind = new MusicBinder();
    private SongInfo currSong;
    private Bitmap img;
    private PendingIntent pendingIntentNextSong, pendingIntentPrevSong, pendingIntentPauseSong, pendingIntentResumeSong, pendingIntentClick;
    private int currentIndex;
    private NotificationCompat.Builder builder;
    private boolean notifvisible;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public boolean isNotifvisible() {
        return notifvisible;
    }

    public void setNotifvisible(boolean notifvisible) {
        this.notifvisible = notifvisible;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        img = Utility.getAlbumArtFromData(currSong, getApplicationContext());
        IntentFilter filter = new IntentFilter("nextSong");
        filter.addAction("prevSong");
        filter.addAction("resumeSong");
        filter.addAction("pauseSong");
        this.registerReceiver(myReceiver, filter);
        mediaSessionCompat = new MediaSessionCompat(this, "tag");
//        Notification notification = new NotificationCompat.Builder(this, "1")
//                .setSmallIcon(R.mipmap.icon)
//                .setContentTitle(currSong.getSong_name())
//                .setContentText(currSong.getSong_artist())
//                .setLargeIcon(img)
//                .addAction(R.drawable.baseline_repeat_white_24, "REPEAT", null)
//                .addAction(R.drawable.baseline_skip_previous_white_24, "PREVIOUS", null)
//                .addAction(android.R.drawable.ic_media_play, "PLAY", null)
//                .addAction(R.drawable.baseline_skip_next_white_24, "NEXT", pendingIntentNextSong)
//                .addAction(R.drawable.baseline_shuffle_white_24, "SHUFFLE", null)
//                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
//                        .setShowActionsInCompactView(1, 2, 3)
//                        .setMediaSession(mediaSessionCompat.getSessionToken()))
//                .setSubText("My Player")
//                .setPriority(NotificationCompat.PRIORITY_LOW)
//                .build();
//
//        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            mChannel = new NotificationChannel("1", "mychannel", NotificationManager.IMPORTANCE_NONE);
//            notificationManager.createNotificationChannel(mChannel);
//        }
        prepareSong(currSong);
        startForeground(1, getNotification(currSong));
        notifvisible = true;
//        startForeground(1, notification);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mediaPlayer.stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    public void playSong(final SongInfo song) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                    mediaPlayer.start();
                    NowPlayingFragment.album_track.setText(song.getSong_name());
                }
            });
        }
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(song.getData());
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class MusicBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }

    public void setList(ArrayList<SongInfo> theSongs) {
        queueToPlay = theSongs;
    }

    public void setCurrSong(SongInfo song) {
        this.currSong = song;
    }

    private void prepareSong(final SongInfo song) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefedit = sharedPreferences.edit();
        gson = new Gson();
        String json = gson.toJson(song);
        prefedit.putString("lastSong", json);
        prefedit.commit();

        try {
            prefedit.putString("lastqueue", ObjectSerializer.serialize(queueToPlay));
        } catch (IOException e) {
            e.printStackTrace();
        }
        prefedit.commit();

        updateUI(song);
        NowPlayingFragment.playPauseView.change(false, true);
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                    mediaPlayer.start();
                    HandleSeekBar();
                    final Handler mHandler = new Handler();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            NowPlayingFragment.seekBar.setMax(song.getSong_duration() / 1000);
                            int currentposition = mediaPlayer.getCurrentPosition() / 1000;
                            NowPlayingFragment.seekBar.setProgress(currentposition);
                            NowPlayingFragment.start_time.setText(Utility.getTime(mediaPlayer.getCurrentPosition()));
                            mHandler.postDelayed(this, 1);
                        }
                    }, 1);
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (currentIndex + 1 < queueToPlay.size()) {
                        SongInfo next = (SongInfo) queueToPlay.get(currentIndex + 1);
                        changeSelectedSong(currentIndex + 1);
                        prepareSong(next);
                    } else {
                        SongInfo next = (SongInfo) queueToPlay.get(0);
                        changeSelectedSong(0);
                        prepareSong(next);
                    }
                }
            });
        }
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(song.getData());
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pauseSong() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            stopForeground(false);
            notifvisible = false;
            mediaPlayer.pause();
        }
    }

    public void resumeSong() {
        if (mediaPlayer != null && !mediaPlayer.isLooping()) {
            if (!notifvisible) {
                startForeground(1, getNotification(currSong));
            }
            mediaPlayer.start();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setListeners();
    }

    private void setListeners() {
        Intent nextSong = new Intent("nextSong");
        pendingIntentNextSong = PendingIntent.getBroadcast(this, 12345, nextSong, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent previousSong = new Intent("prevSong");
        pendingIntentPrevSong = PendingIntent.getBroadcast(this, 12345, previousSong, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent pauseSong = new Intent("pauseSong");
        pendingIntentPauseSong = PendingIntent.getBroadcast(this, 12345, pauseSong, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent resumeSong = new Intent("resumeSong");
        pendingIntentResumeSong = PendingIntent.getBroadcast(this, 12345, resumeSong, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent clickIntent = new Intent(getApplicationContext(), MainActivity.class);
        clickIntent.putExtra("fromNotification", "openActivity");
        clickIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntentClick = PendingIntent.getActivity(getApplicationContext(), 1, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void playPrevious() {
        if (mediaPlayer != null) {
            if (currentIndex - 1 >= 0) {
                SongInfo previous = (SongInfo) queueToPlay.get(currentIndex - 1);
                currSong = previous;
                changeSelectedSong(currentIndex - 1);
                prepareSong(previous);
                updateNotification(currSong);
            } else {
                changeSelectedSong(queueToPlay.size() - 1);
                prepareSong((SongInfo) queueToPlay.get(queueToPlay.size() - 1));
                currSong = (SongInfo) queueToPlay.get(queueToPlay.size() - 1);
                updateNotification(currSong);
            }
        }
    }

    private void HandleSeekBar() {
        NowPlayingFragment.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void changeSelectedSong(int index) {
        currentIndex = index;
    }

    public void playNext() {
        if (mediaPlayer != null) {
            if (currentIndex + 1 < queueToPlay.size()) {
                SongInfo next = (SongInfo) queueToPlay.get(currentIndex + 1);
                currSong = next;
                changeSelectedSong(currentIndex + 1);
                prepareSong(next);
                updateNotification(currSong);
            } else {
                changeSelectedSong(0);
                prepareSong((SongInfo) queueToPlay.get(0));
                currSong = (SongInfo) queueToPlay.get(0);
                updateNotification(currSong);
            }
        }
    }

    private Notification getNotification(SongInfo songc) {
        img = Utility.getAlbumArtFromData(songc, getApplicationContext());
        Toast.makeText(this, songc.getSong_name(), Toast.LENGTH_SHORT).show();
        builder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.mipmap.icon)
                .setContentTitle(songc.getSong_name())
                .setContentText(songc.getSong_artist())
                .setLargeIcon(img)
                .addAction(R.drawable.baseline_repeat_white_24, "REPEAT", null)
                .addAction(R.drawable.baseline_skip_previous_white_24, "PREVIOUS", pendingIntentPrevSong)
                .addAction(android.R.drawable.ic_media_pause, "PAUSE", pendingIntentPauseSong)
                .addAction(R.drawable.baseline_skip_next_white_24, "NEXT", pendingIntentNextSong)
                .addAction(R.drawable.baseline_shuffle_white_24, "SHUFFLE", null)
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(1, 2, 3)
                        .setMediaSession(mediaSessionCompat.getSessionToken()))
                .setSubText("My Player")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(pendingIntentClick);
        Notification notification = builder.build();

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel("1", "mychannel", NotificationManager.IMPORTANCE_NONE);
            notificationManager.createNotificationChannel(mChannel);
        }
        return notification;
    }

    private void updateNotification(SongInfo songc) {
        Toast.makeText(this, "inside update", Toast.LENGTH_SHORT).show();
        Bitmap imgs = Utility.getAlbumArtFromData(songc, getApplicationContext());
        builder.setContentTitle(songc.getSong_name());
        builder.setContentText(songc.getSong_artist());
        builder.setLargeIcon(imgs);
        builder.mActions.set(2, new NotificationCompat.Action(android.R.drawable.ic_media_pause, "PAUSE", pendingIntentPauseSong));
        notificationManager.notify(1, builder.build());
    }

    public void togglePlayPauseNotification(int x) {
        if (x == 1) {
            builder.mActions.set(2, new NotificationCompat.Action(android.R.drawable.ic_media_play, "PLAY", pendingIntentResumeSong));
        } else if (x == 2) {
            builder.mActions.set(2, new NotificationCompat.Action(android.R.drawable.ic_media_pause, "PAUSE", pendingIntentPauseSong));
        }
        notificationManager.notify(1, builder.build());
    }

    private final BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if ("nextSong".equals(action)) {
                playNext();
            } else if ("prevSong".equals(action)) {
                playPrevious();
            } else if ("pauseSong".equals(action)) {
                pauseSong();
                togglePlayPauseNotification(1);
                //NowPlayingFragment.playPauseView.toggle(true);
            } else if ("resumeSong".equals(action)) {
                resumeSong();
                togglePlayPauseNotification(2);
                //NowPlayingFragment.playPauseView.toggle(true);
            }
        }
    };

    public SongInfo getCurrSong() {
        return currSong;
    }

    public void updateUI(SongInfo currSong) {
        NowPlayingFragment.album_track.setText(currSong.getSong_name());
        NowPlayingFragment.album_artist_name.setText(currSong.getSong_artist());
        NowPlayingFragment.end_time.setText(Utility.getTime(currSong.getSong_duration()));

        Bitmap img = Utility.getAlbumArtFromData(currSong, getApplicationContext());
        NowPlayingFragment.songArt.setImageBitmap(img);

//        NowPlayingFragment.playPauseView.change(false,true);
    }

    public boolean isMusicPlaying() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        } else {
            return false;
        }
    }

}
