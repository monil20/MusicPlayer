package com.example.monilandharia.musicplayer;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.graphics.Path;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.monilandharia.musicplayer.dataLoaders.AlbumLoader;
import com.example.monilandharia.musicplayer.dataLoaders.ArtistLoader;
import com.example.monilandharia.musicplayer.dataLoaders.TrackLoader;
import com.example.monilandharia.musicplayer.database.DatabaseHelper;
import com.example.monilandharia.musicplayer.models.AlbumInfo;
import com.example.monilandharia.musicplayer.models.ArtistInfo;
import com.example.monilandharia.musicplayer.models.SongInfo;
import com.example.monilandharia.musicplayer.services.MyService;
import com.example.monilandharia.musicplayer.sms.SmsListener;
import com.example.monilandharia.musicplayer.sms.SmsReceiver;
import com.example.monilandharia.musicplayer.utilities.ObjectSerializer;
import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.WristTwistDetector;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.ohoussein.playpause.PlayPauseView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    private SlidingUpPanelLayout mLayout;
    private CardView cardView;
    private PlayPauseView playPauseView;
    public static TextView trackName, album_artist, startTime, endTime;
    private NowPlayingFragment nowPlayingFragment;
    public static float playPauseOriX, playPauseOriY, trackX, trackY, albumArtistX, albumArtistY, cardViewX, cardViewY, x, y, tempY, tempY1;
    private AHBottomNavigation bottomNavigation;
    private ImageView next, prev, repeat, shuffle;
    private boolean flag = true, flag1 = false;
    private SeekBar seekBar;
    private SlidingUpPanelLayout slidingLayout;
    private RelativeLayout relativeLayout;
    public static Fragment fragmentHome, fragmentMe, fragmentCloud, fragmentSearch, fragmentSettings, fragmentStats, fragmentPlaylists, fragmentFavorites, fragmentTracks, fragmentAlbums, fragmentArtists;
    public static FragmentManager fragmentManager;
    public static FragmentTransaction fragmentTransaction;

    public static MyService myService;
    private ServiceConnection serviceConnection;
    private BroadcastReceiver musicBroadcast;
    private IntentFilter filter;

    public static ArrayList<SongInfo> songs, songsPreview;
    public static ArrayList<AlbumInfo> albums, albumsPreview;
    public static ArrayList<ArtistInfo> artists, artistsPreview;

    public static DatabaseHelper db;

    public static WristTwistDetector.WristTwistListener wristTwistListener;

    public static boolean isSmsEnabled, isGestureControlEnabled;

    private SharedPreferences sharedpreferences;
    private Gson gson;
    public static SongInfo lastSong;
    public static ArrayList<SongInfo> queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestStorageAndMicroPhonePermissions();
    }

    private void requestStorageAndMicroPhonePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WAKE_LOCK,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.MODIFY_AUDIO_SETTINGS,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.RECEIVE_SMS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            albums = AlbumLoader.getAllAlbums(MainActivity.this);
                            songs = TrackLoader.getAllTracks(MainActivity.this);
                            artists = ArtistLoader.getAllArtists(MainActivity.this);
                            albumsPreview = AlbumLoader.getAllAlbums(MainActivity.this, 6);
                            artistsPreview = ArtistLoader.getAllArtists(MainActivity.this, 6);
                            songsPreview = TrackLoader.getAllTracks(MainActivity.this, 6);

                            db = new DatabaseHelper(getApplicationContext());

                            initComponents();
                            initFragments();
                            initSmsService();
                            initGestureControl();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError error) {
                Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_SHORT).show();
            }
        }).onSameThread().check();
    }

    private void initGestureControl() {
        Sensey.getInstance().init(this);
        wristTwistListener = new WristTwistDetector.WristTwistListener() {
            @Override
            public void onWristTwist() {
                // Wrist Twist gesture detected, do something
                Toast.makeText(getApplicationContext(), "Wrist Twisted", Toast.LENGTH_SHORT).show();
            }
        };
        Sensey.getInstance().startWristTwistDetection(wristTwistListener);
    }

    private void initSmsService() {
        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {

            }
        });
    }

    private void showSettingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void initComponents() {

        setContentView(R.layout.activity_main);
        mLayout = findViewById(R.id.sliding_layout);
        nowPlayingFragment = (NowPlayingFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_now_playing);
        cardView = findViewById(R.id.songCardView);
        playPauseView = findViewById(R.id.play_pause);
        trackName = findViewById(R.id.album_track);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        album_artist = findViewById(R.id.album_artist_name);
        next = findViewById(R.id.next);
        prev = findViewById(R.id.prev);
        repeat = findViewById(R.id.repeat);
        shuffle = findViewById(R.id.shuffle);
        seekBar = findViewById(R.id.seekBar);
        startTime = findViewById(R.id.start_time);
        endTime = findViewById(R.id.end_time);
        slidingLayout = findViewById(R.id.sliding_layout);
        relativeLayout = findViewById(R.id.relativelayout);


        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedpreferences.contains("lastSong"))
        {
            gson = new Gson();
            String json = sharedpreferences.getString("lastSong","");
            lastSong = gson.fromJson(json,SongInfo.class);
        }
        if(sharedpreferences.contains("lastqueue"))
        {
            try {
                queue = (ArrayList<SongInfo>) ObjectSerializer.deserialize(sharedpreferences.getString("lastqueue",ObjectSerializer.serialize(new ArrayList<SongInfo>())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void initFragments() {
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.house_outline, android.R.color.white);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Me", R.drawable.avatar, android.R.color.white);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Cloud", R.drawable.cloud, android.R.color.white);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("Search", R.drawable.search_white, android.R.color.white);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);

//        bottomNavigation.setTranslucentNavigationEnabled(true);
        bottomNavigation.setAccentColor(Color.parseColor("#8f367c"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        bottomNavigation.setCurrentItem(0);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(getListener());

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentHome = new HomeFragment();
        fragmentMe = new MeFragment();
        fragmentCloud = new CloudFragment();
        fragmentSearch = new SearchFragment();
        fragmentSettings = new SettingsFragment();
        fragmentPlaylists = new PlaylistsFragment();
        fragmentStats = new StatsFragment();
        fragmentFavorites = new FavoritesFragment();
        fragmentTracks = new TracksFragment();
        fragmentAlbums = new AlbumsFragment();
        fragmentArtists = new ArtistsFragment();
        fragmentTransaction.add(R.id.fragment_home, fragmentHome);
        fragmentTransaction.add(R.id.fragment_home, fragmentMe);
        fragmentTransaction.add(R.id.fragment_home, fragmentCloud);
        fragmentTransaction.add(R.id.fragment_home, fragmentSearch);
        fragmentTransaction.add(R.id.fragment_home, fragmentSettings);
        fragmentTransaction.add(R.id.fragment_home, fragmentPlaylists);
        fragmentTransaction.add(R.id.fragment_home, fragmentFavorites);
        fragmentTransaction.add(R.id.fragment_home, fragmentStats);
        fragmentTransaction.add(R.id.fragment_home, fragmentAlbums);
        fragmentTransaction.add(R.id.fragment_home, fragmentArtists);
        fragmentTransaction.add(R.id.fragment_home, fragmentTracks);
        fragmentTransaction.hide(fragmentMe);
        fragmentTransaction.hide(fragmentCloud);
        fragmentTransaction.hide(fragmentSearch);
        fragmentTransaction.hide(fragmentSettings);
        fragmentTransaction.hide(fragmentStats);
        fragmentTransaction.hide(fragmentFavorites);
        fragmentTransaction.hide(fragmentPlaylists);
        fragmentTransaction.hide(fragmentArtists);
        fragmentTransaction.hide(fragmentAlbums);
        fragmentTransaction.hide(fragmentTracks);
        fragmentTransaction.commit();

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0: {
                        if (!wasSelected) {
                            if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
                                int index = getSupportFragmentManager().getBackStackEntryCount() - 1;
                                FragmentManager.BackStackEntry backStackEntry = getSupportFragmentManager().getBackStackEntryAt(index);
                                String tag = backStackEntry.getName();
                                if (tag.equals("ME")) {
                                    fragmentManager.popBackStack();
                                }
                            }
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.show(fragmentHome);
                            fragmentTransaction.hide(fragmentMe);
                            fragmentTransaction.hide(fragmentCloud);
                            fragmentTransaction.hide(fragmentSearch);
                            fragmentTransaction.hide(fragmentSettings);
                            fragmentTransaction.hide(fragmentStats);
                            fragmentTransaction.hide(fragmentFavorites);
                            fragmentTransaction.hide(fragmentPlaylists);
                            fragmentTransaction.hide(fragmentArtists);
                            fragmentTransaction.hide(fragmentAlbums);
                            fragmentTransaction.hide(fragmentTracks);
                            fragmentTransaction.commit();
                        }
                        break;
                    }
                    case 1: {
                        if (!wasSelected) {
                            if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
                                int index = getSupportFragmentManager().getBackStackEntryCount() - 1;
                                FragmentManager.BackStackEntry backStackEntry = getSupportFragmentManager().getBackStackEntryAt(index);
                                String tag = backStackEntry.getName();
                                if (tag.equals("HOME")) {
                                    fragmentManager.popBackStack();
                                }
                            }
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.show(fragmentMe);
                            fragmentTransaction.hide(fragmentHome);
                            fragmentTransaction.hide(fragmentCloud);
                            fragmentTransaction.hide(fragmentSearch);
                            fragmentTransaction.hide(fragmentSettings);
                            fragmentTransaction.hide(fragmentStats);
                            fragmentTransaction.hide(fragmentFavorites);
                            fragmentTransaction.hide(fragmentPlaylists);
                            fragmentTransaction.hide(fragmentArtists);
                            fragmentTransaction.hide(fragmentAlbums);
                            fragmentTransaction.hide(fragmentTracks);
                            fragmentTransaction.commit();
                        }
                        break;
                    }
                    case 2: {
                        if (!wasSelected) {
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.show(fragmentCloud);
                            fragmentTransaction.hide(fragmentHome);
                            fragmentTransaction.hide(fragmentMe);
                            fragmentTransaction.hide(fragmentSearch);
                            fragmentTransaction.hide(fragmentSettings);
                            fragmentTransaction.hide(fragmentStats);
                            fragmentTransaction.hide(fragmentFavorites);
                            fragmentTransaction.hide(fragmentPlaylists);
                            fragmentTransaction.hide(fragmentArtists);
                            fragmentTransaction.hide(fragmentAlbums);
                            fragmentTransaction.hide(fragmentTracks);
                            fragmentTransaction.commit();
                        }
                        break;
                    }
                    case 3: {
                        if (!wasSelected) {
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.show(fragmentSearch);
                            fragmentTransaction.hide(fragmentHome);
                            fragmentTransaction.hide(fragmentMe);
                            fragmentTransaction.hide(fragmentCloud);
                            fragmentTransaction.hide(fragmentSettings);
                            fragmentTransaction.hide(fragmentStats);
                            fragmentTransaction.hide(fragmentFavorites);
                            fragmentTransaction.hide(fragmentPlaylists);
                            fragmentTransaction.hide(fragmentArtists);
                            fragmentTransaction.hide(fragmentAlbums);
                            fragmentTransaction.hide(fragmentTracks);
                            fragmentTransaction.commit();
                        }
                        break;
                    }
                }
                return true;
            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                // Manage the new y position
            }
        });

        playPauseView.post(new Runnable() {
            @Override
            public void run() {
                playPauseOriX = playPauseView.getX();
                playPauseOriY = playPauseView.getY();
                playPauseView.setX(playPauseOriX + playPauseOriX + 10);
                playPauseView.setY(-30);
                trackX = trackName.getX();
                trackY = trackName.getY();
                trackName.setX(250);
                trackName.setY(30);
                albumArtistX = album_artist.getX();
                albumArtistY = album_artist.getY();
                album_artist.setX(250);
                tempY = y;
                album_artist.setY(y + trackName.getHeight() + 26);
                cardViewX = cardView.getX();
                cardViewY = cardView.getY();
                cardView.setScaleX(0.28f);
                cardView.setScaleY(0.23f);
                cardView.setX(trackName.getY());
                cardView.setY(trackName.getY());
                tempY1 = trackName.getY();
                cardView.setPivotX(0);
                cardView.setPivotY(0);

                flag1 = true;
            }
        });

        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i("Slide", "onPanelSlide, offset " + slideOffset);

            }

            @Override
            public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
                Log.i("State", "onPanelStateChanged " + newState);
                if (newState.toString().equals("DRAGGING")) {
                    if (previousState.toString().equals("EXPANDED")) {
                        flag = false;
                    }
                }
                if (newState.toString().equals("EXPANDED")) {
//                    bottomNavigation.setVisibility(View.INVISIBLE);
//                    SlidingUpPanelLayout.LayoutParams layoutParams = (SlidingUpPanelLayout.LayoutParams) slidingLayout.getLayoutParams();
//                    layoutParams.add

                    x = playPauseView.getX();
                    y = playPauseView.getY();
                    Path path = new Path();
                    path.moveTo(x, y);
                    path.lineTo(playPauseOriX, playPauseOriY);
                    ObjectAnimator objectAnimator =
                            ObjectAnimator.ofFloat(playPauseView, View.X,
                                    View.Y, path);
                    objectAnimator.setDuration(300);

                    Path path1 = new Path();
                    x = trackName.getX();
                    y = trackName.getY();
                    path1.moveTo(x, y);
                    path1.lineTo(trackX, trackY);
                    ObjectAnimator objectAnimator1 =
                            ObjectAnimator.ofFloat(trackName, View.X,
                                    View.Y, path1);
                    objectAnimator1.setDuration(220);

                    Path path2 = new Path();
                    x = album_artist.getX();
                    y = album_artist.getY();
                    path2.moveTo(x, y);
                    path2.lineTo(albumArtistX, albumArtistY);
                    ObjectAnimator objectAnimator2 =
                            ObjectAnimator.ofFloat(album_artist, View.X,
                                    View.Y, path2);
                    objectAnimator2.setDuration(250);

                    Path path3 = new Path();
                    x = cardView.getX();
                    y = cardView.getY();
                    path3.moveTo(x, y);
                    path3.lineTo(cardViewX, cardViewY);
                    ObjectAnimator objectAnimator3 =
                            ObjectAnimator.ofFloat(cardView, View.X,
                                    View.Y, path3);
                    objectAnimator2.setDuration(200);

                    Animation animationFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);

                    if (flag) {
                        objectAnimator.start();
                        objectAnimator1.start();
                        objectAnimator2.start();
                        objectAnimator3.start();
                        cardView.setScaleX(1);
                        cardView.setScaleY(1);
                        prev.startAnimation(animationFadeIn);
                        next.startAnimation(animationFadeIn);
                        shuffle.startAnimation(animationFadeIn);
                        repeat.startAnimation(animationFadeIn);
                        cardView.startAnimation(animationFadeIn);
                        seekBar.setAnimation(animationFadeIn);
                        startTime.setAnimation(animationFadeIn);
                        endTime.setAnimation(animationFadeIn);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                            }
                        }, 900);
                        repeat.setAlpha(1.0f);
                        shuffle.setAlpha(1.0f);
                        next.setAlpha(1.0f);
                        prev.setAlpha(1.0f);
                        cardView.setAlpha(1.0f);
                        seekBar.setAlpha(1.0f);
                        startTime.setAlpha(1.0f);
                        endTime.setAlpha(1.0f);

                    }

                    flag = true;
                    flag1 = false;

                }
                if (newState.toString().equals("COLLAPSED")) {
                    if (!flag1) {
                        x = playPauseView.getX();
                        playPauseOriX = x;
                        y = playPauseView.getY();
                        playPauseOriY = y;
                        Path path = new Path();
                        path.moveTo(x, y);
                        path.lineTo(x + x + 10, -30);
                        ObjectAnimator objectAnimator =
                                ObjectAnimator.ofFloat(playPauseView, View.X,
                                        View.Y, path);
                        objectAnimator.setDuration(0);

                        Log.i("height", playPauseView.getHeight() + "");

                        x = trackName.getX();
                        trackX = x;
                        y = trackName.getY();
                        trackY = y;
                        Path path1 = new Path();
                        path1.moveTo(x, y);
                        path1.lineTo(250, 30);
                        ObjectAnimator objectAnimator1 =
                                ObjectAnimator.ofFloat(trackName, View.X,
                                        View.Y, path1);
                        objectAnimator1.setDuration(0);

                        x = album_artist.getX();
                        albumArtistX = x;
                        y = album_artist.getY();
                        albumArtistY = y;
                        Path path2 = new Path();
                        path2.moveTo(x, y);
                        path2.lineTo(250, tempY + trackName.getHeight() + 26);
                        ObjectAnimator objectAnimator2 =
                                ObjectAnimator.ofFloat(album_artist, View.X,
                                        View.Y, path2);
                        objectAnimator2.setDuration(0);

                        x = cardView.getX();
                        cardViewX = x;
                        y = cardView.getY();
                        cardViewY = y;
                        cardView.setScaleX(0.28f);
                        cardView.setScaleY(0.23f);
                        Path path3 = new Path();
                        path3.moveTo(x, y);
                        path3.lineTo(tempY1, tempY1);
                        ObjectAnimator objectAnimator3 =
                                ObjectAnimator.ofFloat(cardView, View.X,
                                        View.Y, path3);
                        objectAnimator3.setDuration(0);


                        objectAnimator.start();
                        objectAnimator1.start();
                        objectAnimator2.start();
                        objectAnimator3.start();
                        next.setAlpha(0.0f);
                        prev.setAlpha(0.0f);
                        repeat.setAlpha(0.0f);
                        shuffle.setAlpha(0.0f);
//                        cardView.setAlpha(1.0f);
                        seekBar.setAlpha(0.0f);
                        startTime.setAlpha(0.0f);
                        endTime.setAlpha(0.0f);

                        flag = true;
                        flag1 = true;
                    }
                }
            }
        });

        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(PanelState.COLLAPSED);
            }
        });
    }

    @Override
    protected void onStart() {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MyService.MusicBinder binder = (MyService.MusicBinder) service;
                myService = binder.getService();
                if(lastSong!=null) {
                    myService.updateUI(lastSong);
                    myService.setCurrSong(lastSong);
                    myService.setList(queue);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };

        super.onStart();
        Intent serviceIntent = new Intent(this, MyService.class);
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == PanelState.EXPANDED || mLayout.getPanelState() == PanelState.ANCHORED)) {
            mLayout.setPanelState(PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (myService != null && myService.isMusicPlaying()) {
            myService.updateUI(myService.getCurrSong());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extra = intent.getExtras();
        if (extra != null) {
            if (extra.containsKey("fromNotification")) {
                slidingLayout.setPanelState(PanelState.EXPANDED);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private FragmentManager.OnBackStackChangedListener getListener() {
        return new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Log.i("XXX", "Here");
                HomeFragment.recyclerAlbums.scrollToPosition(0);
                HomeFragment.albumsPreviewAdapter.filterAlbums(albumsPreview);

                HomeFragment.recyclerArtists.scrollToPosition(0);
                HomeFragment.artistsPreviewAdapter.filterArtists(artistsPreview);

                HomeFragment.recyclerRecentlyAdded.scrollToPosition(0);
                HomeFragment.tracksPreviewAdapter.filterTracks(songsPreview);
            }
        };
    }

}
